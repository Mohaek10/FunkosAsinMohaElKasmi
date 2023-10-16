plugins {
    id("java")
}

group = "dev.mohaek"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.xerial:sqlite-jdbc:3.41.2.2")
    //Test
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation("org.mockito:mockito-junit-jupiter:5.5.0")
    testImplementation("org.mockito:mockito-core:5.5.0")
    //H2
    implementation("com.h2database:h2:2.2.224")
    //Ibatis
    implementation("org.mybatis:mybatis:3.5.13")
    //Lombok
    implementation("org.projectlombok:lombok:1.18.28")
    testImplementation("org.projectlombok:lombok:1.18.28")
    annotationProcessor("org.projectlombok:lombok:1.18.28")
    //Hikary
    implementation("com.zaxxer:HikariCP:5.0.1")
    //Logger
    implementation("ch.qos.logback:logback-classic:1.4.11")
    implementation("com.opencsv:opencsv:5.8")
    implementation("com.google.code.gson:gson:2.10.1")
}

tasks.test {
    useJUnitPlatform()
}