package demo.tictactoe.client.view

import demo.tictactoe.api.GameState
import demo.tictactoe.api.GameStatus
import demo.tictactoe.api.PlayerSymbol
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

@ExperimentalCoroutinesApi
class GameViewModelTest {

    private lateinit var viewModel: GameViewModel
    private val testDispatcher = StandardTestDispatcher()

    @BeforeEach
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        viewModel = GameViewModel()
    }

    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `when game status is INIT and both players are not joined then status description is waiting to join player`() {
        val gameState = GameState(
            status = GameStatus.INIT,
            playersInGame = mapOf(PlayerSymbol.X to false, PlayerSymbol.O to false),
            currentPlayer = PlayerSymbol.X,
            board = List(9) { PlayerSymbol.EMPTY },
            winner = PlayerSymbol.EMPTY
        )

        viewModel.updateLocalState(gameState)

        assertEquals("waiting until join player O", viewModel.uiState.statusDescription)
        assertEquals(false, viewModel.uiState.canMakeMove)
    }

    @Test
    fun `when game status is INIT and only one player is joined then status description is waiting to join the other player`() {
        val gameState = GameState(
            status = GameStatus.INIT,
            playersInGame = mapOf(PlayerSymbol.X to true, PlayerSymbol.O to false),
            currentPlayer = PlayerSymbol.X,
            board = List(9) { PlayerSymbol.EMPTY },
            winner = PlayerSymbol.EMPTY
        )

        viewModel.updateLocalState(gameState)

        assertEquals("waiting until join player O", viewModel.uiState.statusDescription)
        assertEquals(false, viewModel.uiState.canMakeMove)
    }

    @Test
    fun `when game status is PLAYING and it is local player's turn then status description is make your next move`() {
        val gameState = GameState(
            status = GameStatus.PLAYING,
            playersInGame = mapOf(PlayerSymbol.X to true, PlayerSymbol.O to true),
            currentPlayer = PlayerSymbol.X,
            board = List(9) { PlayerSymbol.EMPTY },
            winner = PlayerSymbol.EMPTY
        )

        viewModel.onLocalPlayerChange(PlayerSymbol.X.name)
        viewModel.updateLocalState(gameState)

        assertEquals("make your next move", viewModel.uiState.statusDescription)
        assertEquals(true, viewModel.uiState.canMakeMove)
    }

    @Test
    fun `when game status is PLAYING and it is not local player's turn then status description is waiting for the other player's move`() {
        val gameState = GameState(
            status = GameStatus.PLAYING,
            playersInGame = mapOf(PlayerSymbol.X to true, PlayerSymbol.O to true),
            currentPlayer = PlayerSymbol.O,
            board = List(9) { PlayerSymbol.EMPTY },
            winner = PlayerSymbol.EMPTY
        )

        viewModel.onLocalPlayerChange(PlayerSymbol.X.name)
        viewModel.updateLocalState(gameState)

        assertEquals("waiting for the O player next move", viewModel.uiState.statusDescription)
        assertEquals(false, viewModel.uiState.canMakeMove)
    }

    @Test
    fun `when game status is FINISH and there is a winner then status description is player won the game`() {
        val gameState = GameState(
            status = GameStatus.FINISH,
            playersInGame = mapOf(PlayerSymbol.X to true, PlayerSymbol.O to true),
            currentPlayer = PlayerSymbol.X,
            board = List(9) { PlayerSymbol.EMPTY },
            winner = PlayerSymbol.X
        )

        viewModel.updateLocalState(gameState)

        assertEquals("player X won the game!", viewModel.uiState.statusDescription)
        assertEquals(false, viewModel.uiState.canMakeMove)
    }

    @Test
    fun `when game status is FINISH and there is no winner then status description is game is terminated`() {
        val gameState = GameState(
            status = GameStatus.FINISH,
            playersInGame = mapOf(PlayerSymbol.X to true, PlayerSymbol.O to true),
            currentPlayer = PlayerSymbol.X,
            board = listOf(PlayerSymbol.EMPTY, PlayerSymbol.EMPTY, PlayerSymbol.EMPTY),
            winner = PlayerSymbol.EMPTY
        )

        viewModel.updateLocalState(gameState)

        assertEquals("game is terminated", viewModel.uiState.statusDescription)
        assertEquals(false, viewModel.uiState.canMakeMove)
    }
}