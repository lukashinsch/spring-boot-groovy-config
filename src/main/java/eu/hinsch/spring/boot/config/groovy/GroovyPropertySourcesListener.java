package eu.hinsch.spring.boot.config.groovy;

import groovy.util.ConfigObject;
import groovy.util.ConfigSlurper;
import org.springframework.boot.context.event.ApplicationEnvironmentPreparedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;
import java.util.stream.Stream;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toMap;

/**
 * Created by lh on 12/04/15.
 */
public class GroovyPropertySourcesListener implements ApplicationListener<ApplicationEnvironmentPreparedEvent> {

    private static final String PREFIX = "spring.profiles.";
    private static final String CONFIG_FILE_PREFIX = "application";
    private final ConfigSlurper configSlurper = new ConfigSlurper();
    private ConfigurableEnvironment environment;

    @Override
    public void onApplicationEvent(ApplicationEnvironmentPreparedEvent event) {
        environment = event.getEnvironment();

        environmentProfileFiles().forEach(this::addFromFile);
        environmentProfileFiles().forEach(this::addFromClasspath);

        addFromFile(CONFIG_FILE_PREFIX + ".groovy");
        addFromClasspath(CONFIG_FILE_PREFIX + ".groovy");

    }

    private Stream<String> environmentProfileFiles() {
        return asList(environment.getActiveProfiles())
                .stream()
                .map(profile -> CONFIG_FILE_PREFIX + "-" + profile + ".groovy");
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
        environment.getPropertySources().addLast(new MapPropertySource(configName, filterProfiles(parse)));
    }

    private Map<String,Object> filterProfiles(ConfigObject parse) {
        return parse.toProperties()
                .entrySet()
                .stream()
                .filter(entry -> isActive(entry.getKey().toString()))
                .collect(toMap(entry -> stripProfilePrefix(entry.getKey().toString()), Map.Entry::getValue));
    }

    private boolean isActive(String key) {
        return isNotProfileSpecific(key) || environment.acceptsProfiles(extractProfile(key));
    }

    private boolean isNotProfileSpecific(String key) {
        return !key.startsWith(PREFIX);
    }

    private String extractProfile(String key) {
        String remainder = removePrefix(key);
        return remainder.substring(0, remainder.indexOf("."));
    }

    private String removePrefix(String key) {
        return key.substring(PREFIX.length());
    }

    private String stripProfilePrefix(String key) {
        if (isNotProfileSpecific(key)) {
            return key;
        }
        String remainder = removePrefix(key);
        return remainder.substring(remainder.indexOf(".") + 1);
    }
}
