package nl.vanhaak.claudlist

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import sh.calvin.reorderable.ReorderableItem
import sh.calvin.reorderable.rememberReorderableLazyListState

private val sampleItems = listOf(
    ListItem.Header(id = "h1", title = "Groceries"),
    ListItem.Content(id = "c1", text = "Milk"),
    ListItem.Content(id = "c2", text = "Eggs"),
    ListItem.Content(id = "c3", text = "Bread"),
    ListItem.Header(id = "h2", title = "Household"),
    ListItem.Content(id = "c4", text = "Paper towels"),
    ListItem.Content(id = "c5", text = "Dish soap"),
    ListItem.Header(id = "h3", title = "Work"),
    ListItem.Content(id = "c6", text = "Finish report"),
    ListItem.Content(id = "c7", text = "Reply to emails"),
    ListItem.Content(id = "c8", text = "Schedule meeting"),
)

@Composable
fun ReorderableListScreen(modifier: Modifier = Modifier) {
    val list = remember { sampleItems.toMutableStateList() }
    val lazyListState = rememberLazyListState()
    val reorderableLazyListState = rememberReorderableLazyListState(lazyListState) { from, to ->
        list.apply {
            add(to.index, removeAt(from.index))
        }
    }

    LazyColumn(
        state = lazyListState,
        modifier = modifier.fillMaxSize()
    ) {
        items(list, key = { it.id }) { item ->
            ReorderableItem(reorderableLazyListState, key = item.id) { isDragging ->
                val elevation by animateDpAsState(
                    targetValue = if (isDragging) 8.dp else 0.dp,
                    label = "dragElevation"
                )
                Surface(
                    shadowElevation = elevation,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    when (item) {
                        is ListItem.Header -> HeaderRow(
                            title = item.title,
                            modifier = Modifier.draggableHandle()
                        )
                        is ListItem.Content -> ContentRow(
                            text = item.text,
                            modifier = Modifier.draggableHandle()
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun HeaderRow(title: String, modifier: Modifier = Modifier) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.primaryContainer)
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Text(
            text = "☰",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(end = 12.dp)
        )
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onPrimaryContainer
        )
    }
}

@Composable
private fun ContentRow(text: String, modifier: Modifier = Modifier) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Text(
            text = "☰",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(end = 12.dp)
        )
        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}
