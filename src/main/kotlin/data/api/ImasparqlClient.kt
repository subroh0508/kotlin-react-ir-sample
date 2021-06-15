package data.api

import data.api.serializer.SparqlResponse
import kotlinx.serialization.KSerializer

interface ImasparqlClient {
    suspend fun <T> execute(
        query: String,
        serializer: KSerializer<T>,
    ): SparqlResponse<T>
}
