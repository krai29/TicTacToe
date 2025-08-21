package com.krai29.tictactoe

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

class TicTacToeViewModel : ViewModel() {

    var board by mutableStateOf(Array(3) { Array(3) { "" } })
        private set
    var currentPlayer by mutableStateOf(Player.X)
        private set
    var gameMessage by mutableStateOf("Player X's turn")
        private set
    var isGameOver by mutableStateOf(false)
        private set

    private var turnCount = 0

    fun onCellClicked(row: Int, col: Int) {
        if (board[row][col].isNotEmpty() || isGameOver) {
            return
        }

        val newBoard = board.map { it.clone() }.toTypedArray()
        newBoard[row][col] = currentPlayer.symbol
        board = newBoard
        turnCount++

        if (checkWin(row, col)) {
            gameMessage = "Player ${currentPlayer.symbol} won!"
            isGameOver = true
        } else if (turnCount == 9) {
            gameMessage = "It's a Draw!"
            isGameOver = true
        } else {
            currentPlayer = if (currentPlayer == Player.X) Player.O else Player.X
            gameMessage = "Player ${currentPlayer.symbol}'s turn"
        }
    }

    private fun checkWin(row: Int, col: Int): Boolean {
        val symbol = currentPlayer.symbol
        // Check row
        if (board[row].all { it == symbol }) return true
        // Check column
        if (board.all { it[col] == symbol }) return true
        // Check diagonals
        if (row == col && board.indices.all { board[it][it] == symbol }) return true
        if (row + col == 2 && board.indices.all { board[it][2 - it] == symbol }) return true
        return false
    }

    fun resetGame() {
        board = Array(3) { Array(3) { "" } }
        currentPlayer = Player.X
        gameMessage = "Player X's turn"
        isGameOver = false
        turnCount = 0
    }
}

enum class Player(val symbol: String) {
    X("X"),
    O("O")
}
