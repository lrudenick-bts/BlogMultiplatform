package com.lrudenick.blogmultiplatform.model

import com.varabyte.kobweb.compose.ui.graphics.Color.Companion.rgb
import org.jetbrains.compose.web.css.CSSColorValue
import org.jetbrains.compose.web.css.rgba

enum class Theme(
    val hex: String,
    val rgb: CSSColorValue
) {
    Primary(
        hex = "#00A2FF",
        rgb = rgb(r = 0, g = 162, b = 255)
    ),
    Secondary(
        hex = "#001019",
        rgb = rgb(r = 0, g = 16, b = 25)
    ),
    LightGray(
        hex = "#FAFAFA",
        rgb = rgb(r = 250, g = 250, b = 250)
    ),
    White(
        hex = "#FFFFFF",
        rgb = rgb (r = 255, g = 255, b = 255)
    ),
    HalfWhite(
        hex = "#FFFFFF80",
        rgb = rgba (r = 255, g = 255, b = 255, a = 0.5)
    ),
    HalfBlack(
        hex = "#00000080",
        rgb = rgba (r = 0, g = 0, b = 0, a = 0.5)
    )
}