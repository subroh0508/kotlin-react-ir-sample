import data.api.di.Api
import data.infra.di.Repositories
import react.dom.render
import kotlinx.browser.document
import kotlinx.browser.window
import org.koin.dsl.koinApplication

val koinApp = koinApplication {
    modules(Api.Module + Repositories.Module)
}

fun main() {
    window.onload = {
        render(document.getElementById("root"), callback = {}) {
            IdolsPageComponent()
        }
    }
}
