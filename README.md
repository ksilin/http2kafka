# http2kafka

This project uses Quarkus, the Supersonic Subatomic Java Framework.

If you want to learn more about Quarkus, please visit its website: https://quarkus.io/ .

## Running the application in dev mode

You can run your application in dev mode that enables live coding using:
```shell script
./gradlew quarkusDev
```

> **_NOTE:_**  Quarkus now ships with a Dev UI, which is available in dev mode only at http://localhost:8080/q/dev/.

## health REST endpoints

The application provides startup/liveness and readiness health checks. 

On startup, availability of the target topic is required. 

Liveness - broker is accessible and no errors were thrown during production

Readiness - broker is accessible

##### startup

```json
❯ https :8443/q/health/started -A basic -a alice:alice --verify=no
HTTP/1.1 200 OK
content-length: 233
content-type: application/json; charset=UTF-8

{
    "checks": [
        {
            "data": {
                "events-out": "[OK]"
            },
            "name": "SmallRye Reactive Messaging - startup check",
            "status": "UP"
        }
    ],
    "status": "UP"
}
```

##### liveness

```json
❯ https :8443/q/health/live -A basic -a alice:alice --verify=no
HTTP/1.1 200 OK
content-length: 234
content-type: application/json; charset=UTF-8

{
    "checks": [
        {
            "data": {
                "events-out": "[OK]"
            },
            "name": "SmallRye Reactive Messaging - liveness check",
            "status": "UP"
        }
    ],
    "status": "UP"
}
```



##### readiness

```json
❯ https :8443/q/health/ready -A basic -a alice:alice --verify=no
HTTP/1.1 200 OK
content-length: 235
content-type: application/json; charset=UTF-8

{
    "checks": [
        {
            "data": {
                "events-out": "[OK]"
            },
            "name": "SmallRye Reactive Messaging - readiness check",
            "status": "UP"
        }
    ],
    "status": "UP"
}
```



## Packaging and running the application

The application can be packaged using:
```shell script
./gradlew build
```
It produces the `quarkus-run.jar` file in the `build/quarkus-app/` directory.
Be aware that it’s not an _über-jar_ as the dependencies are copied into the `build/quarkus-app/lib/` directory.

The application is now runnable using `java -jar build/quarkus-app/quarkus-run.jar`.

If you want to build an _über-jar_, execute the following command:
```shell script
./gradlew build -Dquarkus.package.type=uber-jar
```

The application, packaged as an _über-jar_, is now runnable using `java -jar build/*-runner.jar`.

## Creating a native executable

You can create a native executable using: 
```shell script
./gradlew build -Dquarkus.package.type=native
```

Or, if you don't have GraalVM installed, you can run the native executable build in a container using: 
```shell script
./gradlew build -Dquarkus.package.type=native -Dquarkus.native.container-build=true
```

You can then execute your native executable with: `./build/http2kafka-1.0.0-SNAPSHOT-runner`

If you want to learn more about building native executables, please consult https://quarkus.io/guides/gradle-tooling.

## Related Guides

- SmallRye Reactive Messaging - Kafka Connector ([guide](https://quarkus.io/guides/kafka-reactive-getting-started)): Connect to Kafka with Reactive Messaging
- RESTEasy Classic ([guide](https://quarkus.io/guides/resteasy)): REST endpoint framework implementing Jakarta REST and more
- Micrometer metrics ([guide](https://quarkus.io/guides/micrometer)): Instrument the runtime and your application with dimensional metrics using Micrometer.
- Micrometer Registry Prometheus ([guide](https://quarkus.io/guides/micrometer)): Enable Prometheus support for Micrometer

## Provided Code

### Reactive Messaging codestart

Use SmallRye Reactive Messaging

[Related Apache Kafka guide section...](https://quarkus.io/guides/kafka-reactive-getting-started)


### RESTEasy JAX-RS

Easily start your RESTful Web Services

[Related guide section...](https://quarkus.io/guides/getting-started#the-jax-rs-resources)
