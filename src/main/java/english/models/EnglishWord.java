package english.models;

import config.Config;

import java.nio.file.Path;
import java.util.Locale;

public class EnglishWord {

    public final String word;
    public final Path pathToMp3;

    public EnglishWord(final String word) {
        this.word = word.trim();
        String fileName = word.toLowerCase(Locale.ROOT)
                .replaceAll("[^\\w]", "_")
                .replaceAll("__+", "_");
        fileName = fileName.replaceAll("^_|_$", "");
        this.pathToMp3 = Config.ENGLISH_MP3_FOLDER.resolve(fileName + ".mp3");
    }

    public String pathAsString() {
        return pathToMp3.toAbsolutePath().toString();
    }
}
