package bw.co.roguesystems.tau.config;

import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Configuration;

import jakarta.annotation.PostConstruct;

@Configuration
public class CacheInitializer {

    private final String cacheNames[] = {
        "accessPointType",
        "accessPoint",
        "authorisation",
        "users",
        "application"
    };    
    
    private final CacheManagement cacheManagement;

    private final CacheManager cacheManager;

    public CacheInitializer(CacheManagement cacheManagement, CacheManager cacheManager) {
        this.cacheManagement = cacheManagement;
        this.cacheManager = cacheManager;
    }

    @PostConstruct
    public void clearCacheOnStartup() {

        if(cacheManager == null) {
            return;
        }

        for(String cacheName : cacheNames) {
            cacheManagement.evict(cacheName);
        }
    }
}
