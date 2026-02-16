package nl.vanhaak.claudlist

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

data class SOViewState(
    val soConfigListState: SOConfigListState,
    val toolbarViewState: ToolbarViewState = ToolbarViewState(
        title = R.string.toolbar_main_search,
        icStartResId = R.drawable.ic_place,
        icEndResId = R.drawable.ic_add
    )
)

data class ToolbarViewState(
    @StringRes val title: Int = 0,
    @DrawableRes val icStartResId: Int? = null,
    @StringRes val actionStartTitle: Int? = null,
    @DrawableRes val icEndResId: Int? = null
)

data class SOConfigListState(
    val items: List<SOConfigList>,
    val isEditMode: Boolean,
    val infoBannerText: String? = null
)