package demo.tictactoe.client.widget

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.width
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * A composable function that displays an input field.
 *
 * @param label The label for the input field.
 * @param value The current value of the input field.
 * @param onValueChange A callback function that is called when the value changes.
 * @param isError Whether the input field is in an error state.
 */
@Composable
fun InputField(
    label: String,
    value: String,
    readOnly: Boolean,
    isError: Boolean,
    onValueChange: (String) -> Unit
) {
    Column {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
        )
        OutlinedTextField(
            modifier = Modifier.width(350.dp),
            value = value,
            onValueChange = onValueChange,
            readOnly = readOnly,
            isError = isError
        )
    }
}