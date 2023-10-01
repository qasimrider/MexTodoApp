package com.mex.todoapp.features.taskList

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.border
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedIconButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.mex.todoapp.R
import com.mex.todoapp.data.model.toTask
import com.mex.todoapp.model.Category
import com.mex.todoapp.model.FilterCriteria
import com.mex.todoapp.model.Priority
import com.mex.todoapp.model.TaskView
import com.mex.todoapp.model.resetFilters
import com.mex.todoapp.mvi.UiState
import com.mex.todoapp.navigation.taskNavigation.navigateToCreateTask
import com.mex.todoapp.utility.ObserveUiState
import com.mex.todoapp.utility.convertMillisToDate
import com.mex.todoapp.utility.format


@Composable
fun TasksListScreen(
    navController: NavHostController,
    viewModel: TaskListViewModel = hiltViewModel()
) {

    val tasks = remember { mutableStateListOf<TaskView>() }
    var filterDialog by remember { mutableStateOf(false) }
    val filterCriteria by remember { mutableStateOf(FilterCriteria()) }

    viewModel.run {
        ObserveUiState {
            dispatchIntent(TaskListIntent.GetAllTasks)
            uiState.collect { getTaskUiState ->
                when (getTaskUiState) {
                    is UiState.Success -> {
                        tasks.clear()
                        tasks.addAll(getTaskUiState.data.tasks)
                    }

                    UiState.Initialization -> {}
                    UiState.Loading -> {}
                }
            }
        }

        Box(modifier = Modifier.fillMaxSize()) {
            if (tasks.isEmpty()) {
                Text(
                    text = stringResource(R.string.no_task_message),
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.align(Alignment.Center)
                )
            } else {
                TaskList(
                    tasksList = tasks,
                    onTaskClick = {},
                    onCompleteCheckChange = { completedStateChangedTask ->
                        dispatchIntent(TaskListIntent.UpdateTask(completedStateChangedTask.toTask()))
                    },
                    onTaskDelete = { deletedTask ->
                        dispatchIntent(TaskListIntent.DeleteTask(deletedTask.toTask()))
                    }
                )
            }

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {

                if (filterDialog) {
                    FilterAlertDialog(
                        filterCriteria,
                        Category.values().asList(),
                        Priority.values().asList(),
                        onCategorySelected = { filterCriteria.category = it },
                        onPrioritySelected = { filterCriteria.priority = it },
                        onFilterTextChanged = { filterCriteria.query = it },
                        isCompletedSelected = { filterCriteria.isCompleted = it },
                        onCloseDialog = { filterDialog = false },
                        onClearAllFilters = {
                            filterDialog = false
                            filterCriteria.resetFilters()
                            dispatchIntent(TaskListIntent.FilterTasks(filterCriteria))
                        },
                        onSearchClick = { dispatchIntent(TaskListIntent.FilterTasks(filterCriteria)) })
                }

                FloatingActionButton(
                    modifier = Modifier.align(Alignment.BottomStart),
                    onClick = {
                        filterDialog = true
                    }) {
                    Icon(
                        Icons.Filled.Search,
                        contentDescription = ""
                    )
                }

                FloatingActionButton(
                    modifier = Modifier.align(Alignment.BottomEnd),
                    onClick = {
                        navController.navigateToCreateTask()
                    }) {
                    Icon(
                        Icons.Filled.Add,
                        contentDescription = ""
                    )
                }
            }
        }
    }
}

//region Task list
@Composable
private fun TaskList(
    tasksList: List<TaskView>,
    onTaskClick: (TaskView) -> Unit,
    onCompleteCheckChange: (TaskView) -> Unit,
    onTaskDelete: (TaskView) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 56.dp),
        contentPadding = PaddingValues(16.dp),
    ) {
        items(tasksList.size,
            key = {
                tasksList[it].id

            }) { index ->
            TaskCard(
                tasksList[index],
                onTaskClick = { onTaskClick(it) },
                onCompleteCheckChange = { onCompleteCheckChange(it) },
                onTaskDelete = { onTaskDelete(it) })
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun TaskCard(
    taskView: TaskView,
    onTaskClick: (TaskView) -> Unit,
    onCompleteCheckChange: (TaskView) -> Unit,
    onTaskDelete: (TaskView) -> Unit
) {

    var isChecked by remember { mutableStateOf(taskView.isCompleted) }
    var isLongPressed by remember { mutableStateOf(false) }
    val borderColor = if (isLongPressed) MaterialTheme.colorScheme.error else Color.Transparent


    Card(modifier = Modifier
        .padding(12.dp)
        .fillMaxWidth()
        .border(2.dp, borderColor, RoundedCornerShape(12.dp))
        .combinedClickable(onLongClick = { isLongPressed = true }) {}) {

        Row(verticalAlignment = Alignment.Top) {

            Checkbox(
                checked = isChecked,
                onCheckedChange = { newCheckedState ->
                    isChecked = newCheckedState
                    taskView.isCompleted = newCheckedState
                    onCompleteCheckChange(taskView)
                },
                modifier = Modifier.padding(top = 4.dp)
            )

            Column(Modifier.padding(vertical = 8.dp)) {
                Text(text = taskView.title, fontSize = 24.sp, fontWeight = FontWeight.SemiBold)
                Text(
                    text = taskView.description,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Normal,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = convertMillisToDate(taskView.dueDate).format(),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = taskView.category.name,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = taskView.priority.name,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        if (isLongPressed) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp), horizontalArrangement = Arrangement.End
            ) {
                FilledIconButton(
                    onClick = {
                        isLongPressed = false
                        onTaskDelete(taskView)
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete",
                        tint = MaterialTheme.colorScheme.error
                    )

                }
                Spacer(modifier = Modifier.width(12.dp))

                OutlinedIconButton(onClick = { isLongPressed = false }) {
                    Icon(imageVector = Icons.Default.Close, contentDescription = "Close")
                }
            }
        }
    }
}

@Composable
fun FilterTasksScreen(
    filteringCriteria: FilterCriteria,
    categories: List<Category>,
    priorities: List<Priority>,
    onCategorySelected: (Category) -> Unit,
    onPrioritySelected: (Priority) -> Unit,
    isCompletedSelected: (Boolean) -> Unit,
    onFilterTextChanged: (String) -> Unit,
    onClearAllFilters: () -> Unit,
) {
    var selectedCategory by remember { mutableStateOf<Category?>(filteringCriteria.category) }
    var selectedPriority by remember { mutableStateOf<Priority?>(filteringCriteria.priority) }
    var filterText by remember { mutableStateOf(filteringCriteria.query) }
    var isCompletedChecked by remember { mutableStateOf(filteringCriteria.isCompleted) }


    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(text = stringResource(R.string.category))
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            categories.forEach { category ->
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    RadioButton(
                        selected = selectedCategory == category,
                        onClick = {
                            selectedCategory = category
                            onCategorySelected(category)
                        },
                        modifier = Modifier.selectable(selected = selectedCategory == category) { }
                    )
                    Text(text = category.name)
                }
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        Text(text = stringResource(R.string.priority))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            priorities.forEach { priority ->
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    RadioButton(
                        selected = selectedPriority == priority,
                        onClick = {
                            selectedPriority = priority
                            onPrioritySelected(priority)
                        },
                        modifier = Modifier.selectable(selected = selectedPriority == priority) { }
                    )
                    Text(text = priority.name)
                }
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(
                checked = isCompletedChecked,
                onCheckedChange = { newCheckedState ->
                    isCompletedChecked = newCheckedState
                    isCompletedSelected(newCheckedState)
                },
                modifier = Modifier.padding(top = 4.dp)
            )

            Text(text = stringResource(R.string.completed))

        }

        Spacer(modifier = Modifier.height(18.dp))

        TextField(
            value = filterText,
            onValueChange = {
                filterText = it
                onFilterTextChanged(it)
            },
            singleLine = true,
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Done
            ),
            placeholder = { Text(text = stringResource(R.string.search_tasks)) },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(18.dp))

        TextButton(
            onClick = { onClearAllFilters() },
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.CenterHorizontally)
        ) {
            Text("Clear All")

        }
    }
}

@Composable
fun FilterAlertDialog(
    filteringCriteria: FilterCriteria,
    categories: List<Category>,
    priorities: List<Priority>,
    onCategorySelected: (Category) -> Unit,
    onPrioritySelected: (Priority) -> Unit,
    onFilterTextChanged: (String) -> Unit,
    isCompletedSelected: (Boolean) -> Unit,
    onCloseDialog: () -> Unit,
    onClearAllFilters: () -> Unit,
    onSearchClick: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onCloseDialog,
        text = {
            FilterTasksScreen(
                filteringCriteria,
                categories = categories,
                priorities = priorities,
                onCategorySelected = onCategorySelected,
                onPrioritySelected = onPrioritySelected,
                onFilterTextChanged = onFilterTextChanged,
                onClearAllFilters = onClearAllFilters,
                isCompletedSelected = isCompletedSelected
            )
        },
        confirmButton = {
            Button(onClick = {
                onSearchClick()
                onCloseDialog()
            }) { Text(text = stringResource(R.string.search)) }
        })

}
//endregion
