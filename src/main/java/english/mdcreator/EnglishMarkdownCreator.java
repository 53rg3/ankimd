package english.mdcreator;

import config.Config;
import english.models.EnglishWord;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.Set;

public class EnglishMarkdownCreator {

    public void create(final Set<EnglishWord> corpus) {
        for (final EnglishWord word : corpus) {
            final StringBuilder sb = new StringBuilder();

            // Front matter
            sb.append("---\n");
            sb.append("sound: [sound:ankimd/english/mp3/").append(word.pathToMp3File.getFileName()).append("]\n");
            sb.append("---\n\n");

            // Heading
            sb.append("\\### ").append(word.word).append("\n\n");

            // Definitions
            sb.append("[").append("Merriam-Webster").append("](").append(word.merriamWebsterUrl).append(")\n\n");

            if (word.definitions.isEmpty()) {
                sb.append("No definitions found\n\n");
            } else {
                word.definitions.forEach(definition -> {
                    sb.append("- ").append(definition.definition).append("\n");
                    definition.examples.forEach(example ->
                            sb.append("    - ").append(example).append("\n"));
                });
                sb.append("\n");
            }

            // Translations
            sb.append("[").append("dict.cc").append("](").append(word.dictCcUrl).append(")\n\n");

            if (word.translations.isEmpty()) {
                sb.append("No translations found\n\n");
            } else {
                sb.append("| English        | German       |\n");
                sb.append("| -------------- | ------------ |\n");
                word.translations.forEach(trs -> {
                    sb.append("| ").append(trs.english).append(" | ").append(trs.german).append(" |\n");
                });
            }


            try {
                Files.write(Config.ENGLISH_MARKDOWN_FOLDER.resolve(word.word + ".md"),
                        sb.toString().getBytes(StandardCharsets.UTF_8),
                        StandardOpenOption.CREATE);
            } catch (final IOException e) {
                throw new IllegalStateException(e);
            }
        }
    }

}
