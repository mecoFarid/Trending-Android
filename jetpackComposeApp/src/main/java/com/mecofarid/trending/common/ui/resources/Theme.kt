package com.mecofarid.trending.common.ui.resources

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color


@Composable
fun TrendingTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val materialColorSchema =
        if (darkTheme) MaterialDarkColorScheme
        else MaterialLightColorScheme

    val trendingColorSchema =
        if (darkTheme) TrendingDarkColorScheme
        else TrendingLightColorScheme

    ProvideTrendingColor(colorSchema = trendingColorSchema) {
        MaterialTheme(
            colorScheme = materialColorSchema,
            typography = typography(),
            content = content
        )
    }
}

object TrendingTheme {
    val colorSchema: TrendingColorScheme
        @Composable
        get() = LocalTrendingColorScheme.current
}

private val MaterialDarkColorScheme = darkColorScheme(
    primaryContainer = Colors.Orange,
    onPrimaryContainer = Color.White
)

private val MaterialLightColorScheme = lightColorScheme(
    surface = Color.White,
    background = Color.White,
    primaryContainer = Colors.Orange,
    onPrimaryContainer = Color.White
)

private val TrendingLightColorScheme =
    TrendingColorScheme(
        trendingItemLanguageIndicatorColor = Colors.Blue,
        trendingItemStarColor = Colors.Orange,
        viewPlaceholderBg = Colors.Grey,
        dividerColor = Colors.LightGrey,
        secondaryTextColor = LightThemeColors.SecondaryText
    )

private val TrendingDarkColorScheme =
    TrendingColorScheme(
        trendingItemLanguageIndicatorColor = Colors.Blue,
        trendingItemStarColor = Colors.Orange,
        viewPlaceholderBg = Colors.Grey,
        dividerColor = Colors.LightGrey,
        secondaryTextColor = DarkThemeColors.SecondaryText
    )

private val LocalTrendingColorScheme = staticCompositionLocalOf<TrendingColorScheme>{
    error("Trending Color Scheme not provided")
}

@Composable
private fun ProvideTrendingColor(
    colorSchema: TrendingColorScheme,
    content: @Composable () -> Unit
) {
    val trendingColorSchema = remember {
        // Explicitly creating a new object here so we don't mutate the initial [colors]
        // provided, and overwrite the values set in it.
        colorSchema.copy()
    }.apply{
        // TODO: Investigate. No fucking idea why we copy then update.
        update(colorSchema)
    }

    CompositionLocalProvider(
        LocalTrendingColorScheme provides trendingColorSchema,
        content = content
    )
}


@Stable
class TrendingColorScheme(
    trendingItemLanguageIndicatorColor: Color,
    trendingItemStarColor: Color,
    viewPlaceholderBg: Color,
    dividerColor: Color,
    secondaryTextColor: Color
){
    var trendingItemLanguageIndicatorColor by mutableStateOf(trendingItemLanguageIndicatorColor)
        private set

    var trendingItemStarColor by mutableStateOf(trendingItemStarColor)
        private set

    var viewPlaceholderBg by mutableStateOf(viewPlaceholderBg)
        private set

    var dividerColor by mutableStateOf(dividerColor)
        private set

    var secondaryTextColor by mutableStateOf(secondaryTextColor)
        private set

    fun copy() = TrendingColorScheme(
        trendingItemLanguageIndicatorColor,
        trendingItemStarColor,
        viewPlaceholderBg,
        dividerColor,
        secondaryTextColor
    )

    fun update(other: TrendingColorScheme){
        trendingItemLanguageIndicatorColor = other.trendingItemLanguageIndicatorColor
        trendingItemStarColor = other.trendingItemStarColor
        viewPlaceholderBg = other.viewPlaceholderBg
        dividerColor = other.dividerColor
        secondaryTextColor = other.secondaryTextColor
    }
}