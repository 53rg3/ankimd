package config;

import java.nio.file.Path;
import java.nio.file.Paths;

public class Config {
    private Config() {
    }

    public static final Path KEY_FOLDER = Paths.get("keys/");
    public static final Path WAVENET_KEY = KEY_FOLDER.resolve("wavenet.json");

    public static final Path OUTPUT_FOLDER = Paths.get("output/");
    public static final Path INPUT_FOLDER = Paths.get("input/");

    // ------------------------------------------------------------------------------------------ //
    // ENGLISH
    // ------------------------------------------------------------------------------------------ //
    public static final Path ENGLISH_INPUT_FOLDER = INPUT_FOLDER.resolve("english");
    public static final Path ENGLISH_MP3_FOLDER = ENGLISH_INPUT_FOLDER.resolve("mp3");
    public static final Path ENGLISH_DEFINITIONS_FOLDER = ENGLISH_INPUT_FOLDER.resolve("definitions");
    public static final Path ENGLISH_TRANSLATION_FOLDER = ENGLISH_INPUT_FOLDER.resolve("translations");
    public static final Path ENGLISH_MARKDOWN_FOLDER = ENGLISH_INPUT_FOLDER.resolve("markdown");

}
