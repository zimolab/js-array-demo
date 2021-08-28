plugins {
    kotlin("jvm") version "1.5.21"
    application
}

group = "com.github.zimolab"
version = "1.0-SNAPSHOT"

val tornadofxVersion: String by rootProject
val jsArrayVersion: String by rootProject
val kotlinHtmlVersion: String by rootProject

repositories {
    mavenCentral()
    // 添加jitpack仓库地址
    maven {
        setUrl("https://jitpack.io")
    }

    maven {
        setUrl("https://maven.pkg.jetbrains.space/public/p/kotlinx-html/maven")
    }

}

application {
    mainClassName = "com.github.zimolab.jsarray.demo.MainKt"
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib:1.5.21")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.7.2")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.7.2")
    implementation("com.github.zimolab:js-array:$jsArrayVersion")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.5.21")
    implementation("no.tornado:tornadofx:$tornadofxVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-html-jvm:$kotlinHtmlVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-html:$kotlinHtmlVersion")

}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}

tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
    compileTestKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
}