import org.jetbrains.compose.compose
import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("multiplatform")
    id("org.jetbrains.compose") version "1.0.0-alpha4-build331"
}

kotlin {
    jvm("desktop")

    sourceSets {
        named("commonMain") {
            dependencies {
                api(compose.runtime)
                api(compose.foundation)
                api(compose.material)
                api(compose.materialIconsExtended)
                api(project(":ipw:api"))
            }
        }
        named("desktopMain") {
            dependencies {
                implementation(compose.uiTooling)
                implementation(compose.desktop.currentOs)
                implementation("io.github.microutils:kotlin-logging-jvm:2.0.10")
                implementation("org.apache.logging.log4j:log4j-core:2.14.1")
                implementation("org.apache.logging.log4j:log4j-slf4j-impl:2.14.1")
                implementation(project(":ipw:lib"))
            }
        }
    }
}

rootProject.apply {
    from(rootProject.file("gradle/projectProperties.gradle.kts"))
}

version = "0.5.0"

repositories {
    buildLocalRepo(project)
    mavenLocal()
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    google()
}

tasks.withType<KotlinCompile>() {
    kotlinOptions.jvmTarget = "11"
}

/*compose.desktop {
    application {
        mainClass = "com.github.n34t0.compose.codeEditor/MainKt"
        jvmArgs("-Xmx2048m")
//        jvmArgs("-Xmx2048m", "-Dccw.debug=true")

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "sample"
            packageVersion = "1.0.0"
        }
    }
}*/
