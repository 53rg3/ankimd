package english.scrapers;

import english.models.DictCcTranslation;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DictCcScraperTest {

    private static final DictCcScraper scraper = new DictCcScraper();

    @Test
    public void createUrl() {
        assertEquals("https://www.dict.cc/to+be+exact", DictCcScraper.createUrl("to be exact"));
        assertEquals("https://www.dict.cc/exact", DictCcScraper.createUrl(" exact  "));
    }

    @Test
    public void scrapeTranslations() {
        List<DictCcTranslation> list = scraper.scrapeDefinitions("to be exact");
        assertEquals(50, list.size());
        list = scraper.scrapeDefinitions("belligerent");
        assertEquals(21, list.size());
    }

}
