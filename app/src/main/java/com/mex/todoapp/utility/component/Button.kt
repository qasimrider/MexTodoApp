package com.mex.todoapp.utility.component

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp

@Composable
fun MexTodoButton(
    modifier: Modifier = Modifier,
    @StringRes textId: Int,
    enabled: Boolean = true,
    onClicked: () -> Unit
) {
    Button(
        enabled = enabled,
        modifier = modifier.fillMaxWidth().padding(horizontal = 8.dp),
        onClick = { onClicked() })
    { Text(stringResource(id = textId)) }
}