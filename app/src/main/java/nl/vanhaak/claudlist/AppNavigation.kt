package nl.vanhaak.claudlist

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

object Routes {
    const val MAIN_SEARCH = "main_search"
    const val FILTER = "filter"
    const val FILTER_EDIT = "filter_edit"
}

@Composable
fun AppNavigation(
    navController: NavHostController,
    viewModel: IGenericSearchOptionsViewModel,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Routes.MAIN_SEARCH,
        modifier = modifier
    ) {
        composable(Routes.MAIN_SEARCH) {
            MainSearchScreen(
                onNavigateToFilter = { navController.navigate(Routes.FILTER) },
                onNavigateToFilterEdit = { navController.navigate(Routes.FILTER_EDIT) }
            )
        }
        composable(Routes.FILTER) {
            FilterScreen()
        }
        composable(Routes.FILTER_EDIT) {
            ReorderableListScreen(viewModel = viewModel)
        }
    }
}