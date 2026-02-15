package nl.vanhaak.claudlist

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class GenericSearchOptionsViewModel : ViewModel(), IGenericSearchOptionsViewModel {

    private val _viewState = MutableStateFlow(
        SOViewState(
            soConfigListState = SOConfigListState(
                items = sampleItems,
                isEditMode = true,
                infoBannerText = "Wijzig je filters of de volgorde waarin ze worden getoond."
            )
        )
    )
    override val viewState: StateFlow<SOViewState> = _viewState.asStateFlow()

    override fun moveItem(fromIndex: Int, toIndex: Int) {
        _viewState.update { state ->
            val mutableItems = state.soConfigListState.items.toMutableList()
            mutableItems.add(toIndex, mutableItems.removeAt(fromIndex))
            state.copy(
                soConfigListState = state.soConfigListState.copy(items = mutableItems)
            )
        }
    }

    override fun deleteItem(id: String) {
        _viewState.update { state ->
            state.copy(
                soConfigListState = state.soConfigListState.copy(
                    items = state.soConfigListState.items.filter { it.id != id }
                )
            )
        }
    }

    override fun toggleEditMode() {
        _viewState.update { state ->
            state.copy(
                soConfigListState = state.soConfigListState.copy(
                    isEditMode = !state.soConfigListState.isEditMode
                )
            )
        }
    }

    companion object {
        private val sampleItems = listOf(
            SOConfigList.HeaderIVM(id = "h1", title = "Default filter"),
            SOConfigList.SearchOptionIVM(id = "c1", text = "Alle woningen"),
            SOConfigList.HeaderIVM(id = "h2", title = "Filters", showAddButton = true),
            SOConfigList.SearchOptionIVM(id = "c2", text = "Amsterdam"),
            SOConfigList.SearchOptionIVM(id = "c3", text = "Rotterdam"),
            SOConfigList.SearchOptionIVM(id = "c4", text = "Utrecht"),
            SOConfigList.SearchOptionIVM(id = "c5", text = "Den Haag"),
        )
    }
}