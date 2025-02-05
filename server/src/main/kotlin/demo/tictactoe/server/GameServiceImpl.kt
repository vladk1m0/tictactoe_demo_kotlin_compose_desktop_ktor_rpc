package demo.tictactoe.server

import demo.tictactoe.api.*
import kotlin.coroutines.CoroutineContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

/** A map to store the state of each game. */
private val games = mutableMapOf<String, MutableStateFlow<GameState>>()

/**
 * Implementation of the GameService interface.
 *
 * @param coroutineContext The coroutine context to be used for asynchronous operations.
 */
class GameServiceImpl(override val coroutineContext: CoroutineContext) : GameService {

    /** A ping method to check if the service is alive. */
    override suspend fun ping() {}

    /**
     * Creates a new game with the given game ID.
     *
     * @param gameId The ID of the game to be created.
     * @return The initial state of the created game.
     * @throws IllegalArgumentException if a game with the given ID already exists.
     */
    override suspend fun createGame(gameId: String): GameState {
        require(games[gameId] == null) { "Game [id=${gameId}] already exists" }
        val game = GameState()
        games[gameId] = MutableStateFlow(game)
        return game
    }

    /**
     * Allows a player to join an existing game.
     *
     * @param gameId The ID of the game to join.
     * @param player The player symbol (X or O) to join the game.
     * @return The updated game state after the player has joined.
     * @throws IllegalArgumentException if the game does not exist or if the player cannot join a
     * finished game.
     */
    override suspend fun joinGame(gameId: String, player: PlayerSymbol): GameState {
        val gameFlow =
            games[gameId] ?: throw IllegalArgumentException("Game [id=$gameId] not found")
        if (gameFlow.value.playersInGame[player] == true)
            throw IllegalStateException("Player ${player.name} already in game [id=$gameId]")
        gameFlow.update { game ->
            require(game.status != GameStatus.FINISH) {
                "Player ${player.name} cannot join to game [id=$gameId, status=${game.status.name}]"
            }
            val playersInGame = game.playersInGame + (player to true)
            game.copy(
                status = updateGameStatus(playersInGame, game.status),
                playersInGame = playersInGame
            )
        }
        return gameFlow.value
    }

    /**
     * Allows a player to leave an existing game.
     *
     * @param gameId The ID of the game to leave.
     * @param player The player symbol (X or O) to leave the game.
     * @return The updated game state after the player has left.
     * @throws IllegalArgumentException if the game does not exist.
     */
    override suspend fun leaveGame(gameId: String, player: PlayerSymbol): GameState {
        val gameFlow =
            games[gameId] ?: throw IllegalArgumentException("Game [id=${gameId}] not found")
        gameFlow.update { game ->
            val playersInGame =
                game.playersInGame.toMutableMap().also { it[player] = false }.toMap()
            val status = updateGameStatus(playersInGame, game.status)
            game.copy(
                status = status,
                playersInGame = playersInGame
            )
        }
        return gameFlow.value
    }

    /**
     * Submits a player's action to the game.
     *
     * @param gameId The ID of the game.
     * @param action The action to be submitted.
     * @return The updated game state after the action has been submitted.
     * @throws IllegalArgumentException if the game does not exist.
     */
    override suspend fun submitAction(gameId: String, action: PlayerAction): GameState {
        val gameFlow =
            games[gameId] ?: throw IllegalArgumentException("Game [id=${gameId}] not found")
        gameFlow.update { game ->
            if (game.status == GameStatus.PLAYING &&
                game.board[action.position] == PlayerSymbol.EMPTY &&
                game.winner == PlayerSymbol.EMPTY
            ) {
                val updatedBoard =
                    game.board.toMutableList().also { it[action.position] = action.player }
                val winner = checkWinner(updatedBoard)
                game.copy(
                    status = if (winner != PlayerSymbol.EMPTY) GameStatus.FINISH else game.status,
                    board = updatedBoard,
                    currentPlayer =
                        if (game.currentPlayer == PlayerSymbol.X) PlayerSymbol.O
                        else PlayerSymbol.X,
                    winner = winner
                )
            } else {
                game
            }
        }
        return gameFlow.value
    }

    /**
     * Retrieves the flow of game states for a given game ID.
     *
     * @param gameId The ID of the game.
     * @return A flow of game states.
     * @throws IllegalArgumentException if the game does not exist.
     */
    override suspend fun getGameStateFlow(gameId: String): Flow<GameState> {
        return games[gameId]?.asStateFlow()
            ?: throw IllegalArgumentException("Game [id=${gameId}] not found")
    }

    /**
     * Checks if there is a winner in the current game board.
     *
     * @param board The current game board.
     * @return The winning player symbol if there is a winner, otherwise PlayerSymbol.EMPTY.
     */
    private fun checkWinner(board: List<PlayerSymbol>): PlayerSymbol {
        val winPatterns =
            listOf(
                listOf(0, 1, 2),
                listOf(3, 4, 5),
                listOf(6, 7, 8),
                listOf(0, 3, 6),
                listOf(1, 4, 7),
                listOf(2, 5, 8),
                listOf(0, 4, 8),
                listOf(2, 4, 6)
            )

        for (pattern in winPatterns) {
            if (board[pattern[0]] == board[pattern[1]] && board[pattern[1]] == board[pattern[2]]) {
                return board[pattern[0]]
            }
        }
        return PlayerSymbol.EMPTY
    }

    /**
     * Updates the game status based on the current players in the game.
     *
     * @param playersInGame A map of players and their presence in the game.
     * @return The updated game status.
     */
    private fun updateGameStatus(playersInGame: Map<PlayerSymbol, Boolean>, status: GameStatus): GameStatus {
        return when {
            playersInGame[PlayerSymbol.X] == true
                    && playersInGame[PlayerSymbol.O] == true
                    && status == GameStatus.INIT ->
                GameStatus.PLAYING

            playersInGame[PlayerSymbol.X] == false
                    && playersInGame[PlayerSymbol.O] == false
                    && status == GameStatus.PLAYING ->
                GameStatus.FINISH

            else -> status
        }
    }
}
