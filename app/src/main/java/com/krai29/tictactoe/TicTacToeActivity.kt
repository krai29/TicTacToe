package com.krai29.tictactoe

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.google.android.material.button.MaterialButton
import com.krai29.tictactoe.databinding.ActivityMainBinding

class TicTacToeActivity : AppCompatActivity(),View.OnClickListener {

    private var player = true
    private var turnCount = 0
    private var boardStatus = Array(3){IntArray(3)}

    private lateinit var board : Array<Array<MaterialButton>>
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initArray()
        for (i in board){
            for (button in i){
                button.setOnClickListener(this)
            }
        }

        initializeBoardStatus()

        binding.resetBtn.setOnClickListener {
            player = true
            turnCount = 0
            initializeBoardStatus()
            updateDisplay("Player X turn")
        }
    }

    private fun initializeBoardStatus() {
        for (i in 0..2){
            for (j in 0..2){
                boardStatus[i][j] = -1
                board[i][j].isEnabled = true
                board[i][j].text = ""
            }
        }
    }

    private fun initArray() {
        board = arrayOf(
            arrayOf(binding.button1,binding.button2,binding.button3),
            arrayOf(binding.button4,binding.button5,binding.button6),
            arrayOf(binding.button7,binding.button8,binding.button9)
        )
    }

    override fun onClick(v: View) {
        when(v.id){
            R.id.button1 -> {
                updateValue(row = 0,col = 0,player = player)
            }
            R.id.button2 -> {
                updateValue(row = 0,col = 1,player = player)
            }
            R.id.button3 -> {
                updateValue(row = 0,col = 2,player = player)
            }
            R.id.button4 -> {
                updateValue(row = 1,col = 0,player = player)
            }
            R.id.button5 -> {
                updateValue(row = 1,col = 1,player = player)
            }
            R.id.button6 -> {
                updateValue(row = 1,col = 2,player = player)
            }
            R.id.button7 -> {
                updateValue(row = 2,col = 0,player = player)
            }
            R.id.button8 -> {
                updateValue(row = 2,col = 1,player = player)
            }
            R.id.button9 -> {
                updateValue(row = 2,col = 2,player = player)
            }

        }
        turnCount++
        player = !player
        if (player){
            updateDisplay(getString(R.string.player_x_turn))
        }else{
            updateDisplay(getString(R.string.player_o_turn))
        }
        if (turnCount == 9){
            updateDisplay(getString(R.string.draw_message))
        }
        checkWinner()
    }

    private fun checkWinner() {
        // Rows
        for (i in 0..2){
            if (boardStatus[i][0] == boardStatus[i][1] && boardStatus[i][0] == boardStatus[i][2]){
                if (boardStatus[i][0] == 1){
                    updateDisplay(getString(R.string.player_x_won_message))
                    break
                }else if(boardStatus[i][0]==0){
                    updateDisplay(getString(R.string.player_o_won))
                    break
                }
            }
        }

        // Columns
        for (i in 0..2){
            if (boardStatus[0][i] == boardStatus[1][i] && boardStatus[0][i] == boardStatus[2][i]){
                if (boardStatus[0][i]==1){
                    updateDisplay(getString(R.string.player_x_won_message))
                    break
                }else if(boardStatus[0][i]==0){
                    updateDisplay(getString(R.string.player_o_won))
                    break
                }
            }
        }

        //Diagonals
        if (boardStatus[0][0] == boardStatus[1][1] && boardStatus[0][0] == boardStatus[2][2]){
            if (boardStatus[0][0]==1){
                updateDisplay(getString(R.string.player_x_won_message))
            }else if(boardStatus[0][0]==0){
                updateDisplay(getString(R.string.player_o_won))
            }
        }

        if (boardStatus[0][2] == boardStatus[1][1] && boardStatus[0][2] == boardStatus[2][0]){
            if (boardStatus[0][2]==1){
                updateDisplay(getString(R.string.player_x_won_message))
            }else if(boardStatus[0][2]==0){
                updateDisplay(getString(R.string.player_o_won))
            }
        }

    }

    private fun updateDisplay(gameMessage: String) {
        binding.displayTv.text = gameMessage
        if (gameMessage.contains("won")){
            disableButtons()
        }
    }

    private fun disableButtons() {
        for (i in board){
            for (j in i){
                j.isEnabled = false
            }
        }
    }

    private fun updateValue(row: Int, col: Int, player: Boolean) {
        val text = if (player) "X" else "O"
        val value = if (player)  1 else  0
        board[row][col].apply {
            isEnabled = false
            setText(text)
        }
        boardStatus[row][col] = value

    }
}