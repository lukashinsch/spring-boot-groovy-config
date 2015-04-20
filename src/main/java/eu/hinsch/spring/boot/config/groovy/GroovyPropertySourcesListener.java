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
import java.util.stream.Stream;

import static java.util.Arrays.asList;

/**
 * Created by lh on 12/04/15.
 */
public class GroovyPropertySourcesListener implements ApplicationListener<ApplicationEnvironmentPreparedEvent> {

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
        environment.getPropertySources().addLast(new PropertiesPropertySource(configName, parse.toProperties()));
    }
}
