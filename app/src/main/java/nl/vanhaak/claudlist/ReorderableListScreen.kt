package nl.vanhaak.claudlist

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import nl.vanhaak.claudlist.ui.theme.ClaudListTheme
import sh.calvin.reorderable.ReorderableItem
import sh.calvin.reorderable.rememberReorderableLazyListState

private val BlueToolbar = Color(0xFF1565C0)
private val OrangeAccent = Color(0xFFE65100)
private val BackgroundGray = Color(0xFFF0F2F5)
private val InfoBannerColor = Color(0xFFFFA726)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReorderableListScreen(
    viewModel: IGenericSearchOptionsViewModel,
    modifier: Modifier = Modifier
) {
    val state by viewModel.viewState.collectAsState()
    val listState = state.soConfigListState
    val items = listState.items
    val isEditMode = listState.isEditMode
    val lazyListState = rememberLazyListState()
    val reorderableLazyListState = rememberReorderableLazyListState(lazyListState) { from, to ->
        viewModel.moveItem(from.index, to.index)
    }

    val halfPadding = dimensionResource(R.dimen.default_half_padding)
    val activityPadding = dimensionResource(R.dimen.default_activity_padding)

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Filter Edit",
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                },
                actions = {
                    TextButton(onClick = { viewModel.toggleEditMode() }) {
                        Text(
                            text = if (isEditMode) "Wijzig volgorde" else "Gereed",
                            color = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = BlueToolbar
                )
            )
        },
        modifier = modifier
    ) { innerPadding ->
        LazyColumn(
            state = lazyListState,
            contentPadding = PaddingValues(horizontal = activityPadding, vertical = halfPadding),
            verticalArrangement = Arrangement.spacedBy(halfPadding),
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(BackgroundGray)
        ) {
            // Info banner
            if (isEditMode && listState.infoBannerText != null) {
                item(key = "__info_banner__") {
                    Card(
                        shape = RoundedCornerShape(halfPadding),
                        colors = CardDefaults.cardColors(containerColor = InfoBannerColor)
                    ) {
                        Text(
                            text = listState.infoBannerText,
                            color = Color.White,
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.padding(activityPadding)
                        )
                    }
                }
            }

            items(items, key = { it.id }) { item ->
                when (item) {
                    is SOConfigList.HeaderIVM -> {
                        HeaderRow(
                            title = item.title,
                            showAddButton = item.showAddButton && isEditMode
                        )
                    }
                    is SOConfigList.SearchOptionIVM -> {
                        ReorderableItem(reorderableLazyListState, key = item.id) { isDragging ->
                            val elevation by animateDpAsState(
                                targetValue = if (isDragging) halfPadding else dimensionResource(R.dimen.dp2),
                                label = "dragElevation"
                            )
                            Card(
                                shape = RoundedCornerShape(dimensionResource(R.dimen.default_round_corner_size_with_padding)),
                                elevation = CardDefaults.cardElevation(defaultElevation = elevation),
                                colors = CardDefaults.cardColors(containerColor = Color.White),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                SearchOptionRow(
                                    text = item.text,
                                    isEditMode = isEditMode,
                                    onDelete = { viewModel.deleteItem(item.id) },
                                    dragModifier = Modifier.draggableHandle()
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun HeaderRow(
    title: String,
    showAddButton: Boolean,
    modifier: Modifier = Modifier
) {
    val dp4 = dimensionResource(R.dimen.dp4)

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .padding(top = dimensionResource(R.dimen.default_half_padding), bottom = dp4)
    ) {
        Text(
            text = title.uppercase(),
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.Bold,
            color = Color.Gray
        )
        Spacer(modifier = Modifier.weight(1f))
        if (showAddButton) {
            TextButton(
                onClick = { },
                modifier = Modifier.border(
                    width = dimensionResource(R.dimen.dp1),
                    color = BlueToolbar,
                    shape = RoundedCornerShape(dp4)
                )
            ) {
                Text(
                    text = "Nieuw +",
                    color = BlueToolbar,
                    style = MaterialTheme.typography.labelMedium
                )
            }
        }
    }
}

@Composable
private fun SearchOptionRow(
    text: String,
    isEditMode: Boolean,
    onDelete: () -> Unit,
    dragModifier: Modifier = Modifier,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = dimensionResource(R.dimen.default_activity_padding), vertical = dimensionResource(R.dimen.default_half_padding))
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge
        )
        Spacer(modifier = Modifier.weight(1f))
        if (isEditMode) {
            IconButton(onClick = onDelete) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete",
                    tint = OrangeAccent
                )
            }
        } else {
            Text(
                text = "\u2630",
                style = MaterialTheme.typography.titleLarge,
                color = Color.Gray,
                modifier = dragModifier
            )
        }
    }
}

private class FakeSearchOptionsViewModel : IGenericSearchOptionsViewModel {
    override val viewState: StateFlow<SOViewState> = MutableStateFlow(
        SOViewState(
            soConfigListState = SOConfigListState(
                items = listOf(
                    SOConfigList.HeaderIVM(id = "h1", title = "Default filter"),
                    SOConfigList.SearchOptionIVM(id = "c1", text = "Alle woningen"),
                    SOConfigList.HeaderIVM(id = "h2", title = "Filters", showAddButton = true),
                    SOConfigList.SearchOptionIVM(id = "c2", text = "Amsterdam"),
                    SOConfigList.SearchOptionIVM(id = "c3", text = "Rotterdam"),
                ),
                isEditMode = false,
                infoBannerText = "Wijzig je filters of de volgorde waarin ze worden getoond."
            )
        )
    )
    override fun moveItem(fromIndex: Int, toIndex: Int) {}
    override fun deleteItem(id: String) {}
    override fun toggleEditMode() {}
}

private class FakeEditModeViewModel : IGenericSearchOptionsViewModel {
    override val viewState: StateFlow<SOViewState> = MutableStateFlow(
        SOViewState(
            soConfigListState = SOConfigListState(
                items = listOf(
                    SOConfigList.HeaderIVM(id = "h1", title = "Default filter"),
                    SOConfigList.SearchOptionIVM(id = "c1", text = "Alle woningen"),
                    SOConfigList.HeaderIVM(id = "h2", title = "Filters", showAddButton = true),
                    SOConfigList.SearchOptionIVM(id = "c2", text = "Amsterdam"),
                    SOConfigList.SearchOptionIVM(id = "c3", text = "Rotterdam"),
                ),
                isEditMode = true,
                infoBannerText = "Wijzig je filters of de volgorde waarin ze worden getoond."
            )
        )
    )
    override fun moveItem(fromIndex: Int, toIndex: Int) {}
    override fun deleteItem(id: String) {}
    override fun toggleEditMode() {}
}

@Preview(showBackground = true)
@Composable
private fun ReorderableListScreenPreview() {
    ClaudListTheme {
        ReorderableListScreen(viewModel = FakeSearchOptionsViewModel())
    }
}

@Preview(showBackground = true)
@Composable
private fun ReorderableListScreenEditModePreview() {
    ClaudListTheme {
        ReorderableListScreen(viewModel = FakeEditModeViewModel())
    }
}
