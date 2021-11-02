# Gatling 
### perfromance testing

Gatling quick start: https://theperformanceengineer.com/2016/11/29/load-testing-using-gatling-gatling-simulation-from-scratch/

run:
```bash
mvn gatling:test -Dgatling.simulationClass=ru.avbelyaev.SimulationCreateUsers > user-ids.csv
```

don't forget maven plugin in pom.xml:
```xml

    <build>
        <plugins>
            <plugin>
                <groupId>net.alchim31.maven</groupId>
                <artifactId>scala-maven-plugin</artifactId>
                <version>3.4.4</version>
            </plugin>
            <plugin>
                <groupId>io.gatling</groupId>
                <artifactId>gatling-maven-plugin</artifactId>
                <version>3.1.2</version>
                <configuration>
                    <simulationsFolder>src/test/scala</simulationsFolder>
                    <resultsFolder>results/gatling</resultsFolder>
                </configuration>
            </plugin>
        </plugins>
    </build>
```
