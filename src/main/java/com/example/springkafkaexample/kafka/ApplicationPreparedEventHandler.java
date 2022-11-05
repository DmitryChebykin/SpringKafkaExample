package com.example.springkafkaexample.kafka;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationPreparedEvent;
import org.springframework.boot.env.OriginTrackedMapPropertySource;
import org.springframework.boot.origin.Origin;
import org.springframework.boot.origin.OriginTrackedValue;
import org.springframework.context.ApplicationListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.ClassPathResource;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class ApplicationPreparedEventHandler implements ApplicationListener<ApplicationPreparedEvent> {

    @Override
    public void onApplicationEvent(ApplicationPreparedEvent event) {
        ConfigurableApplicationContext applicationContext = event.getApplicationContext();
        ConfigurableEnvironment environment = applicationContext.getEnvironment();
        MutablePropertySources propertySources = environment.getPropertySources();
        MutablePropertySources filteredPropertySources = applicationContext.getEnvironment().getPropertySources();
        PropertySource<?> propertySource = filteredPropertySources.stream().filter(OriginTrackedMapPropertySource.class::isInstance).findFirst().get();
        String name = propertySource.getName();

        propertySources.remove(name);

        Map<String, OriginTrackedValue> stringMap = (Map<String, OriginTrackedValue>) propertySource.getSource();

        Map<String, OriginTrackedValue> map = new HashMap<>();

        stringMap.entrySet().stream().forEach(e -> map.put(e.getKey(), e.getValue()));

        List<String> propsList = Arrays.asList("spring.kafka.ssl.key-store-location", "spring.kafka.ssl.trust-store-location");
        propsList.stream().forEach(e -> {
            OriginTrackedValue originTrackedValue = stringMap.get(e);
            Object value = originTrackedValue.getValue();

            Origin origin = originTrackedValue.getOrigin();
            String value1 = (String) value;
            ClassPathResource classPathResource = new ClassPathResource(value1);
            String absolutePath1 = null;
            try {
                absolutePath1 = classPathResource.getFile().getAbsoluteFile().getAbsolutePath();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            URL resource = this.getClass().getResource(value1);
            File file = null;
            try {
                file = Paths.get(resource.toURI()).toFile();
            } catch (URISyntaxException ex) {
                log.info("ApplicationPreparedEventHandler exception {}", (Object[]) ex.getStackTrace());
            }

            map.replace(e, OriginTrackedValue.of(absolutePath1, origin));
        });

        propertySources.addFirst(new OriginTrackedMapPropertySource(name, map));
        log.info("");
    }
}
