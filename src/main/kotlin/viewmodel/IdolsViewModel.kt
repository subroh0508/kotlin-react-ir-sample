package viewmodel

import data.infra.IdolRepository
import data.model.Idol
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

class IdolsViewModel(
    private val repository: IdolRepository,
) {
    private val viewModelScope by lazy { MainScope() }

    val uiModel get() = _uiModel
    private val _uiModel by lazy { MutableStateFlow(UiModel()) }

    fun search(name: String? = null, age: String? = null) {
        val job = viewModelScope.launch(start = CoroutineStart.LAZY) {
            runCatching { executeSearching(name, age) }
                .onSuccess { _uiModel.value = UiModel(items = it) }
                .onFailure { _uiModel.value = UiModel(error = it) }
        }

        _uiModel.value = UiModel(loading = true)
        job.start()
    }

    private suspend fun executeSearching(
        name: String? = null,
        age: String? = null,
    ) = withContext(Dispatchers.Default) {
        repository.search(name, age)
    }

    data class UiModel(
        val items: List<Idol> = listOf(),
        val error: Throwable? = null,
        val loading: Boolean = false,
    )
}
