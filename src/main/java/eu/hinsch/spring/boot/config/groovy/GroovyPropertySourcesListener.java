package eu.hinsch.spring.boot.config.groovy;

import groovy.util.ConfigObject;
import groovy.util.ConfigSlurper;
import org.springframework.boot.context.event.ApplicationEnvironmentPreparedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.PropertiesPropertySource;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Stream;

import static java.util.Arrays.asList;

/**
 * Created by lh on 12/04/15.
 */
public class GroovyPropertySourcesListener implements ApplicationListener<ApplicationEnvironmentPreparedEvent> {

    private static final String PREFIX = "spring.profiles.";
    private final ConfigSlurper configSlurper = new ConfigSlurper();
    private ConfigurableEnvironment environment;

    @Override
    public void onApplicationEvent(ApplicationEnvironmentPreparedEvent event) {
        environment = event.getEnvironment();

        environmentProfileFiles().forEach(this::addFromFile);
        environmentProfileFiles().forEach(this::addFromClasspath);

        addFromFile("application.groovy");
        addFromClasspath("application.groovy");

    }

    private Stream<String> environmentProfileFiles() {
        return asList(environment.getActiveProfiles())
                .stream()
                .map(profile -> "application-" + profile + ".groovy");
    }

    private void addFromFile(String filename) {
        File file = new File(filename);
        if (file.exists()) {
            try {
                URL url = file.toURI().toURL();
                addFromUrl(url.toString(), url);
            } catch (MalformedURLException e) {
                throw new RuntimeException("url error", e);
            }
        }
    }

    private void addFromClasspath(String filename) {
        URL url = this.getClass().getClassLoader().getResource(filename);
        if (url != null) {
            addFromUrl("applicationConfig [classpath:/" + filename + "]", url);
        }
    }

    private void addFromUrl(String configName, URL url) {
        ConfigObject parse = configSlurper.parse(url);
        environment.getPropertySources().addLast(new PropertiesPropertySource(configName, filterProfiles(parse)));
    }

    private Properties filterProfiles(ConfigObject parse) {
        Properties properties = new Properties();
        for (Map.Entry<Object,Object> entry : parse.toProperties().entrySet()) {
            if (isActive(entry.getKey().toString())) {
                properties.put(stripProfilePrefix(entry.getKey().toString()), entry.getValue());
            }
        }
        return properties;
    }

    private boolean isActive(String key) {
        if (isNotProfileSpecific(key)) {
            return true;
        }
        String remainder = key.substring(PREFIX.length());
        String profile = remainder.substring(0, remainder.indexOf("."));
        return environment.acceptsProfiles(profile);
    }

    private boolean isNotProfileSpecific(String key) {
        return !key.startsWith(PREFIX);
    }

    private String stripProfilePrefix(String key) {
        if (isNotProfileSpecific(key)) {
            return key;
        }
        String remainder = key.substring(PREFIX.length());
        return remainder.substring(remainder.indexOf(".") + 1);
    }
}
