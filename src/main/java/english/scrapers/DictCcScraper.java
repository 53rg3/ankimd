package english.scrapers;

import com.google.common.base.Joiner;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import core.utils.UserAgentProvider;
import english.models.DictCcTranslation;
import english.models.EnglishWord;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class DictCcScraper {

    private final OkHttpClient client = new OkHttpClient();
    private final UserAgentProvider userAgents = new UserAgentProvider();
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Type listType = new TypeToken<ArrayList<DictCcTranslation>>() {
    }.getType();

    public List<DictCcTranslation> scrapeDefinitions(final String word) {
        final String url = createUrl(word);
        final Request request = new Request.Builder()
                .get()
                .url(url)
                .header("User-Agent", this.userAgents.getNext())
                .build();
        try (final Response response = this.client.newCall(request).execute()) {
            final ResponseBody body = response.body();
            if (!response.isSuccessful()) {
                throw new IllegalStateException("HTTP request failed (status: " + response.code() + "), URL:" + url);
            }
            if (body != null) {
                return this.parseTranslations(body.string());
            }
            throw new IllegalStateException("Body was empty, URL: " + url);
        } catch (final IOException e) {
            throw new IllegalStateException("Failed to get response for: " + url);
        }
    }

    List<DictCcTranslation> parseTranslations(final String html) {
        final Document doc = Jsoup.parse(html);
        final List<DictCcTranslation> resultList = new ArrayList<>();

        doc.select("[id~=tr\\d]").forEach(translationRow -> {
            final Elements translationCells = translationRow.select(".td7nl");
            if (translationCells.size() != 2) {
                throw new IllegalStateException("Expected to find 2 cells with translation, got " + translationCells.size());
            }

            final AtomicInteger index = new AtomicInteger(0);
            final DictCcTranslation translation = new DictCcTranslation();
            final Elements row = translationRow.select(".td7nl");
            row.forEach(translationCell -> {
                final List<String> metaData = new ArrayList<>();
                translationCell.select("dfn").forEach(dfn -> {
                    metaData.add(dfn.text());
                    dfn.remove();
                });
                translationCell.select("kbd").forEach(kbd -> {
                    metaData.add(kbd.text().replaceAll("[^\\w]", ""));
                    kbd.remove();
                });
                translationCell.select("var").forEach(var -> {
                    metaData.add(var.text().replaceAll("[^\\w]", ""));
                    var.remove();
                });
                translationCell.select("div").forEach(div -> {
                    metaData.add(div.text());
                    div.remove();
                });

                String result = translationCell.text();
                if (!metaData.isEmpty()) {
                    result += " (" + Joiner.on(", ").join(metaData) + ")";
                }
                switch (index.getAndIncrement()) {
                    case 0:
                        translation.english = result;
                        break;
                    case 1:
                        translation.german = result;
                        break;
                    default:
                        throw new IllegalStateException("Expected only 2 cells");
                }
            });
            resultList.add(translation);
        });

        return resultList;
    }

    public static String createUrl(final String word) {
        return "https://www.dict.cc/" + word.trim().replaceAll("\\s", "+");
    }

    public List<DictCcTranslation> loadDefinitionsFromFile(final EnglishWord englishWord) {
        try {
            return GSON.fromJson(Files.readString(englishWord.pathToTranslationsFile), listType);
        } catch (final IOException e) {
            throw new IllegalStateException(e);
        }
    }

    public List<DictCcTranslation> createDefinitionsFile(final EnglishWord englishWord) {
        try {
            final List<DictCcTranslation> translationList = this.scrapeDefinitions(englishWord.word);
            Files.write(englishWord.pathToTranslationsFile,
                    GSON.toJson(translationList).getBytes(),
                    StandardOpenOption.CREATE);
            return translationList;
        } catch (final IOException e) {
            throw new IllegalStateException(e);
        }
    }

}
