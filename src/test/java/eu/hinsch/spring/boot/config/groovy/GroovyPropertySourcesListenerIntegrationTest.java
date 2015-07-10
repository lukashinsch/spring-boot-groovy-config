package eu.hinsch.spring.boot.config.groovy;


import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.core.env.Environment;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

@RunWith(Enclosed.class)
public class GroovyPropertySourcesListenerIntegrationTest {

    public static class WithoutProfile extends TestConfig {

        @Test
        public void shouldNotLoadProfileProperties() {
            assertThat(environment.getProperty("info.conflict"), is("main"));
            assertThat(environment.getProperty("info.key"), is(nullValue()));
        }
    }

    @ActiveProfiles("test")
    public static class WithTestProfile extends TestConfig {

        @Test
        public void shouldLoadProfileProperties() {
            assertThat(environment.getProperty("info.conflict"), is("test"));
            assertThat(environment.getProperty("info.key"), is("test-value"));
        }
    }

    @RunWith(SpringJUnit4ClassRunner.class)
    @SpringApplicationConfiguration(classes = SpringBootGroovyConfigApplication.class)
    public abstract static class TestConfig {
        @Autowired
        protected Environment environment;

        @Test
        public void shouldLoadNonProfileProperties() {
            assertThat(environment.getProperty("info.mainFile"), is("some value"));
        }
    }
}
