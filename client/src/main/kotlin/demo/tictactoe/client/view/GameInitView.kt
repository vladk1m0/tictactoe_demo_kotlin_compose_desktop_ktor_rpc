package demo.tictactoe.client.view

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import demo.tictactoe.client.widget.DropdownList
import demo.tictactoe.client.widget.ErrorDialog
import demo.tictactoe.api.PlayerSymbol
import demo.tictactoe.client.widget.InputField
import kotlin.system.exitProcess
import kotlin.uuid.ExperimentalUuidApi

/**
 * A composable function that displays the game start view.
 *
 * @param host The server hostname.
 * @param port The server port.
 * @param localPlayer The local player symbol.
 * @param gameId The game ID.
 * @param onHostChange A callback function that is called when the host changes.
 * @param onPortChange A callback function that is called when the port changes.
 * @param onLocalPlayerChange A callback function that is called when the local player changes.
 * @param onGameIdChange A callback function that is called when the game ID changes.
 * @param onStartGame A callback function that is called when the game is started.
 * @param onJoinGame A callback function that is called when the game is joined.
 * @param onResetError A callback function that is called when the error is reset.
 */
@OptIn(ExperimentalUuidApi::class)
@Composable
@Preview
fun GameInitView(
    host: String,
    port: Int,
    localPlayer: PlayerSymbol,
    gameId: String,
    isError: Boolean,
    errorMessage: String,
    onHostChange: (String) -> Unit,
    onPortChange: (String) -> Unit,
    onLocalPlayerChange: (String) -> Unit,
    onGameIdChange: (String) -> Unit,
    onStartGame: () -> Unit,
    onJoinGame: () -> Unit,
    onResetError: () -> Unit
) {
    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        InputField(
            label = "Server hostname",
            value = host,
            readOnly = false,
            isError = host.isEmpty(),
            onValueChange = onHostChange
        )
        Spacer(Modifier.height(15.dp))

        InputField(
            label = "Server port",
            value = port.toString(),
            readOnly = false,
            isError = port.toString().isEmpty(),
            onValueChange = onPortChange,
        )
        Spacer(Modifier.height(15.dp))

        DropdownField(
            label = "Player type",
            selectedOption = localPlayer.toString(),
            onSelectionChanged = onLocalPlayerChange
        )
        Spacer(Modifier.height(15.dp))

        ActionPanel(
            gameId = gameId,
            onGameIdChange = onGameIdChange,
            onStartGame = onStartGame,
            onJoinGame = onJoinGame
        )

        if (isError) {
            ErrorDialog(
                errorMsg = errorMessage,
                onOkPressed = onResetError
            )
        }
    }
}

/**
 * A composable function that displays a dropdown field.
 *
 * @param label The label for the dropdown field.
 * @param selectedOption The currently selected option.
 * @param onSelectionChanged A callback function that is called when the selection changes.
 */
@Composable
private fun DropdownField(
    label: String,
    selectedOption: String,
    onSelectionChanged: (String) -> Unit
) {
    Column {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
        )
        DropdownList(
            onSelectionChanged = onSelectionChanged,
            selectedOption = selectedOption
        )
    }
}

/**
 * A composable function that displays the game ID field.
 *
 * @param gameId The current game ID.
 * @param onGameIdChange A callback function that is called when the game ID changes.
 * @param onStartGame A callback function that is called when the game is started.
 * @param onJoinGame A callback function that is called when the game is joined.
 */
@Composable
private fun ActionPanel(
    gameId: String,
    onGameIdChange: (String) -> Unit,
    onStartGame: () -> Unit,
    onJoinGame: () -> Unit
) {
    Column {
        Text(
            text = "Game ID",
            style = MaterialTheme.typography.labelSmall,
        )
        OutlinedTextField(
            value = gameId,
            onValueChange = onGameIdChange,
            modifier = Modifier.width(350.dp)
        )
        Row(
            horizontalArrangement = Arrangement.Start
        ) {
            FilledTonalButton(
                enabled = gameId.isBlank(),
                onClick = onStartGame
            ) {
                Text("Start game")
            }
            FilledTonalButton(
                enabled = gameId.isNotBlank(),
                onClick = onJoinGame
            ) {
                Text("Join game")
            }
            ExitButton()
        }
    }
}

/**
 * A composable function that displays the exit button.
 */
@Composable
private fun ExitButton() {
    FilledTonalButton(onClick = { exitProcess(0) }) {
        Text("Exit")
    }
}