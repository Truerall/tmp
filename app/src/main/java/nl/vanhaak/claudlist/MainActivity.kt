package nl.vanhaak.claudlist

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import nl.vanhaak.claudlist.ui.theme.ClaudListTheme

class MainActivity : ComponentActivity() {

    private val viewModel: GenericSearchOptionsViewModel by viewModels()

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ClaudListTheme {
                val navController = rememberNavController()
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route

                Scaffold(
                    topBar = {
                        TopAppBar(
                            title = {
                                Text(
                                    text = toolbarTitle(currentRoute),
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold
                                )
                            },
                            actions = {
                                ToolbarActions(
                                    currentRoute = currentRoute,
                                    viewModel = viewModel
                                )
                            },
                            colors = TopAppBarDefaults.topAppBarColors(
                                containerColor = BlueToolbar
                            )
                        )
                    }
                ) { innerPadding ->
                    AppNavigation(
                        navController = navController,
                        viewModel = viewModel,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

private fun toolbarTitle(currentRoute: String?): String = when (currentRoute) {
    Routes.MAIN_SEARCH -> "Main Search"
    Routes.FILTER -> "Filter"
    Routes.FILTER_EDIT -> "Filter Edit"
    else -> ""
}

@Composable
private fun ToolbarActions(
    currentRoute: String?,
    viewModel: IGenericSearchOptionsViewModel
) {
    when (currentRoute) {
        Routes.MAIN_SEARCH -> {
            IconButton(onClick = { }) {
                Icon(
                    imageVector = Icons.Default.Place,
                    contentDescription = "Map",
                    tint = Color.White
                )
            }
            IconButton(onClick = { }) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add",
                    tint = Color.White
                )
            }
        }
        Routes.FILTER_EDIT -> {
            val state by viewModel.viewState.collectAsState()
            val isEditMode = state.soConfigListState.isEditMode
            TextButton(onClick = { viewModel.toggleEditMode() }) {
                Text(
                    text = if (isEditMode) "Wijzig volgorde" else "Gereed",
                    color = Color.White
                )
            }
        }
    }
}
