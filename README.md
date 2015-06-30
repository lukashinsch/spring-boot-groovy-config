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

## Still to come
* Support for profiles inside application.groovy (actually, this is where using groovy script would be most useful with regards to re-use etc.)
