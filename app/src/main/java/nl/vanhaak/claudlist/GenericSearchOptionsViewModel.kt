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
            val originalItems = state.soConfigListState.items
            val mutableItems = originalItems.toMutableList()

            // Perform the swap
            val movedItem = mutableItems.removeAt(fromIndex)
            mutableItems.add(toIndex, movedItem)

            // Rebuild placeholders and enforce swap for default section
            val rebuilt = rebuildPlaceholders(mutableItems, movedItem.id) ?: return@update state
            state.copy(
                soConfigListState = state.soConfigListState.copy(items = rebuilt)
            )
        }
    }

    private fun rebuildPlaceholders(items: MutableList<SOConfigList>, draggedItemId: String): List<SOConfigList>? {
        // Find default section range: from h1 header to the next header
        val h1Index = items.indexOfFirst { it is SOConfigList.HeaderIVM && it.id == "h1" }
        if (h1Index == -1) return items

        // Remove all placeholders from default section
        fun defaultSectionEnd(): Int {
            val idx = items.drop(h1Index + 1).indexOfFirst { it is SOConfigList.HeaderIVM }
            return if (idx == -1) items.size else h1Index + 1 + idx
        }
        items.subList(h1Index + 1, defaultSectionEnd()).removeAll { it is SOConfigList.PlaceholderIVM }

        // Find SearchOptionIVMs in default section
        val defaultOptions = items.subList(h1Index + 1, defaultSectionEnd())
            .filterIsInstance<SOConfigList.SearchOptionIVM>()

        if (defaultOptions.size > 1) {
            // Kick the old default (the one that is NOT the dragged item) to top of Filters section
            val oldDefault = defaultOptions.first { it.id != draggedItemId }
            items.remove(oldDefault)
            // Insert right after the Filters header (h2)
            val h2Index = items.indexOfFirst { it is SOConfigList.HeaderIVM && it.id == "h2" }
            if (h2Index != -1) {
                items.add(h2Index + 1, oldDefault)
            } else {
                items.add(oldDefault) // fallback: append
            }
        }

        // Re-check: if default section is now empty, insert placeholder
        val remainingOptions = items.subList(h1Index + 1, defaultSectionEnd())
            .count { it is SOConfigList.SearchOptionIVM }
        if (remainingOptions == 0) {
            items.add(
                h1Index + 1,
                SOConfigList.PlaceholderIVM(
                    id = "ph1",
                    text = "Sleep een filter hierheen om het als standaard in te stellen"
                )
            )
        }

        return items
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
            val newIsEditMode = !state.soConfigListState.isEditMode
            state.copy(
                soConfigListState = state.soConfigListState.copy(
                    isEditMode = newIsEditMode
                ),
                toolbarViewState = state.toolbarViewState.copy(
                    actionStartTitle = if (newIsEditMode) R.string.btn_edit_order else R.string.btn_done
                )
            )
        }
    }

    override fun onNavEvent(event: NavEvent) {
        _viewState.update { state ->
            state.copy(
                toolbarViewState = when (event) {
                    is NavEvent.ToMainSearch -> ToolbarViewState(
                        title = R.string.toolbar_main_search,
                        icStartResId = R.drawable.ic_place,
                        icEndResId = R.drawable.ic_add
                    )
                    is NavEvent.ToFilter -> ToolbarViewState(
                        title = R.string.toolbar_filter
                    )
                    is NavEvent.ToFilterEdit -> ToolbarViewState(
                        title = R.string.toolbar_filter_edit,
                        actionStartTitle = if (state.soConfigListState.isEditMode) R.string.btn_edit_order else R.string.btn_done
                    )
                }
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
