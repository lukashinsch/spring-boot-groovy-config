# spring-boot-groovy-config
Configure spring boot application with groovy files.
This allows not only to specify the properties in a groovy DSL way, but also allows for (small) logic and re-use inside the configuration (note that complex logic in configuration is usually not a good idea, but some if/else cases or dynamically calculated values (e.g. port numbers) can otherwise be cumbersome to configure).

Looks for `application.groovy` and `application-<profile>.groovy` on classpath and in current dir.

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
