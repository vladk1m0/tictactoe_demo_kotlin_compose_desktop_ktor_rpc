package demo.tictactoe.api

import kotlinx.coroutines.flow.Flow
import kotlinx.rpc.RemoteService
import kotlinx.rpc.annotations.Rpc
import kotlinx.serialization.Serializable

/**
 * Enum representing the player symbols in the game.
 *
 * @property internalName The internal name of the player symbol.
 */
@Serializable
enum class PlayerSymbol(val internalName: String) {
    EMPTY(""), O("O"), X("X");

    override fun toString(): String {
        return internalName
    }
}

/**
 * Enum representing the status of the game.
 */
@Serializable
enum class GameStatus {
    INIT, PLAYING, FINISH
}

/**
 * Data class representing the state of the game.
 *
 * @property status The current status of the game.
 * @property playersInGame A map of players and their presence in the game.
 * @property currentPlayer The current player's symbol.
 * @property winner The winning player's symbol.
 * @property board The current state of the game board.
 */
@Serializable
data class GameState(
    var status: GameStatus = GameStatus.INIT,
    val playersInGame: Map<PlayerSymbol, Boolean> = mapOf(
        PlayerSymbol.O to false, PlayerSymbol.X to false
    ),
    val currentPlayer: PlayerSymbol = PlayerSymbol.X,
    val winner: PlayerSymbol = PlayerSymbol.EMPTY,
    val board: List<PlayerSymbol> = List(9) { PlayerSymbol.EMPTY }
)

/**
 * Data class representing a player's action in the game.
 *
 * @property position The position on the board where the action is performed.
 * @property player The player symbol performing the action.
 */
@Serializable
data class PlayerAction(
    val position: Int, val player: PlayerSymbol
)

/**
 * Interface defining the game service methods.
 */
@Rpc
interface GameService : RemoteService {
    /**
     * A ping method to check if the service is alive.
     */
    suspend fun ping()

    /**
     * Creates a new game with the given game ID.
     *
     * @param gameId The ID of the game to be created.
     * @return The initial state of the created game.
     */
    suspend fun createGame(gameId: String): GameState

    /**
     * Allows a player to join an existing game.
     *
     * @param gameId The ID of the game to join.
     * @param player The player symbol (X or O) to join the game.
     * @return The updated game state after the player has joined.
     */
    suspend fun joinGame(gameId: String, player: PlayerSymbol): GameState

    /**
     * Allows a player to leave an existing game.
     *
     * @param gameId The ID of the game to leave.
     * @param player The player symbol (X or O) to leave the game.
     * @return The updated game state after the player has left.
     */
    suspend fun leaveGame(gameId: String, player: PlayerSymbol): GameState

    /**
     * Submits a player's action to the game.
     *
     * @param gameId The ID of the game.
     * @param action The action to be submitted.
     * @return The updated game state after the action has been submitted.
     */
    suspend fun submitAction(gameId: String, action: PlayerAction): GameState

    /**
     * Retrieves the flow of game states for a given game ID.
     *
     * @param gameId The ID of the game.
     * @return A flow of game states.
     */
    suspend fun getGameStateFlow(gameId: String): Flow<GameState>
}