package nl.vanhaak.claudlist

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
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
                val state by viewModel.viewState.collectAsState()
                val toolbar = state.toolbarViewState

                Scaffold(
                    topBar = {
                        TopAppBar(
                            title = {
                                if (toolbar.title != 0) {
                                    Text(
                                        text = stringResource(toolbar.title),
                                        color = Color.White,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            },
                            actions = {
                                ToolbarActions(toolbar = toolbar, viewModel = viewModel)
                            },
                            colors = TopAppBarDefaults.topAppBarColors(
                                containerColor = colorResource(R.color.primary)
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

@Composable
private fun ToolbarActions(
    toolbar: ToolbarViewState,
    viewModel: IGenericSearchOptionsViewModel
) {
    toolbar.icStartResId?.let { resId ->
        IconButton(onClick = { }) {
            Icon(
                painter = painterResource(resId),
                contentDescription = null,
                tint = Color.White
            )
        }
    }
    toolbar.actionStartTitle?.let { resId ->
        TextButton(onClick = { viewModel.toggleEditMode() }) {
            Text(
                text = stringResource(resId),
                color = Color.White
            )
        }
    }
    toolbar.icEndResId?.let { resId ->
        IconButton(onClick = { }) {
            Icon(
                painter = painterResource(resId),
                contentDescription = null,
                tint = Color.White
            )
        }
    }
}
