package com.mex.todoapp.features.createTask

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.mex.todoapp.R
import com.mex.todoapp.data.model.toTask
import com.mex.todoapp.model.Category
import com.mex.todoapp.model.Priority
import com.mex.todoapp.model.TaskView
import com.mex.todoapp.mvi.UiState
import com.mex.todoapp.utility.ObserveUiState
import com.mex.todoapp.utility.component.MexTodoButton
import com.mex.todoapp.utility.convertMillisToDate
import com.mex.todoapp.utility.format

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateTaskScreen(navController: NavHostController) {

    val viewModel: CreateTaskViewModel = hiltViewModel()

    var title by remember { mutableStateOf("") }
    var dueDate by remember { mutableLongStateOf(0L) }
    var description by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf(Category.WORK) }
    var selectedPriority by remember { mutableStateOf(Priority.LOW) }
    var createTaskEnabled by remember { mutableStateOf(false) }

    viewModel.run {
        ObserveUiState {
            uiState.collect { createTaskUiState ->
                when (createTaskUiState) {
                    is UiState.Success -> {
                        navController.popBackStack()
                    }

                    UiState.Initialization -> {}
                    UiState.Loading -> {}
                }
            }
        }

        Box {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                Text(
                    text = stringResource(R.string.create_task),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )

                Spacer(modifier = Modifier.height(16.dp))

                TaskInputFields(
                    onTitleChange = { title = it },
                    onDueDateChange = { dueDate = it },
                    onDescriptionChange = { description = it }
                )

                Spacer(modifier = Modifier.height(16.dp))

                DropDownSelection(
                    selectedValue = selectedCategory,
                    values = Category.values().toList().dropLast(1),
                    label = stringResource(R.string.category),
                    onValueSelected = { category -> selectedCategory = category }
                )

                Spacer(modifier = Modifier.height(16.dp))

                DropDownSelection(
                    selectedValue = selectedPriority,
                    values = Priority.values().toList().dropLast(1),
                    label = stringResource(R.string.priority),
                    onValueSelected = { priority -> selectedPriority = priority }
                )

                Spacer(modifier = Modifier.height(32.dp))

            }

            createTaskEnabled = title.isNotEmpty() && dueDate != 0L && description.isNotEmpty()

            MexTodoButton(
                textId = R.string.create_task,
                modifier = Modifier.align(Alignment.BottomCenter),
                enabled = createTaskEnabled
            ) {

                dispatchIntent(
                    CreateTaskIntent.CreateTask(
                        TaskView(
                            title = title,
                            description = description,
                            dueDate = dueDate,
                            category = selectedCategory,
                            priority = selectedPriority
                        ).toTask()
                    )
                )
            }
        }
    }
}

//region Text Fields
@Composable
fun TaskInputFields(
    onTitleChange: (String) -> Unit,
    onDueDateChange: (Long) -> Unit,
    onDescriptionChange: (String) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var dueDate by remember { mutableLongStateOf(0L) }
    var description by remember { mutableStateOf("") }
    val maxChar = 50


    Column {
        TextField(
            value = title,
            onValueChange = {
                if (it.length < maxChar) {
                    title = it
                    onTitleChange(it)
                }
            },
            placeholder = { Text("Title") },
            modifier = Modifier.fillMaxWidth(),
            textStyle = TextStyle(fontSize = 18.sp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = description,
            onValueChange = {
                description = it
                onDescriptionChange(it)
            },
            placeholder = { Text("Description") },
            modifier = Modifier.fillMaxWidth(),
            textStyle = TextStyle(fontSize = 18.sp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        DueDatePicker {
            dueDate = it
            onDueDateChange(it)
        }

    }
}
//endregion

//region Generic Drop down
@ExperimentalMaterial3Api
@Composable
fun <T> DropDownSelection(
    selectedValue: T,
    values: List<T>,
    label: String,
    onValueSelected: (T) -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }


    Column {
        Text(
            text = label,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )

        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = selectedValue.toString(),
            onValueChange = {},
            readOnly = true,
            enabled = false,
            modifier = Modifier
                .clickable { expanded = true }
                .fillMaxWidth()
                .padding(8.dp),
            colors = OutlinedTextFieldDefaults.colors(
                disabledTextColor = MaterialTheme.colorScheme.onSurface,
                disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant,
            )
        )

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            values.forEach { value ->
                DropdownMenuItem(text = {
                    Text(text = value.toString())
                },
                    onClick = {
                        onValueSelected(value)
                        expanded = false
                    }
                )
            }
        }
    }
}
//endregion

//region Due Date
@Composable
fun DueDatePicker(selectedDate: (Long) -> Unit) {
    var date by remember { mutableStateOf("") }
    var openDialog by remember { mutableStateOf(false) }


    TextField(
        value = date,
        onValueChange = { date = it },
        readOnly = true,
        trailingIcon = {
            Icon(imageVector = Icons.Default.DateRange, contentDescription = null)
        },
        placeholder = { Text("Due Date") },
        enabled = false,
        modifier = Modifier
            .fillMaxWidth()
            .clickable { openDialog = true },
        colors = OutlinedTextFieldDefaults.colors(
            disabledTextColor = MaterialTheme.colorScheme.onSurface,
            disabledPlaceholderColor = MaterialTheme.colorScheme.onSurfaceVariant,
            disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant,
        )
    )

    if (openDialog) {
        DatePicker { dialogState, selectedDateMillis ->
            openDialog = dialogState
            selectedDateMillis?.let { dateInMillis ->
                date = convertMillisToDate(dateInMillis).format()
                selectedDate(dateInMillis)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePicker(onDialogInteraction: (Boolean, Long?) -> Unit) {

    val datePickerState = rememberDatePickerState()
    val confirmEnabled by remember { derivedStateOf { datePickerState.selectedDateMillis != null } }

    DatePickerDialog(
        onDismissRequest = { onDialogInteraction(false, null) },
        confirmButton = {
            TextButton(
                onClick = { onDialogInteraction(false, datePickerState.selectedDateMillis) },
                enabled = confirmEnabled
            ) { Text(text = stringResource(R.string.ok)) }
        },
        dismissButton = {
            TextButton(onClick = { onDialogInteraction(false, null) }) {
                Text(stringResource(R.string.cancel))
            }
        }
    ) { androidx.compose.material3.DatePicker(state = datePickerState) }
}
//endregion
