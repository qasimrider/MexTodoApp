package com.mex.todoapp.navigation.taskNavigation

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.mex.todoapp.features.taskList.TasksListScreen

const val taskListRoute = "taskRoute"

fun NavController.navigateToTasksList(navOptions: NavOptions? = null) {
    this.navigate(taskListRoute, navOptions)
}

fun NavGraphBuilder.tasksListNavigation(navController: NavHostController) {
    composable(route = taskListRoute) {
        TasksListScreen(navController = navController)
    }
}