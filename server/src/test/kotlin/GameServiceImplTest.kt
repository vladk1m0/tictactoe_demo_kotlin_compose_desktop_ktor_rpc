package demo.tictactoe.server

import demo.tictactoe.api.*
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import java.lang.IllegalStateException

@ExperimentalCoroutinesApi
class GameServiceImplTest {

    private val gameService =
        GameServiceImpl(coroutineContext = kotlinx.coroutines.Dispatchers.Unconfined)

    @Test
    fun `when creating a new game then game status should be INIT`() = runTest {
        val gameId = "game1"
        val gameState = gameService.createGame(gameId)
        // Assert that the game status is INIT
        assertEquals(GameStatus.INIT, gameState.status)
    }

    @Test
    fun `when creating a game that already exists then throw IllegalArgumentException`() = runTest {
        val gameId = "game2"
        gameService.createGame(gameId)
        // Assert that creating the same game again throws an IllegalArgumentException
        assertFailsWith<IllegalArgumentException> { gameService.createGame(gameId) }
    }

    @Test
    fun `when joining a game then player should be added to the game`() = runTest {
        val gameId = "game3.1"
        gameService.createGame(gameId)
        val gameState = gameService.joinGame(gameId, PlayerSymbol.X)
        // Assert that the game status is INIT and the player is added
        assertEquals(GameStatus.INIT, gameState.status)
        assertEquals(true, gameState.playersInGame[PlayerSymbol.X])
    }

    @Test
    fun `when joining a game both players X and Y then game status changing to PLAING`() = runTest {
        val gameId = "game3.3"
        gameService.createGame(gameId)
        var gameState = gameService.joinGame(gameId, PlayerSymbol.X)
        gameState = gameService.joinGame(gameId, PlayerSymbol.O)
        // Assert that the game status is PLAYING and both players are in game
        assertEquals(GameStatus.PLAYING, gameState.status)
        assertEquals(true, gameState.playersInGame[PlayerSymbol.X])
        assertEquals(true, gameState.playersInGame[PlayerSymbol.O])
    }

    @Test
    fun `when joining a game that does not exist then throw IllegalArgumentException`() = runTest {
        val gameId = "game4"
        // Assert that joining a non-existent game throws an IllegalArgumentException
        assertFailsWith<IllegalArgumentException> { gameService.joinGame(gameId, PlayerSymbol.X) }
    }

    @Test
    fun `when joining a finished game then throw IllegalStateException`() = runTest {
        val gameId = "game5"
        gameService.createGame(gameId)
        gameService.joinGame(gameId, PlayerSymbol.X)
        gameService.joinGame(gameId, PlayerSymbol.O)
        gameService.submitAction(gameId, PlayerAction(0, PlayerSymbol.X))
        gameService.submitAction(gameId, PlayerAction(1, PlayerSymbol.X))
        gameService.submitAction(gameId, PlayerAction(2, PlayerSymbol.X))
        // Assert that joining a finished game throws an IllegalStateException
        assertFailsWith<IllegalStateException> { gameService.joinGame(gameId, PlayerSymbol.X) }
    }

    @Test
    fun `when leaving a game then player should be removed from the game`() = runTest {
        val gameId = "game6"
        gameService.createGame(gameId)
        gameService.joinGame(gameId, PlayerSymbol.X)
        val gameState = gameService.leaveGame(gameId, PlayerSymbol.X)
        // Assert that the game status is INIT and the player is removed
        assertEquals(GameStatus.INIT, gameState.status)
        assertEquals(false, gameState.playersInGame[PlayerSymbol.X])
    }

    @Test
    fun `when leaving a game that does not exist then throw IllegalArgumentException`() = runTest {
        val gameId = "game7"
        // Assert that leaving a non-existent game throws an IllegalArgumentException
        assertFailsWith<IllegalArgumentException> { gameService.leaveGame(gameId, PlayerSymbol.X) }
    }

    @Test
    fun `when submitting an action then game state should be updated`() = runTest {
        val gameId = "game8"
        gameService.createGame(gameId)
        gameService.joinGame(gameId, PlayerSymbol.X)
        gameService.joinGame(gameId, PlayerSymbol.O)
        val gameState = gameService.submitAction(gameId, PlayerAction(0, PlayerSymbol.X))
        // Assert that the game status is PLAYING and the board is updated
        assertEquals(GameStatus.PLAYING, gameState.status)
        assertEquals(PlayerSymbol.X, gameState.board[0])
    }

    @Test
    fun `when submitting an action to a game that does not exist then throw IllegalArgumentException`() = runTest {
        val gameId = "game9"
        // Assert that submitting an action to a non-existent game throws an IllegalArgumentException
        assertFailsWith<IllegalArgumentException> {
            gameService.submitAction(gameId, PlayerAction(0, PlayerSymbol.X))
        }
    }

    @Test
    fun `when getting game state flow for a game that does not exist then throw IllegalArgumentException`() = runTest {
        val gameId = "game10"
        // Assert that getting the game state flow for a non-existent game throws an IllegalArgumentException
        assertFailsWith<IllegalArgumentException> { gameService.getGameStateFlow(gameId) }
    }
}