package english.scrapers;

import english.models.MerriamDefinition;
import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class MerriamScraperTest {

    private static final MerriamScraper scraper = new MerriamScraper();

    @Test
    public void get() {

        final List<MerriamDefinition> definitions = scraper.scrapeDefinitions("nice");
        assertEquals(14, definitions.size());

    }

    @Test
    public void parseDefinitions_1() throws Exception {
        // word is "nice"
        final Path path = Paths.get(ClassLoader.getSystemResource("merriam-webster-1.html").getPath());
        final List<MerriamDefinition> definitions = scraper.parseDefinitions(Files.readString(path));

        assertEquals("polite, kind", definitions.get(0).definition);
        assertEquals("a very nice person", definitions.get(0).examples.get(0));
        assertEquals("That's nice of you to say.", definitions.get(0).examples.get(1));

        assertEquals(14, definitions.size());
    }

    @Test
    public void parseDefinitions_2() throws Exception {
        // word is "to be exact"
        final Path path = Paths.get(ClassLoader.getSystemResource("merriam-webster-2.html").getPath());
        final List<MerriamDefinition> definitions = scraper.parseDefinitions(Files.readString(path));

        assertEquals("used to indicate that a statement is accurate and specific", definitions.get(0).definition);
        assertEquals("that afternoon, June 22, to be exact", definitions.get(0).examples.get(0));
        assertEquals("They had many children—seven, to be exact.", definitions.get(0).examples.get(1));
        assertEquals("He came a long way—from Nome, Alaska, to be exact—to attend the wedding.", definitions.get(0).examples.get(2));

        assertEquals(1, definitions.size());
    }

    @Test
    public void parseDefinitions_3() throws Exception {
        // word is "asdf"
        final Path path = Paths.get(ClassLoader.getSystemResource("merriam-webster-3.html").getPath());
        // todo rewrite test when you remove the exception in case no definitions are found
        assertThrows(IllegalStateException.class, () ->
                scraper.parseDefinitions(Files.readString(path)));
    }

    @Test
    public void parseDefinitions_4() throws Exception {
        // word is "to be exact"
        final Path path = Paths.get(ClassLoader.getSystemResource("merriam-webster-4.html").getPath());
        final List<MerriamDefinition> definitions = scraper.parseDefinitions(Files.readString(path));

        assertEquals(3, definitions.size());
    }

}
