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

    private fun enforceDefaultSection(items: List<SOConfigList>, draggedItemId: String): List<SOConfigList> {
        val sections = splitIntoSections(items)
        val defaultSection = sectionById(sections, "h1") ?: return items
        val filtersSection = sectionById(sections, "h2")

        bumpExtraDefault(defaultSection, filtersSection, draggedItemId)
        ensurePlaceholder(defaultSection)

        return sections.flatten()
    }

    /** Split flat list into sections, each starting with a HeaderIVM. */
    private fun splitIntoSections(items: List<SOConfigList>): MutableList<MutableList<SOConfigList>> =
        items.fold(mutableListOf()) { acc, item ->
            if (item is SOConfigList.HeaderIVM) acc.add(mutableListOf(item))
            else acc.lastOrNull()?.add(item)
            acc
        }

    private fun sectionById(sections: List<List<SOConfigList>>, headerId: String) =
        sections.firstOrNull { (it.firstOrNull() as? SOConfigList.HeaderIVM)?.id == headerId }
                as? MutableList<SOConfigList>

    /** If default has >1 search option, move the old one to top of filters. */
    private fun bumpExtraDefault(
        defaultSection: MutableList<SOConfigList>,
        filtersSection: MutableList<SOConfigList>?,
        draggedItemId: String
    ) {
        val options = defaultSection.filterIsInstance<SOConfigList.SearchOptionIVM>()
        if (options.size <= 1) return

        val oldDefault = options.first { it.id != draggedItemId }
        defaultSection.remove(oldDefault)
        filtersSection?.add(1, oldDefault)
    }

    /** Remove all placeholders, then add one back if no search option remains. */
    private fun ensurePlaceholder(section: MutableList<SOConfigList>) {
        section.removeAll { it is SOConfigList.PlaceholderIVM }
        if (section.none { it is SOConfigList.SearchOptionIVM }) {
            section.add(1, DEFAULT_PLACEHOLDER)
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
