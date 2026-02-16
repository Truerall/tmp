package nl.vanhaak.claudlist

sealed class NavEvent {
    data object ToMainSearch : NavEvent()
    data object ToFilter : NavEvent()
    data object ToFilterEdit : NavEvent()
}