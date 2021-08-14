package english.models;

import config.Config;
import english.scrapers.DictCcScraper;
import english.scrapers.MerriamScraper;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class EnglishWord {

    public final String word;
    public final String merriamWebsterUrl;
    public final String dictCcUrl;
    public final Path pathToMp3File;
    public final Path pathToDefinitionsFile;
    public final Path pathToTranslationsFile;
    public final List<MerriamDefinition> definitions = new ArrayList<>();
    public final List<DictCcTranslation> translations = new ArrayList<>();

    public EnglishWord(final String word) {
        this.word = word.trim();
        this.merriamWebsterUrl = MerriamScraper.createUrl(this.word);
        this.dictCcUrl = DictCcScraper.createUrl(this.word);

        String fileName = word.toLowerCase(Locale.ROOT)
                .replaceAll("[^\\w]", "_")
                .replaceAll("__+", "_");
        fileName = fileName.replaceAll("^_|_$", "");

        this.pathToMp3File = Config.ENGLISH_MP3_FOLDER.resolve(fileName + ".mp3");
        this.pathToDefinitionsFile = Config.ENGLISH_DEFINITIONS_FOLDER.resolve(fileName + ".json");
        this.pathToTranslationsFile = Config.ENGLISH_TRANSLATION_FOLDER.resolve(fileName + ".json");
    }
}
