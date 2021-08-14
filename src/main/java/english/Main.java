package english;

import config.Config;
import core.tts.WaveNetApiScraper;
import english.models.DictCcTranslation;
import english.models.EnglishWord;
import english.models.MerriamDefinition;
import english.scrapers.DictCcScraper;
import english.scrapers.MerriamScraper;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * This scrapes all required content (definitions, translations & mp3) for the English deck and stores them in
 * `input/english`. After that it generates the CSV for the Anki import.
 * <p>
 * The basis is the words file at `input/english/words`. The program will normalize it and iterate over it and
 * scrape all missing contents. I.e. if the mp3 for a word does already exist in `input/english/mp3`,
 * then it will be skipped. The identifier are the file names.
 * <p>
 * Words can also be phrases, but be aware that definitions and translation can come back empty.
 */
public class Main {

    private static final WaveNetApiScraper waveNetApiScraper = new WaveNetApiScraper("en-US", "en-US-Wavenet-D");
    private static final MerriamScraper merriamScraper = new MerriamScraper();
    private static final DictCcScraper dictCcScraper = new DictCcScraper();
    private static final Set<EnglishWord> corpus = normalizedUnsortedSetOfWords(Config.ENGLISH_INPUT_FOLDER.resolve("words"));
    private static final AtomicInteger count = new AtomicInteger();

    public static void main(final String[] args) throws Exception {
        for (final EnglishWord word : corpus) {
            System.out.print(count.incrementAndGet() + " | " + word.word + " | ");

            // MP3
            if (!word.pathToMp3File.toFile().exists()) {
                waveNetApiScraper.create(word.word, word.pathToMp3File);
                System.out.print("MP3 added | ");
            } else {
                System.out.print("MP3 exists | ");
            }

            // Definitions
            if (!word.pathToDefinitionsFile.toFile().exists()) {
                final List<MerriamDefinition> definitionsList = merriamScraper.createDefinitionsFile(word);
                word.definitions.addAll(definitionsList);
                System.out.print("def added | ");
            } else {
                word.definitions.addAll(merriamScraper.loadDefinitionsFromFile(word));
                System.out.print("def exists | ");
            }

            // Translations
            if (!word.pathToTranslationsFile.toFile().exists()) {
                final List<DictCcTranslation> definitionsList = dictCcScraper.createDefinitionsFile(word);
                word.translations.addAll(definitionsList);
                System.out.print("trl added");
            } else {
                word.translations.addAll(dictCcScraper.loadDefinitionsFromFile(word));
                System.out.print("trl exists");
            }
            System.out.print("\n");
        }
    }

    private static Set<EnglishWord> normalizedUnsortedSetOfWords(final Path pathToWordsFile) {
        final Set<String> normalizedWords = new HashSet<>();
        try {
            Files.readAllLines(pathToWordsFile).forEach(word -> {
                normalizedWords.add(word.trim().toLowerCase(Locale.ROOT));
            });
            final Set<EnglishWord> englishWordSet = new LinkedHashSet<>();
            normalizedWords.forEach(word -> {
                englishWordSet.add(new EnglishWord(word));
            });
            return englishWordSet;
        } catch (final IOException e) {
            throw new IllegalStateException(e);
        }
    }

    private static boolean doesMp3Exist(final Path path) {
        return path.toFile().exists();
    }

}
