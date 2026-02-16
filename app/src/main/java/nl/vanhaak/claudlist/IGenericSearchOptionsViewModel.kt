package nl.vanhaak.claudlist

import kotlinx.coroutines.flow.StateFlow

interface IGenericSearchOptionsViewModel {
    val viewState: StateFlow<SOViewState>
    fun moveItem(fromIndex: Int, toIndex: Int)
    fun deleteItem(id: String)
    fun toggleEditMode()
    fun onNavEvent(event: NavEvent)
}