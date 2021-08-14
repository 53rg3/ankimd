package core.tts;


import com.google.api.gax.core.FixedCredentialsProvider;
import com.google.auth.oauth2.ServiceAccountCredentials;
import com.google.cloud.texttospeech.v1.*;
import com.google.protobuf.ByteString;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static config.Config.*;

public class WaveNetApiScraper {

    private final VoiceSelectionParams voice;
    private final AudioConfig audioConfig;
    private final TextToSpeechSettings textToSpeechSettings;

    public WaveNetApiScraper(String languageCode, String voiceName) {
        this.voice = VoiceSelectionParams.newBuilder()
                .setLanguageCode(languageCode)
                .setName(voiceName)
                .setSsmlGender(SsmlVoiceGender.MALE)
                .build();
        this.audioConfig = AudioConfig.newBuilder()
                .setAudioEncoding(AudioEncoding.MP3)
                .build();
        try {
            final ServiceAccountCredentials credentials = ServiceAccountCredentials
                    .fromStream(Files.newInputStream(WAVENET_KEY));
            this.textToSpeechSettings = TextToSpeechSettings.newBuilder()
                    .setCredentialsProvider(FixedCredentialsProvider.create(credentials)).build();
        } catch (final IOException e) {
            throw new IllegalStateException("Couldn't load credentials.", e);
        }
    }

    public void create(final String textToAudioFrom, Path pathToFile) {
        try (final TextToSpeechClient textToSpeechClient = TextToSpeechClient.create(this.textToSpeechSettings)) {
            final SynthesisInput input = SynthesisInput.newBuilder().setText(textToAudioFrom).build();
            final SynthesizeSpeechResponse response = textToSpeechClient.synthesizeSpeech(input, this.voice, this.audioConfig);
            final ByteString audioContents = response.getAudioContent();
            this.writeToFile(audioContents, pathToFile.toAbsolutePath().toString());
        } catch (final Exception e) {
            throw new IllegalStateException("Couldn't create mp3.", e);
        }
    }

    private void writeToFile(final ByteString audioContents, final String absolutePathToFile) {
        try (final OutputStream out = new FileOutputStream(Paths.get(absolutePathToFile).toFile())) {
            out.write(audioContents.toByteArray());
        } catch (final Exception e) {
            throw new IllegalStateException("Couldn't write OutputStream to file.", e);
        }
    }

    public static String createFileName(final String reading) {
        return reading + ".mp3";
    }

    // ------------------------------------------------------------------------------------------ //
    // PRIVATE
    // ------------------------------------------------------------------------------------------ //


}
