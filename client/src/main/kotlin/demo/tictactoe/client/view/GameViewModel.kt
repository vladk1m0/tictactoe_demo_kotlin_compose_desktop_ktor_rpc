package demo.tictactoe.client.view

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import demo.tictactoe.api.*
import demo.tictactoe.client.data.newGameServiceClient
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

var gameServiceClient: GameService? = null

data class GameUiState(
    val host: String = "localhost",
    val port: Int = 8080,
    val gameId: String = "",
    val localPlayer: PlayerSymbol = PlayerSymbol.X,
    val gameState: GameState? = null,
    val statusDescription: String = "",
    val canMakeMove: Boolean = false,
    val isError: Boolean = false,
    val errorMessage: String = ""
)

class GameViewModel : ViewModel() {

    var uiState by mutableStateOf(GameUiState())
        private set

    suspend fun connectServer(host: String, port: Int) {
        viewModelScope.async {
            if (gameServiceClient == null) {
                gameServiceClient = newGameServiceClient(host, port)
            }
            try {
                gameServiceClient?.ping()
            } catch (_: Exception) {
                gameServiceClient = newGameServiceClient(host, port)
            }
        }.await()
    }

    fun onHostChange(newHost: String) {
        uiState = uiState.copy(host = newHost)
    }

    fun onPortChange(newPort: String) {
        uiState = uiState.copy(port = newPort.toInt())
    }

    fun onLocalPlayerChange(newLocalPlayer: String) {
        uiState = uiState.copy(localPlayer = PlayerSymbol.valueOf(newLocalPlayer))
    }

    fun onGameIdChange(newGameId: String) {
        uiState = uiState.copy(gameId = newGameId)
    }

    @OptIn(ExperimentalUuidApi::class)
    fun onStartGame() {
        viewModelScope.launch {
            try {
                connectServer(uiState.host, uiState.port)
                val gameId = Uuid.random().toString().substring(0..7)
                gameServiceClient?.createGame(gameId)
                val gameState = gameServiceClient?.joinGame(gameId, uiState.localPlayer)
                uiState = uiState.copy(gameId = gameId, gameState = gameState)
            } catch (ex: Exception) {
                uiState = uiState.copy(isError = true, errorMessage = ex.message ?: "unknown error")
            }
        }
    }

    @OptIn(ExperimentalUuidApi::class)
    fun onJoinGame() {
        viewModelScope.launch {
            try {
                connectServer(uiState.host, uiState.port)
                val gameState = gameServiceClient?.joinGame(uiState.gameId, uiState.localPlayer)
                uiState = uiState.copy(gameState = gameState)
            } catch (ex: Exception) {
                uiState = uiState.copy(isError = true, errorMessage = ex.localizedMessage)
            }
        }
    }

    @OptIn(ExperimentalUuidApi::class)
    fun onLeaveGame() {
        viewModelScope.launch {
            try {
                uiState = uiState.copy(gameState = null)
                connectServer(uiState.host, uiState.port)
                gameServiceClient?.leaveGame(uiState.gameId, uiState.localPlayer)
                gameServiceClient = null
            } catch (ex: Exception) {
                uiState = uiState.copy(gameState = null, isError = true, errorMessage = ex.localizedMessage)
            }
        }
    }

    @OptIn(ExperimentalUuidApi::class)
    fun onResetError() {
        uiState = uiState.copy(isError = false, errorMessage = "")
    }

    @OptIn(ExperimentalUuidApi::class)
    fun onLocalMove(index: Int) {
        viewModelScope.launch {
            try {
                connectServer(uiState.host, uiState.port)
                val gameState =
                    gameServiceClient?.submitAction(uiState.gameId, PlayerAction(index, uiState.localPlayer))
                updateLocalState(gameState)
                uiState = uiState.copy(gameState = gameState)
            } catch (ex: Exception) {
                uiState = uiState.copy(isError = true, errorMessage = ex.localizedMessage)
            }
        }
    }

    @OptIn(ExperimentalUuidApi::class)
    fun onRemoteMove(remoteGateState: GameState) {
        viewModelScope.launch {
            try {
                updateLocalState(remoteGateState)
                uiState = uiState.copy(gameState = remoteGateState)
            } catch (ex: Exception) {
                uiState = uiState.copy(isError = true, errorMessage = ex.localizedMessage)
            }
        }
    }

    fun updateLocalState(gameState: GameState?) {
        var statusDescription = ""
        var canMakeMove = true

        when (gameState?.status) {
            GameStatus.INIT -> {
                if (gameState.playersInGame[PlayerSymbol.X] == false) {
                    statusDescription = "waiting until join player ${PlayerSymbol.X.name}"
                    canMakeMove = false
                }
                if (gameState.playersInGame[PlayerSymbol.O] == false) {
                    statusDescription = "waiting until join player ${PlayerSymbol.O.name}"
                    canMakeMove = false
                }
            }

            GameStatus.PLAYING -> {
                if (uiState.localPlayer == gameState.currentPlayer) {
                    statusDescription = "make your next move"
                } else {
                    statusDescription = "waiting for the ${gameState.currentPlayer.name} player next move"
                    canMakeMove = false
                }
            }

            GameStatus.FINISH -> {
                canMakeMove = false
                statusDescription = if (gameState.winner != PlayerSymbol.EMPTY) {
                    "player ${gameState.winner.name} won the game!"
                } else {
                    "game is terminated"
                }
            }

            else -> canMakeMove = false
        }

        uiState = uiState.copy(statusDescription = statusDescription, canMakeMove = canMakeMove)
    }

    fun isGameFieldEnabled(index: Int): Boolean {
        return uiState.canMakeMove && uiState.gameState?.board[index]?.internalName == ""
    }

    fun gameFieldValue(index: Int): String {
        return uiState.gameState?.board[index]?.internalName ?: ""
    }
}
