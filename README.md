[![Coverage Status](https://coveralls.io/repos/lukashinsch/spring-boot-groovy-config/badge.svg?branch=master)](https://coveralls.io/r/lukashinsch/spring-boot-groovy-config?branch=master)
[![Build Status](https://travis-ci.org/lukashinsch/spring-boot-groovy-config.svg?branch=master)](https://travis-ci.org/lukashinsch/spring-boot-groovy-config)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/eu.hinsch/spring-boot-groovy-config/badge.svg)](https://maven-badges.herokuapp.com/maven-central/eu.hinsch/spring-boot-groovy-config/)


# spring-boot-groovy-config
Configure spring boot application with groovy files.
This allows not only to specify the properties in a groovy DSL way, but also allows for (small) logic and re-use inside the configuration (note that complex logic in configuration is usually not a good idea, but some if/else cases or dynamically calculated values (e.g. port numbers) can otherwise be cumbersome to configure).

Looks for `application.groovy` and `application-<profile>.groovy` on classpath and in current dir.

## Howto use

Maven
```xml
<dependency>
    <groupId>eu.hinsch</groupId>
    <artifactId>spring-boot-groovy-config</artifactId>
    <version>0.1.0</version>
</dependency>
```

Gradle
```groovy
compile 'eu.hinsch:spring-boot-groovy-config:0.1.0'
```

## Example 
```
spring {
  datasource {
    username = 'myuser'
    password = 'maypass'
  }
}
```

Profile specific sections in application.groovy:

```
spring {
  profiles {
    dev {
      displayname = 'development'
    }
    prod {
      displayname = 'production'
    }
  }
}     
```
When the application is started with spring.profiles.active=dev displayname will be set to 'development', ...
