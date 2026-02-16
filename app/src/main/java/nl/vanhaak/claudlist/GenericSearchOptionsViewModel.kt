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
            val items = state.soConfigListState.items
            val movedItem = items[fromIndex]
            val swapped = items.toMutableList().apply {
                add(toIndex, removeAt(fromIndex))
            }

            val rebuilt = enforceDefaultSection(swapped, movedItem.id)
            state.copy(
                soConfigListState = state.soConfigListState.copy(items = rebuilt)
            )
        }
    }

    /**
     * Split items by section headers, enforce max-1 in default, reassemble.
     * If default ends up with >1 option, the old one gets bumped to Filters.
     * If default is empty, a placeholder is inserted.
     */
    private fun enforceDefaultSection(items: List<SOConfigList>, draggedItemId: String): List<SOConfigList> {
        // Split the flat list into sections: each section = [Header, ...items]
        val sections = items.fold(mutableListOf<MutableList<SOConfigList>>()) { acc, item ->
            if (item is SOConfigList.HeaderIVM) acc.add(mutableListOf(item))
            else acc.lastOrNull()?.add(item)
            acc
        }

        val defaultSection = sections.firstOrNull {
            (it.firstOrNull() as? SOConfigList.HeaderIVM)?.id == "h1"
        } ?: return items
        val filtersSection = sections.firstOrNull {
            (it.firstOrNull() as? SOConfigList.HeaderIVM)?.id == "h2"
        }

        // Strip placeholders, collect search options
        val defaultOptions = defaultSection.drop(1)
            .filterIsInstance<SOConfigList.SearchOptionIVM>()

        if (defaultOptions.size > 1) {
            // Bump old default (not the dragged item) to top of Filters
            val oldDefault = defaultOptions.first { it.id != draggedItemId }
            defaultSection.remove(oldDefault)
            filtersSection?.add(1, oldDefault) // after the header
        }

        // Replace placeholders: remove all, add one if section is empty
        defaultSection.removeAll { it is SOConfigList.PlaceholderIVM }
        val hasOption = defaultSection.any { it is SOConfigList.SearchOptionIVM }
        if (!hasOption) {
            defaultSection.add(1, DEFAULT_PLACEHOLDER)
        }

        return sections.flatten()
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
        private val DEFAULT_PLACEHOLDER = SOConfigList.PlaceholderIVM(
            id = "ph1",
            text = "Sleep een filter hierheen om het als standaard in te stellen"
        )

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
