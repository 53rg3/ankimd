package english.scrapers;

import com.google.common.base.Joiner;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import core.utils.UserAgentProvider;
import english.models.EnglishWord;
import english.models.MerriamDefinition;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MerriamScraper {

    private final OkHttpClient client = new OkHttpClient();
    private final UserAgentProvider userAgents = new UserAgentProvider();
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Type listType = new TypeToken<ArrayList<MerriamDefinition>>() {
    }.getType();

    public List<MerriamDefinition> scrapeDefinitions(final String word) {
        final String url = createUrl(word);
        final Request request = new Request.Builder()
                .get()
                .url(url)
                .header("User-Agent", this.userAgents.getNext())
                .build();
        try (final Response response = this.client.newCall(request).execute()) {
            final ResponseBody body = response.body();
            if (response.code() == 404) {
                return Collections.emptyList();
            }
            if (!response.isSuccessful()) {
                throw new IllegalStateException("HTTP request failed (status: " + response.code() + "), URL: " + url);
            }
            if (body != null) {
                return this.parseDefinitions(body.string());
            }
            throw new IllegalStateException("Body was empty, URL: " + url);
        } catch (final IOException e) {
            throw new IllegalStateException("Failed to get response for: " + url);
        }
    }

    List<MerriamDefinition> parseDefinitions(final String html) {
        final Document doc = Jsoup.parse(html);
        final List<MerriamDefinition> resultList = new ArrayList<>();

        doc.select("[id~=dictionary-entry-\\d]").forEach(definitionWrapper ->
                definitionWrapper.select("[class~=sb-\\d]").forEach(definitionAndExamples -> {
                    final MerriamDefinition result = new MerriamDefinition();

                    // Definitions
                    final List<String> defs = new ArrayList<>();
                    definitionAndExamples.select(".dtText").forEach(definition ->
                            defs.add(definition.text().replaceFirst(": ", "").trim()));

                    if (defs.isEmpty()) {
                        definitionAndExamples.select(".unText").forEach(definition ->
                                result.definition = definition.text().replaceFirst(": ", "").trim());
                    } else {
                        result.definition = Joiner.on(", ").join(defs);
                    }

                    // Examples
                    definitionAndExamples.select(".ex-sent").forEach(example -> {
                        result.examples.add(example.text().trim());
                    });

                    if (result.isValid()) {
                        resultList.add(result);
                    }
                }));

        return resultList;
    }

    public static String createUrl(final String word) {
        return "https://www.merriam-webster.com/dictionary/" + URLEncoder.encode(word, Charset.defaultCharset());
    }

    public List<MerriamDefinition> loadDefinitionsFromFile(final EnglishWord englishWord) {
        try {
            return GSON.fromJson(Files.readString(englishWord.pathToDefinitionsFile), listType);
        } catch (final IOException e) {
            throw new IllegalStateException(e);
        }
    }

    public List<MerriamDefinition> createDefinitionsFile(final EnglishWord englishWord) {
        try {
            final List<MerriamDefinition> merriamDefinitionList = this.scrapeDefinitions(englishWord.word);
            Files.write(englishWord.pathToDefinitionsFile,
                    GSON.toJson(merriamDefinitionList).getBytes(),
                    StandardOpenOption.CREATE);
            return merriamDefinitionList;
        } catch (final IOException e) {
            throw new IllegalStateException(e);
        }
    }

}
