package data.api

import data.api.serializer.SparqlResponse
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.readText
import io.ktor.utils.io.charsets.Charset
import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json

class ImasparqlApiClient(
    private val httpClient: HttpClient
) : ImasparqlClient {
    override suspend fun <T> execute(
        query: String,
        serializer: KSerializer<T>,
    ): SparqlResponse<T> {
        val response = httpClient.get<HttpResponse>(query)

        return Json {
            isLenient = true
            ignoreUnknownKeys = true
            allowSpecialFloatingPointValues = true
            useArrayPolymorphism = true
        }.decodeFromString(
            SparqlResponse.serializer(serializer),
            response.readText(Charset.forName("UTF-8")),
        )
    }
}