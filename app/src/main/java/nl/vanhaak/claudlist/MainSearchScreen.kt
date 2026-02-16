package nl.vanhaak.claudlist

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import nl.vanhaak.claudlist.ui.theme.ClaudListTheme

@Composable
fun MainSearchScreen(
    onNavigateToFilter: () -> Unit,
    onNavigateToFilterEdit: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier.fillMaxSize()
    ) {
        Text(
            text = stringResource(R.string.screen_main_search),
            style = MaterialTheme.typography.headlineMedium
        )
        Spacer(modifier = Modifier.height(24.dp))
        Button(onClick = onNavigateToFilter) {
            Text(stringResource(R.string.btn_go_to_filter))
        }
        Spacer(modifier = Modifier.height(12.dp))
        Button(onClick = onNavigateToFilterEdit) {
            Text(stringResource(R.string.btn_go_to_filter_edit))
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun MainSearchScreenPreview() {
    ClaudListTheme {
        MainSearchScreen(
            onNavigateToFilter = {},
            onNavigateToFilterEdit = {}
        )
    }
}
