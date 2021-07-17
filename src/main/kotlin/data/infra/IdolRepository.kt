package data.infra

import data.api.ImasparqlClient
import data.api.di.Api.HOSTNAME
import data.api.json.IdolJson
import data.model.Idol

private external fun encodeURIComponent(s: String): String

class IdolRepository(
    private val client: ImasparqlClient,
) {
    suspend fun search(
        name: String? = null,
        age: String? = null,
    ): List<Idol> {
        val query = when {
            name != null -> buildQuery(name = name)
            age != null -> buildQuery(age = age)
            else -> null
        }

        return if (query == null) listOf() else client.execute(
            query,
            IdolJson.serializer(),
        ).results.bindings.mapNotNull { it.toEntity() }
    }

    private fun buildQuery(
        name: String? = null,
        age: String? = null,
    ) = buildString {
        append("$ENDPOINT_MAIN?output=json&query=")
        append(
            encodeURIComponent(
                """
                PREFIX schema: <http://schema.org/>
                PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>
                PREFIX imas: <https://sparql.crssnky.xyz/imasrdf/URIs/imas-schema.ttl#>
                PREFIX foaf: <http://xmlns.com/foaf/0.1/>
                SELECT *
                WHERE {
                  ?s a imas:Idol;
                    schema:name ?name;
                    imas:Brand ?brand;
                    imas:Color ?color;
                    foaf:age ?age;
                    schema:birthPlace ?birthplace;
                    imas:IdolListURL ?idollistUrl.
                  {
                    SELECT ?s (GROUP_CONCAT(?hobby; separator = ',') as ?hobbies)
                    WHERE {
                      ?s imas:Hobby ?hobby.
                    }
                    GROUP BY ?s
                  }
                  FILTER (lang(?name) = 'ja')
                  FILTER (str(?brand) != '1st Vision')
                  ${name?.let { "FILTER (regex(?name, '.*$it.*', 'i') || regex(?yomi, '.*$it.*', 'i'))." } ?: ""}
                  ${age?.let { "FILTER (regex(str(?age), '.*$it.*', 'i'))."  } ?: ""}
                  OPTIONAL { ?s imas:alternateNameKana ?tmp }
                  OPTIONAL { ?s imas:nameKana ?tmp }
                  OPTIONAL { ?s imas:givenNameKana ?tmp }
                  BIND(?tmp AS ?yomi)
                  BIND (REPLACE(str(?s), '${ESCAPED_ENDPOINT_RDFS_DETAIL}', '') as ?id)
                }
                ORDER BY ?yomi
                """.trimIndentAndBr()
            )
        )
    }

    private fun String.trimIndentAndBr() = trimIndent().replace("[\n\r]", "")

    private fun IdolJson.toEntity() = let { json ->
        val id = json.id["value"] ?: return@let null
        val name = json.name["value"] ?: return@let null
        val yomi = json.yomi["value"] ?: return@let null
        val color = json.color["value"] ?: "000000"
        val age = json.age["value"] ?: return@let null
        val birthplace = json.birthplace["value"] ?: return@let null
        val hobbies = json.hobbies["value"] ?: return@let null
        val idollistUrl = json.idollistUrl["value"] ?: return@let null

        Idol(id, name, yomi, "#$color", age, birthplace, hobbies.split(","), idollistUrl)
    }

    companion object {
        private const val ENDPOINT_MAIN = "/spql/imas/query"
        private const val ESCAPED_ENDPOINT_RDFS_DETAIL = """https://$HOSTNAME/imasrdf/RDFs/detail/"""
    }
}