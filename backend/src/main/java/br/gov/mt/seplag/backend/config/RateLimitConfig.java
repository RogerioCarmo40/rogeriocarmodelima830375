package br.gov.mt.seplag.backend.config;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class RateLimitConfig {

    @Value("${rate.limit.capacity}")
    private int capacity;

    @Value("${rate.limit.refill-duration-minutes}")
    private int refillDurationMinutes;

    private final Map<String, Bucket> buckets = new ConcurrentHashMap<>();

    // 🔥 RENOMEAR ESTE MÉTODO
    public Bucket getBucket(String key) {
        return buckets.computeIfAbsent(key, k -> newBucket());
    }

    private Bucket newBucket() {

        Bandwidth limit = Bandwidth.builder()
            .capacity(capacity)
            .refillIntervally(capacity, Duration.ofMinutes(refillDurationMinutes))
            .build();

        return Bucket.builder()
            .addLimit(limit)
            .build();
    }
}
