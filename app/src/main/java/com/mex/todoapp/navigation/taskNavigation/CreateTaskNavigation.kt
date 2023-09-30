package com.mex.todoapp.navigation.taskNavigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.mex.todoapp.features.createTask.CreateTaskScreen


const val createTaskRoute = "createTask"

fun NavController.navigateToCreateTask(navOptions: NavOptions? = null) {
    this.navigate(createTaskRoute, navOptions)
}

fun NavGraphBuilder.createTask(navController: NavHostController) {
    composable(
        route = createTaskRoute,
        enterTransition = {
            slideIntoContainer(
                AnimatedContentTransitionScope.SlideDirection.Left,
                animationSpec = tween(700)
            ) + fadeIn(animationSpec = tween(100))
        },
        popEnterTransition = {
            slideIntoContainer(
                AnimatedContentTransitionScope.SlideDirection.Right,
                animationSpec = tween(700)
            ) + fadeIn(animationSpec = tween(300))
        },
        exitTransition = {
            fadeOut(animationSpec = tween(300))
        },
        popExitTransition = {
            fadeOut(animationSpec = tween(300))
        }) {

        CreateTaskScreen(navController)
    }
}