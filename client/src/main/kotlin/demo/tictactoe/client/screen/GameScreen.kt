package demo.tictactoe.client.screen

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import demo.tictactoe.client.view.GamePlayView
import demo.tictactoe.client.view.GameInitView
import demo.tictactoe.client.view.GameViewModel

/**
 * A composable function that displays the game screen.
 *
 * @param viewModel The view model for the game screen.
 */
@Composable
@Preview
fun GameScreen(
    viewModel: GameViewModel = viewModel()
) {
    val uiState = viewModel.uiState

    if (uiState.gameState == null) {
        GameInitView(
            host = uiState.host,
            port = uiState.port,
            localPlayer = uiState.localPlayer,
            gameId = uiState.gameId,
            isError = uiState.isError,
            errorMessage = uiState.errorMessage,
            onHostChange = viewModel::onHostChange,
            onPortChange = viewModel::onPortChange,
            onLocalPlayerChange = viewModel::onLocalPlayerChange,
            onGameIdChange = viewModel::onGameIdChange,
            onStartGame = viewModel::onStartGame,
            onJoinGame = viewModel::onJoinGame,
            onResetError = viewModel::onResetError,
        )
    } else {
        GamePlayView(
            localPlayer = uiState.localPlayer,
            gameId = uiState.gameId,
            isError = uiState.isError,
            errorMessage = uiState.errorMessage,
            statusDescription = uiState.statusDescription,
            onLeaveGame = viewModel::onLeaveGame,
            onResetError = viewModel::onResetError,
            onLocalMove = viewModel::onLocalMove,
            onRemoteMove = viewModel::onRemoteMove,
            isGameFieldEnabled = viewModel::isGameFieldEnabled,
            gameFieldValue = viewModel::gameFieldValue
        )
    }
}
