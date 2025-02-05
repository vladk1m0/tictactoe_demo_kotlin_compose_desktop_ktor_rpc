package demo.tictactoe.client

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPosition
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import demo.tictactoe.client.screen.GameScreen
import java.awt.Dimension

/**
 * Minimum height of the window.
 */
const val MIN_HEIGHT = 600

/**
 * Minimum width of the window.
 */
const val MIN_WIDTH = 400

/**
 * Main entry point of the application.
 */
fun main() = application {
    val windowState = rememberWindowState(
        position = WindowPosition(Alignment.Center),
        size = DpSize(MIN_WIDTH.dp, MIN_HEIGHT.dp)
    )

    Window(
        title = "TicTacToe",
        onCloseRequest = ::exitApplication,
        state = windowState
    ) {
        LaunchedEffect(Unit) {
            window.minimumSize = Dimension(MIN_WIDTH, MIN_HEIGHT)
            window.maximumSize = Dimension(MIN_WIDTH, MIN_HEIGHT)
        }
        GameScreen()
    }
}
