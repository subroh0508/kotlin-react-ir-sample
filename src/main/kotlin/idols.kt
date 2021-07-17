import data.model.Idol
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.css.*
import kotlinx.html.InputType
import kotlinx.html.js.onChangeFunction
import kotlinx.html.js.onClickFunction
import org.koin.core.component.KoinScopeComponent
import org.koin.core.component.inject
import org.koin.core.component.newScope
import org.koin.core.scope.Scope
import org.koin.dsl.module
import org.w3c.dom.HTMLInputElement
import react.*
import react.dom.*
import styled.css
import styled.styledHr
import styled.styledInput
import viewmodel.IdolsViewModel

@Suppress("FunctionName")
fun RBuilder.IdolsPageComponent() = child(functionComponent { child(IdolsPage::class) {} })

data class IdolsState(
    val items: List<Idol> = listOf(),
    val name: String = "",
    val age: String = "",
    val loading: Boolean = false,
) : RState

@JsExport
class IdolsPage : KoinReactComponent<RProps, IdolsState>(
    module {
        scope<IdolsPage> {
            scoped { IdolsViewModel(get()) }
        }
    },
) {
    init { state = IdolsState() }

    private val viewModel: IdolsViewModel by inject()

    override fun componentDidMount() {
        viewModel.uiModel.onEach { model ->
            setState({ IdolsState(items = model.items, name = it.name, loading = model.loading) })
        }.launchIn(this)
    }

    override fun RBuilder.render() {
        styledInput {
            css {
                margin(vertical = 5.px)

                fontSize = 14.px
            }
            attrs {
                type = InputType.text
                value = state.name
                onChangeFunction = { event ->
                    setState({ IdolsState(items = it.items, name = (event.target as HTMLInputElement).value) })
                }
            }
        }

        button {
            attrs {
                onClickFunction = { viewModel.search(name = state.name.takeIf(String::isNotBlank)) }
            }

            +"Search"
        }

        if (state.loading) {
            div { +"Loading..." }
        } else {
            state.items.forEach { idol ->
                div {
                    h3 { +idol.name }
                    styledHr {
                        css {
                            height = 1.px
                            borderStyle = BorderStyle.none
                            backgroundColor = Color(idol.color)
                        }
                    }
                }
            }
        }
    }
}
