package core.utils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class UserAgentProvider {

    private static final Path userAgentFile = Paths.get(ClassLoader.getSystemResource("user_agents").getPath());
    private final List<String> userAgents = loadUserAgentsAsList();
    private final AtomicInteger currentIndex = new AtomicInteger(0);

    public String getNext() {
        if (this.currentIndex.get() >= this.userAgents.size()) {
            this.currentIndex.set(0);
        }
        return this.userAgents.get(this.currentIndex.getAndIncrement());
    }

    public int size() {
        return this.userAgents.size();
    }

    private static List<String> loadUserAgentsAsList() {
        try {
            return Files.readAllLines(userAgentFile, StandardCharsets.UTF_8);
        } catch (final IOException e) {
            throw new IllegalStateException("Failed to load " + userAgentFile.toAbsolutePath());
        }
    }

}
