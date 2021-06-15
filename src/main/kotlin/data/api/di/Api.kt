package data.api.di

import data.api.ContentType
import data.api.ImasparqlApiClient
import data.api.ImasparqlClient
import io.ktor.client.HttpClient
import io.ktor.client.engine.js.Js
import io.ktor.client.features.defaultRequest
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.request.accept
import io.ktor.http.URLProtocol
import kotlinx.serialization.json.Json as SerializationJson
import org.koin.dsl.module

object Api {
    val Module get() = module {
        single { client }
        single<ImasparqlClient> { ImasparqlApiClient(get()) }
    }

    private const val HOSTNAME = "sparql.crssnky.xyz"
    private val client get() = HttpClient(Js) {
        defaultRequest {
            url {
                protocol = URLProtocol.HTTPS
                host = HOSTNAME
            }
            accept(ContentType.Application.SparqlJson)
        }
        Json {
            acceptContentTypes = listOf(ContentType.Application.SparqlJson)
            serializer = KotlinxSerializer(SerializationJson {
                isLenient = true
                ignoreUnknownKeys = true
                allowSpecialFloatingPointValues = true
                useArrayPolymorphism = true
            })
        }
    }
}