package com.jaehong.presentation.ui.navigation

import com.jaehong.presentation.ui.navigation.DestinationType.MAP_VIEW
import com.jaehong.presentation.ui.navigation.DestinationType.SPLASH


sealed class Destination(private val route: String) {

    operator fun invoke(): String = route

    object Splash : Destination(SPLASH)

    object MapView : Destination(MAP_VIEW)
}
