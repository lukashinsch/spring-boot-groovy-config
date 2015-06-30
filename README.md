# spring-boot-groovy-config
Configure spring boot application with groovy files

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