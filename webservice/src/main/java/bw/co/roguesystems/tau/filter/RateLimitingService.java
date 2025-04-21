package bw.co.roguesystems.tau.filter;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;

@Component
public class RateLimitingService {

    @Value("${rate-limiting.tokens}")
    private int tokens;

    private final Map<String, Bucket> buckets = new ConcurrentHashMap<>();

    public Bucket resolveBucket(String key) {
        return buckets.computeIfAbsent(key, this::createBucket);
    }

    private Bucket createBucket(String key) {
        // Define a limit of 10 requests per minute
        Bandwidth limit = Bandwidth.builder().capacity(tokens).refillIntervally(tokens, Duration.ofMinutes(1)).build();

        return Bucket.builder().addLimit(limit).build();
    }
}
