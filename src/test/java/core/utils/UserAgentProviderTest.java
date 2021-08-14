package core.utils;

import org.junit.jupiter.api.Test;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.IntStream;

class UserAgentProviderTest {

    @Test
    public void getNext() {
        final UserAgentProvider userAgentProvider = new UserAgentProvider();
        final Set<String> uniqueUAs = new LinkedHashSet<>();
        IntStream.range(0, userAgentProvider.size()).forEach(count -> {
            final String userAgent = userAgentProvider.getNext();
            if (!uniqueUAs.add(userAgent)) {
                throw new IllegalStateException("Got duplicate user-agent: " + userAgent);
            }
        });
    }

}
