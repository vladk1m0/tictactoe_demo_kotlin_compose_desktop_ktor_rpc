package demo.tictactoe.client.view

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicText
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import demo.tictactoe.api.GameState
import demo.tictactoe.api.PlayerSymbol
import demo.tictactoe.client.widget.ErrorDialog
import demo.tictactoe.client.widget.InputField
import kotlinx.rpc.krpc.streamScoped
import kotlin.system.exitProcess

@Composable
@Preview
fun GamePlayView(
    localPlayer: PlayerSymbol,
    gameId: String,
    isError: Boolean,
    errorMessage: String,
    statusDescription: String,
    onLeaveGame: () -> Unit,
    onResetError: () -> Unit,
    onLocalMove: (Int) -> Unit,
    onRemoteMove: (GameState) -> Unit,
    isGameFieldEnabled: (Int) -> Boolean,
    gameFieldValue: (Int) -> String
) {
    LaunchedEffect(Unit) {
        streamScoped {
            gameServiceClient?.getGameStateFlow(gameId)?.collect { state -> onRemoteMove(state) }
        }
    }

    Column(modifier = Modifier.padding(16.dp)) {
        InputField(
            label = "Game ID",
            value = gameId,
            readOnly = true,
            isError = false,
            onValueChange = {}
        )
        InputField(
            label = "Current player",
            value = localPlayer.name,
            readOnly = true,
            isError = false,
            onValueChange = {}
        )
        Spacer(Modifier.height(15.dp))
        GameBoard(
            onLocalMove = onLocalMove,
            isGameFieldEnabled = isGameFieldEnabled,
            gameFieldValue = gameFieldValue
        )
        Spacer(Modifier.height(16.dp))
        StatusText(
            statusDescription
        )
        Spacer(Modifier.height(16.dp))
        ActionPanel(
            onLeaveGame = onLeaveGame
        )
        if (isError) {
            ErrorDialog(errorMsg = errorMessage, onOkPressed = onResetError)
        }
    }
}

/**
 * A composable function that displays the game board.
 *
 * @param onLocalMove A callback function that is called when a local move is made.
 * @param isGameFieldEnabled A function that determines if a game field is enabled.
 * @param gameFieldValue A function that returns the value of a game field.
 */
@Composable
private fun GameBoard(
    onLocalMove: (Int) -> Unit,
    isGameFieldEnabled: (Int) -> Boolean,
    gameFieldValue: (Int) -> String
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.width(380.dp)) {
        for (i in 0..2) {
            Row(horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically) {
                for (j in 0..2) {
                    val index = 3 * i + j
                    Spacer(modifier = Modifier.width(4.dp))
                    Button(
                        enabled = isGameFieldEnabled(index),
                        colors = ButtonDefaults.buttonColors(backgroundColor = Color.White, contentColor = Color.Red),
                        onClick = { onLocalMove(index) },
                        modifier = Modifier.size(80.dp)
                    ) { BasicText(text = gameFieldValue(index)) }
                }
            }
            Spacer(modifier = Modifier.height(4.dp))
        }
    }
}

/**
 * A composable function that displays the status text.
 *
 * @param statusDescription The status description to display.
 */
@Composable
private fun StatusText(statusDescription: String) {
    Column(modifier = Modifier.padding(5.dp)) {
        Text(
            text = "Status: $statusDescription",
            style = MaterialTheme.typography.labelLarge,
            color = Color.Red
        )
    }
}

/**
 * A composable function that displays the action buttons.
 *
 * @param onLeaveGame A callback function that is called when the game is left.
 */
@Composable
private fun ActionPanel(onLeaveGame: () -> Unit) {
    Row {
        FilledTonalButton(onClick = onLeaveGame) { Text("Leave") }
        Spacer(modifier = Modifier.width(4.dp))
        FilledTonalButton(onClick = { exitProcess(0) }) { Text("Exit") }
    }
}
