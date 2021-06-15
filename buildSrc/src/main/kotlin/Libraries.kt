@file:Suppress("HardcodedStringLiteral")

object Libraries {
    object Serialization {
        private const val version = "1.2.1"

        const val core = "org.jetbrains.kotlinx:kotlinx-serialization-json:$version"
    }

    object Coroutine {
        private const val version = "1.5.0"

        const val core = "org.jetbrains.kotlinx:kotlinx-coroutines-core:$version"
    }

    object Ktor {
        private const val version = "1.6.0"

        const val client = "io.ktor:ktor-client-core:$version"
        const val json = "io.ktor:ktor-client-json:$version"
        const val serialization = "io.ktor:ktor-client-serialization:$version"
    }

    object Koin {
        private const val version = "3.0.2"

        const val core = "io.insert-koin:koin-core:$version"
    }

    object Html {
        private const val version = "0.7.3"
        const val core = "org.jetbrains.kotlinx:kotlinx-html-js:$version"
    }

    class JsWrappers(kotlinVersion: String) {
        private val wrappersBuild = "pre.211-kotlin-$kotlinVersion"

        private val reactVersion = "${Npm.react}-$wrappersBuild"
        val react = "org.jetbrains.kotlin-wrappers:kotlin-react:$reactVersion"
        val reactDom = "org.jetbrains.kotlin-wrappers:kotlin-react-dom:$reactVersion"

        private val cssVersion = "1.0.0-$wrappersBuild"
        val css = "org.jetbrains.kotlin-wrappers:kotlin-css-js:$cssVersion"

        private val styledVersion = "${Npm.styledComponent}-$wrappersBuild"
        val styled = "org.jetbrains.kotlin-wrappers:kotlin-styled:$styledVersion"

        private val extensionsVersion = "1.0.1-$wrappersBuild"
        val extensions = "org.jetbrains.kotlin-wrappers:kotlin-extensions:$extensionsVersion"

        private val reactRouterDomVersion = "${Npm.reactRouterDom}-$wrappersBuild"
        val reactRouterDom = "org.jetbrains.kotlin-wrappers:kotlin-react-router-dom:$reactRouterDomVersion"
    }

    object Npm {
        const val styledComponent = "5.3.0"
        const val react = "17.0.2"
        const val reactRouterDom = "5.2.0"
    }
}
