package nl.vanhaak.claudlist

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

@Serializable object MainSearch
@Serializable object Filter
@Serializable object FilterEdit

@Composable
fun AppNavigation(
    navController: NavHostController,
    viewModel: IGenericSearchOptionsViewModel,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = MainSearch,
        modifier = modifier
    ) {
        composable<MainSearch> {
            MainSearchScreen(
                onNavigateToFilter = {
                    viewModel.onNavEvent(NavEvent.ToFilter)
                    navController.navigate(Filter)
                },
                onNavigateToFilterEdit = {
                    viewModel.onNavEvent(NavEvent.ToFilterEdit)
                    navController.navigate(FilterEdit)
                }
            )
        }
        composable<Filter> {
            FilterScreen()
        }
        composable<FilterEdit> {
            ReorderableListScreen(viewModel = viewModel)
        }
    }
}
