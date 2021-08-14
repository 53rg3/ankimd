package config;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ConfigTest {

    @Test
    public void paths() {
        assertEquals("/home/cc/Desktop/Programming/Repos/ankimd/input/english/mp3/test.mp3",
                Config.ENGLISH_MP3_FOLDER.resolve("test.mp3").toAbsolutePath().toString());

        System.out.println(Config.WAVENET_KEY.toAbsolutePath());
    }

}
