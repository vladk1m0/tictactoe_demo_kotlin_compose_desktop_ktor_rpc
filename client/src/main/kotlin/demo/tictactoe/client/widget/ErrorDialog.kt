package demo.tictactoe.client.widget

import androidx.compose.material.AlertDialog
import androidx.compose.material.Text
import androidx.compose.material3.Button
import androidx.compose.runtime.Composable

/**
 * A composable function that displays an error dialog.
 *
 * @param errorMsg The error message to display.
 * @param onOkPressed A callback function that is called when the "Ok" button is pressed.
 */
@Composable
fun ErrorDialog(
    errorMsg: String,
    onOkPressed: () -> Unit = {}
) {
    AlertDialog(
        onDismissRequest = {},
        title = {
            Text("Error")
        },
        text = {
            Text(errorMsg)
        },
        confirmButton = {
            Button(onClick = onOkPressed) {
                Text("Ok")
            }
        }
    )
}
