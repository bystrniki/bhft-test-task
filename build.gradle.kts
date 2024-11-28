plugins {
    kotlin("jvm") version "2.0.21"
    id("io.qameta.allure") version "2.12.0"
    id("io.gatling.gradle") version "3.10.5"
}

group = "com.bystritskiy"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.squareup.retrofit2:retrofit:2.11.0")
    implementation("com.squareup.retrofit2:converter-jackson:2.11.0")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.15.2")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.15.2")
    implementation("com.squareup.okhttp3:okhttp:4.12.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")
    implementation("org.junit.jupiter:junit-jupiter-api:5.11.3")

    testRuntimeOnly("org.aspectj:aspectjweaver:1.9.22.1")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.11.3")
    testImplementation("org.junit.jupiter:junit-jupiter-engine:5.11.3")
    testImplementation("org.junit.jupiter:junit-jupiter-params:5.11.3")
    testImplementation("org.assertj:assertj-core:3.26.3")
    testImplementation("io.qameta.allure:allure-junit5:2.29.0")
    testImplementation("io.qameta.allure:allure-kotlin-commons:2.4.0")
    testImplementation("org.java-websocket:Java-WebSocket:1.5.7")

    gatling("io.gatling:gatling-app:3.10.5")
    gatling("io.gatling:gatling-recorder:3.10.5")
    gatling("io.gatling.highcharts:gatling-charts-highcharts:3.10.5")
    gatling("io.gatling:gatling-test-framework:3.10.5")


}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(17)
}

allure {
    adapter {
        frameworks {
            junit5 {
                enabled.set(true)
            }
        }
    }
}