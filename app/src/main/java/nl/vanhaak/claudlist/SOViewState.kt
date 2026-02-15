package nl.vanhaak.claudlist

data class SOViewState(
    val soConfigListState: SOConfigListState
)

data class SOConfigListState(
    val items: List<SOConfigList>,
    val isEditMode: Boolean,
    val infoBannerText: String? = null
)