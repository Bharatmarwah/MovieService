package in.bm.MovieService.CONFIG;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableCaching
public class RedisConfig {


    @Bean
    public RedisCacheManager cacheManager(
            RedisConnectionFactory connectionFactory) {

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());


        RedisCacheConfiguration config =
                RedisCacheConfiguration.defaultCacheConfig()
                        .entryTtl(Duration.ofMinutes(5))
                        .disableCachingNullValues()
                        .serializeKeysWith(
                                RedisSerializationContext.SerializationPair
                                        .fromSerializer(new StringRedisSerializer()))
                        .serializeValuesWith(
                                RedisSerializationContext.SerializationPair
                                        .fromSerializer(new GenericJackson2JsonRedisSerializer(mapper)));

        Map<String, RedisCacheConfiguration> cacheConfig = new HashMap<>();

        cacheConfig.put("movies", config.entryTtl(Duration.ofHours(24)));
        cacheConfig.put("movieDetails", config.entryTtl(Duration.ofHours(48)));

        cacheConfig.put("shows", config.entryTtl(Duration.ofHours(1)));

        cacheConfig.put("theaters", config.entryTtl(Duration.ofHours(12)));
        cacheConfig.put("theaterDetails", config.entryTtl(Duration.ofDays(3)));

        cacheConfig.put("seatCategories", config.entryTtl(Duration.ofDays(1)));

        cacheConfig.put("seats", config.entryTtl(Duration.ofDays(1)));

        return RedisCacheManager.builder(connectionFactory)
                .withInitialCacheConfigurations(cacheConfig)
                .cacheDefaults(config)
                .build();
    }
}
