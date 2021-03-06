# Image Detection application

This is a minimal CRUD service exposing a few endpoints over REST.

Under the hood, this demo uses:

- RESTEasy to expose the REST endpoints
- REST-assured and JUnit 5 for endpoint testing

## Requirements

To compile and run this demo you will need:

- JDK 11+
- Maven
- Postgres

### Configuring Maven and JDK 11+

Make sure that both the `M2_HOME` and `JAVA_HOME` environment variables have
been set, and that a JDK 11+ `java` command is on the path.

### Postgres Database

Install postgres and create databases imagedb and testdb.

## Building the application

Launch the Maven build on the checked out sources of this demo:

```shell script
 ./mvnw package
```
### Live coding with Quarkus

The Maven Quarkus plugin provides a development mode that supports
live coding. To try this out:

```shell script
 ./mvnw quarkus:dev
```

This command will leave Quarkus running in the foreground listening on port 8080.


### Run Quarkus in JVM mode

When you're done iterating in developer mode, you can run the application as a
conventional jar file.

First compile it:

```shell script
 ./mvnw package
```

Then run it:

```shell script
 java -jar ./target/quarkus-app/quarkus-run.jar
```

Have a look at how fast it boots, or measure the total native memory consumption.

### Run the application as container services

Docker needs to be installed in the environment. For windows environment Docker desktop can be installed.

Compile using the following command:

```shell script
./mvnw clean package -Dquarkus.container-image.build=true
```

Build the image:

```shell script
docker build -f src/main/docker/Dockerfile.jvm -t quarkus/image-detection-jvm .
```

Bring up the services:

```shell script
docker-compose up -d
```

Bring down the services:

```shell script
docker-compose down
```

### Run Quarkus as a native executable

For running as a native executable a GrallVM needs to be setup and configured.

See the [Building a Native Executable guide](https://quarkus.io/guides/building-native-image-guide)
for help setting up your environment.

You can also create a native executable from this application without making any
source code changes. A native executable removes the dependency on the JVM:
everything needed to run the application on the target platform is included in
the executable, allowing the application to run with minimal resource overhead.

Compiling a native executable takes a bit longer, as GraalVM performs additional
steps to remove unnecessary codepaths. Use the  `native` profile to compile a
native executable:

```shell script
./mvnw package -Dnative
```

After getting a cup of coffee, you'll be able to run this executable directly:

```shell script
> ./target/image-detection-1.0.0-SNAPSHOT-runner
```

## External Service
The external service Imagga is used for detecting objects of an image. 
A basic authorization code needs to be sent as part of the Header for the POST and PUT requests.

Authorization: Basic xyz

## Tests

Run the tests using the command

```shell script
./mvnw quarkus:test
```

## Swagger-UI

http://localhost:8080/image-detect/swagger-ui
