package birds

import dev.icerock.moko.mvvm.viewmodel.ViewModel
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import model.BirdImage

class BirdsViewModel : ViewModel() {
    private val _birdsViewState = MutableStateFlow(BirdsViewState())
    val birdsViewState = _birdsViewState.asStateFlow()

    private val httpClient = HttpClient {
        install(ContentNegotiation) {
            json()
        }
    }

    init {
        updateImages()
    }

    override fun onCleared() {
        httpClient.close()
        super.onCleared()
    }

    fun updateImages() {
        viewModelScope.launch {
            val images = getImages()
            _birdsViewState.update {
                it.copy(images = images)
            }
        }
    }

    private suspend fun getImages(): List<BirdImage> {
        val images = httpClient
            .get("https://sebastianaigner.github.io/demo-image-api/pictures.json")
            .body<List<BirdImage>>()
        return images
    }
}