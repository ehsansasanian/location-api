package com.drei.demo;

import com.drei.demo.repository.LocationDao;
import com.drei.demo.repository.LocationRepository;
import jakarta.annotation.Resource;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.containers.JdbcDatabaseContainer;
import org.testcontainers.containers.PostgisContainerProvider;
import org.testcontainers.lifecycle.Startables;

import java.util.Map;
import java.util.stream.Stream;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(initializers = InitHelper.DbConfigure.class)
public abstract class InitHelper {
    @Resource
    protected TestRestTemplate restTemplate;
    @Resource
    protected LocationRepository locationRepository;
    @Resource
    protected LocationDao locationDao;

    static class DbConfigure implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        static JdbcDatabaseContainer<?> postgres = new PostgisContainerProvider().newInstance();

        public static Map<String, String> getProperties() {
            Startables.deepStart(Stream.of(postgres)).join();

            return Map.of(
                    "spring.datasource.url", postgres.getJdbcUrl(),
                    "spring.datasource.username", postgres.getUsername(),
                    "spring.datasource.password",postgres.getPassword()
            );
        }

        @Override
        public void initialize(ConfigurableApplicationContext context) {
            ConfigurableEnvironment env = context.getEnvironment();
            env.getPropertySources().addFirst(new MapPropertySource(
                    "testcontainers",
                    (Map) getProperties()
            ));
        }
    }
}
