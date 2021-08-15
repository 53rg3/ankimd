#!/bin/bash

# This scrape MP3 from Google WaveNet, definitions from Merriam Webster, translations from dict.cc
# Folders in `input/english` which must exist:
# - `mp3/`
# - `definitions/`
# - `translations/`
# - `markdown/`
# otherwise you'll get exceptions.
# Key in `keys/wavenet.json` must exist.
# See `src/main/java/english/Main.java` for more info.

mvn clean
mvn -e exec:java -Dexec.mainClass="english.Main" -Dexec.args=""
