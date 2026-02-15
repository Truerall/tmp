package nl.vanhaak.claudlist

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import nl.vanhaak.claudlist.ui.theme.ClaudListTheme

class MainActivity : ComponentActivity() {

    private val viewModel: GenericSearchOptionsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ClaudListTheme {
                ReorderableListScreen(viewModel = viewModel)
            }
        }
    }
}
