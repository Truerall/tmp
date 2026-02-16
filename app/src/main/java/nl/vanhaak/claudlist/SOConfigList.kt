package nl.vanhaak.claudlist

sealed class SOConfigList {
    abstract val id: String

    data class HeaderIVM(override val id: String, val title: String, val showAddButton: Boolean = false) : SOConfigList()
    data class SearchOptionIVM(override val id: String, val text: String) : SOConfigList()
    data class PlaceholderIVM(override val id: String, val text: String) : SOConfigList()
}