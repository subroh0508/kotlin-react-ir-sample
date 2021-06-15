plugins {
    kotlin("js")
}

group = "net.subroh0508"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven { url = uri("https://maven.pkg.jetbrains.space/kotlin/p/kotlin/kotlin-js-wrappers") }
}

dependencies {
    val wrappers = Libraries.JsWrappers(kotlinVersion)
    implementation(wrappers.react)
    implementation(wrappers.reactDom)
    implementation(wrappers.styled)
    implementation(wrappers.reactRouterDom)
}

kotlin {
    js(IR) {
        binaries.executable()
        browser {
            commonWebpackConfig {
                cssSupport.enabled = true
            }
        }
    }
}
