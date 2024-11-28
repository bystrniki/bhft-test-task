## Test assignment for QA Engineer position at BHFT

## Getting started
To get started, the prerequisites are:
- JDK 17+
- Gradle 8.10+
- Docker runtime

## Project Architecture

```bash
├── README.md
├── build.gradle.kts
├── gradle
│   └── wrapper
│       ├── gradle-wrapper.jar
│       └── gradle-wrapper.properties
├── gradle.properties
├── gradlew
├── gradlew.bat
├── settings.gradle.kts
├── src
│   ├── gatling
│   │   ├── kotlin
│   │   │   └── org
│   │   │       └── bystritskiy
│   │   │           ├── feeders
│   │   │           │   └── TodoFeeder.kt
│   │   │           └── tests
│   │   │               └── post
│   │   │                   └── PostTodoSimulation.kt
│   │   └── resources
│   │       └── logback.xml
│   ├── main
│   │   └── kotlin
│   │       └── org
│   │           └── bystritskiy
│   │               ├── api
│   │               │   ├── TodoClient.kt
│   │               │   └── impl
│   │               │       └── TodoClientImpl.kt
│   │               ├── config
│   │               │   └── EnvironmentVariables.kt
│   │               ├── extensions
│   │               │   └── TodoCleanupExtension.kt
│   │               ├── models
│   │               │   └── Todo.kt
│   │               └── utils
│   │                   └── TodoUtils.kt
│   └── test
│       └── kotlin
│           └── org
│               └── bystritskiy
│                   ├── BaseTest.kt
│                   ├── delete
│                   │   └── DeleteTodoTests.kt
│                   ├── get
│                   │   └── GetTodosTests.kt
│                   ├── post
│                   │   └── PostTodosTests.kt
│                   ├── put
│                   │   └── PutTodosTests.kt
│                   └── ws
│                       ├── BaseWebSocketTest.kt
│                       └── WebSocketTests.kt
├── task.md
└── todo-app.tar
```

## Setting up the project

### Cloning the repository
##### Cloning git repository
```bash
git clone https://github.com/bystrniki/bhft-test-task
```

### Configuring environment variables
Create the `.env` file in the project root 
##### Creating .env file
```bash
touch .env
```
There are total of 3 environment variables used in the project. You can configure yours by using reference below:
```
TODO_APP_TEST_BASE_URL=http://localhost:8080
TODO_APP_LOAD_TEST_BASE_URL=http://localhost:9090
TEST_ADMIN_CREDENTIALS=admin:admin
```

### Running container and loading image

To run the container, you need to load the Docker image first.
##### Loading Docker image
```bash
docker load -i todo-app.tar --all-platforms
```

After loading the image, you can run the container by executing the following command:
##### Running the container via Docker
```bash
docker run -p 8080:4242 -e VERBOSE=1 todo-app:latest
```

## Running tests

### Running tests

Application base URL for tests can be configured via `TODO_APP_TEST_BASE_URL` environment variable.

##### Running Docker container for tests
```bash
docker run -p 8080:4242 -e VERBOSE=1 todo-app:latest
```

##### Executing tests via JUnit
```bash
./gradlew clean test
```

### Running load tests

This project uses [Gatling](https://docs.gatling.io/) as a load testing tool. Load test scenarios can be found in the `gatling` source set under `src/gatling/kotlin/org/bystritskiy`.

It is recommended to run separate Docker container for executing load tests so they do not affect other test scenarios. Application base URL for load tests can be configured via `TODO_APP_LOAD_TEST_BASE_URL` environment variable.

##### Running Docker container for load tests
```bash
docker run -p 9090:4242 -e VERBOSE=1 todo-app:latest
```

##### Executing load tests via Gatling
```bash
./gradlew clean gatlingRun
```

After test execution finishes, you can find the HTML report in the `build/reports/gatling` directory. The report contains detailed statistics and graphs showing the performance results for the load tests.

#### Found bugs

