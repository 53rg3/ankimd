package core.csvcreator;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.QuoteMode;
import org.apache.commons.io.FilenameUtils;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * This creates the output CSV from a given folder with Markdown files.
 * Format of the output CSV, columns:
 * 1. ID (name of the file) // todo maybe take also subfolders into the ID?
 * 2. Notes (empty column for manual edits in Anki)
 * 3. Question (for the frontpage) // todo parse question from front matter
 * 4. Markdown (complete file content, including front matter)
 */
public class Main {

    private static final String outputPath = createOutputPath();
    private static final FileWriter fileWriter = createFileWriter();
    private static final CSVPrinter csvPrinter = createCsvPrinter();
    private static final AtomicInteger count = new AtomicInteger();

    public static void main(final String[] args) {
        validateArgs(args);

        final Path path = Paths.get(args[0]);
        try {
            Files.walk(path)
                    .filter(file -> file.toFile().isFile())
                    .filter(file -> !file.toFile().isDirectory())
                    .filter(file -> file.toString().endsWith(".md"))
                    .forEach(file -> {
                        count.incrementAndGet();
                        try {
                            final String markdown = Files.readString(file);
                            final List<String> row = new ArrayList<>();
                            row.add(FilenameUtils.getBaseName(file.getFileName().toString()));
                            row.add("");
                            row.add("");
                            row.add(markdown.replaceAll("\n", "<br>"));
                            csvPrinter.printRecord(row);
                        } catch (final IOException e) {
                            throw new IllegalStateException(e);
                        }
                    });
        } catch (final IOException e) {
            throw new IllegalStateException(e);
        }

        try {
            fileWriter.close();
            csvPrinter.close();
            System.out.println("Rows created: " + count.get());
            System.out.println("Created: " + outputPath);
        } catch (final IOException e) {
            throw new IllegalStateException(e);
        }
    }

    private static void validateArgs(final String[] args) {
        if (args.length != 1) {
            System.err.println("Expecting 1 argument which is a path to a folder with Markdown files");
            System.exit(1);
        }
        final File path = Paths.get(args[0]).toFile();
        if (!path.exists()) {
            System.err.println("Given path does not exist: " + path);
            System.exit(1);
        }
        if (!path.isDirectory()) {
            System.err.println("Given path is not a directory " + path);
            System.exit(1);
        }
    }

    @NotNull
    private static FileWriter createFileWriter() {
        try {
            return new FileWriter(outputPath);
        } catch (final IOException e) {
            throw new IllegalStateException(e);
        }
    }

    @NotNull
    private static String createOutputPath() {
        return "output/" + Instant.now().toString().replaceAll("\\..*", ".csv");
    }

    @NotNull
    private static CSVPrinter createCsvPrinter() {
        final CSVFormat csvFormat = CSVFormat.Builder.create()
                .setDelimiter("\t")
                .setEscape(Character.valueOf('"'))
                .setQuote(Character.valueOf('"'))
                .setQuoteMode(QuoteMode.MINIMAL)
                .setSkipHeaderRecord(false)
                .setCommentMarker(null)
                .build();
        try {
            return new CSVPrinter(Main.fileWriter, csvFormat);
        } catch (final IOException e) {
            throw new IllegalStateException(e);
        }
    }

}
