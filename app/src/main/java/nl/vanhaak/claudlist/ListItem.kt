package nl.vanhaak.claudlist

sealed class ListItem {
    abstract val id: String

    data class Header(override val id: String, val title: String) : ListItem()
    data class Content(override val id: String, val text: String) : ListItem()
}
