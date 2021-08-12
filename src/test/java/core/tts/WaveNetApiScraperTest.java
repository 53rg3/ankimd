package core.tts;

import english.models.EnglishWord;
import org.junit.Ignore;
import org.junit.jupiter.api.Test;

class WaveNetApiScraperTest {

    @Test
    @Ignore("manual test, creates file")
    public void manualTest() {
        final WaveNetApiScraper waveNetApiScraper = new WaveNetApiScraper("en-US", "en-US-Wavenet-D");
        final EnglishWord englishWord = new EnglishWord("attack vector");
        waveNetApiScraper.create(englishWord.word, englishWord.pathAsString());
    }

}
