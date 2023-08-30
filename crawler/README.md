# Crawler

Kotlin coroutine-based web crawler scoped to domain.

Limitations
- web client doesnt have retries, connection timeouts , doesnt throttle
- queue doesnt persist
- doesnt handle exceptions granularly
- might be more efficient :) 
- Coroutines API turned out to be still in experimental state


## Build
```
./gradlew clean jar
```

## Run
```bash
# help
java -jar build/libs/crawler-1.0.jar --help

# run with default params
java -jar build/libs/crawler-1.0.jar

# run with custom params
java -jar build/libs/crawler-1.0.jar --url http://my.site.com --domain my.site --workers 5
```

## E2E test
There is an E2E test that crawls a website with the following structure
```
http://localhost:9997/index.html
. http://localhost:9997/foo
. . http://localhost:9997/baz
. . http://localhost:9997/foo-2
. http://localhost:9997/bar
. . http://localhost:9997/baz
. http://localhost:9997/about
```

And with cyclic links
```
http://localhost:9997/cycle/cycle-a.html       <----
. http://localhost:9997/cycle/cycle-b.html          |
. . http://localhost:9997/cycle/cycle-c.html        |
. . . http://localhost:9997/cycle/cycle-a.html  ---- 
```
