package data.api

import io.ktor.http.ContentType as KtorContentType

abstract class ContentType {
    object Application {
        val SparqlJson = KtorContentType("application", "sparql-results+json")
    }
}
