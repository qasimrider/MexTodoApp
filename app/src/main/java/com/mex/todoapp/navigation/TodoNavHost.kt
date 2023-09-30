package com.mex.todoapp.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.mex.todoapp.navigation.taskNavigation.createTask
import com.mex.todoapp.navigation.taskNavigation.createTaskRoute
import com.mex.todoapp.navigation.taskNavigation.taskListRoute
import com.mex.todoapp.navigation.taskNavigation.tasksListNavigation

@Composable
fun ToDoNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController,
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = taskListRoute,
    ) {
        tasksListNavigation(navController)
        createTask(navController)
    }
}