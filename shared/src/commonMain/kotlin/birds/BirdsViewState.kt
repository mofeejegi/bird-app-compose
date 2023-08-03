package birds

import model.BirdImage

data class BirdsViewState(
    val images: List<BirdImage> = emptyList()
)