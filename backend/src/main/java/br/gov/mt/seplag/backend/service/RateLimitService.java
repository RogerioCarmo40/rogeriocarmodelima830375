package br.gov.mt.seplag.backend.service;

import br.gov.mt.seplag.backend.config.RateLimitConfig;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.ConsumptionProbe;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RateLimitService {
    
    private final RateLimitConfig rateLimitConfig;
    
    public boolean isAllowed(HttpServletRequest request) {
        String clientId = getClientId(request);
        Bucket bucket = rateLimitConfig.getBucket(clientId);
        
        ConsumptionProbe probe = bucket.tryConsumeAndReturnRemaining(1);
        return probe.isConsumed();
    }
    
    public long getRemainingTokens(HttpServletRequest request) {
        String clientId = getClientId(request);
        Bucket bucket = rateLimitConfig.getBucket(clientId);
        
        ConsumptionProbe probe = bucket.tryConsumeAndReturnRemaining(0);
        return probe.getRemainingTokens();
    }
    
    private String getClientId(HttpServletRequest request) {
        String username = request.getUserPrincipal() != null ? 
                         request.getUserPrincipal().getName() : "anonymous";
        String ip = request.getRemoteAddr();
        return username + ":" + ip;
    }
}
