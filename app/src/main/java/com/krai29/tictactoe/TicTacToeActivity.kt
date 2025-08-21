package com.krai29.tictactoe

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel

class TicTacToeActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TicTacToeAppTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    TicTacToeScreen(viewModel = viewModel())
                }
            }
        }
    }
}

@Composable
fun TicTacToeScreen(viewModel: TicTacToeViewModel) {
    val isDarkTheme = isSystemInDarkTheme()
    val screenBackgroundColor = if (isDarkTheme) colorResource(id = R.color.board_area_background_dark) else colorResource(id = R.color.board_area_background_light)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(screenBackgroundColor)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = viewModel.gameMessage,
            fontSize = 24.sp,
            color = if (isDarkTheme) Color.White else Color.Black,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        BoardView(
            board = viewModel.board,
            onCellClick = { row, col -> viewModel.onCellClicked(row, col) },
            isEnabled = !viewModel.isGameOver,
            isDarkTheme = isDarkTheme
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = { viewModel.resetGame() },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = if (isDarkTheme) colorResource(id = R.color.purple_700) else colorResource(id = R.color.purple_500)
            )
        ) {
            Text(text = "Reset Game", fontSize = 18.sp, color = Color.White)
        }
    }
}

@Composable
fun BoardView(
    board: Array<Array<String>>,
    onCellClick: (Int, Int) -> Unit,
    isEnabled: Boolean,
    isDarkTheme: Boolean
) {
    val boardBackgroundColor = if (isDarkTheme) colorResource(id = R.color.board_area_background_dark) else colorResource(id = R.color.board_area_background_light)
    val gridLineColor = if (isDarkTheme) colorResource(id = R.color.grid_line_color_dark) else colorResource(id = R.color.grid_line_color_light)

    Box(modifier = Modifier.background(boardBackgroundColor)) { // Outer box for board background
        Column(
            modifier = Modifier
                .aspectRatio(1f)
                .padding(4.dp)
        ) {
            for (i in board.indices) {
                Row(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    for (j in board[i].indices) {
                        CellView(
                            text = board[i][j],
                            onClick = { onCellClick(i, j) },
                            isEnabled = isEnabled && board[i][j].isEmpty(),
                            isDarkTheme = isDarkTheme,
                            modifier = Modifier.weight(1f)
                        )
                        if (j < board[i].size - 1) { // Vertical grid line
                            Spacer(modifier = Modifier
                                .fillMaxHeight()
                                .width(1.dp)
                                .background(gridLineColor))
                        }
                    }
                }
                if (i < board.size - 1) { // Horizontal grid line
                    Spacer(modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                        .background(gridLineColor))
                }
            }
        }
    }
}

@Composable
fun CellView(
    text: String,
    onClick: () -> Unit,
    isEnabled: Boolean,
    isDarkTheme: Boolean,
    modifier: Modifier = Modifier
) {
    val isClicked = text.isNotEmpty()

    val cellBackgroundColor = when {
        isClicked && isDarkTheme -> colorResource(id = R.color.cell_clicked_background_dark)
        isClicked && !isDarkTheme -> colorResource(id = R.color.cell_clicked_background_light)
        !isClicked && isDarkTheme -> colorResource(id = R.color.cell_unclicked_background_dark)
        else -> colorResource(id = R.color.cell_unclicked_background_light)
    }

    val playerXColor = if (isDarkTheme) colorResource(id = R.color.player_x_color_dark) else colorResource(id = R.color.player_x_color_light)
    val playerOColor = if (isDarkTheme) colorResource(id = R.color.player_o_color_dark) else colorResource(id = R.color.player_o_color_light)
    val textColor = when (text) {
        "X" -> playerXColor
        "O" -> playerOColor
        else -> if (isDarkTheme) Color.White else Color.Black // Should not be visible if empty
    }

    Box(
        modifier = modifier
            .aspectRatio(1f)
            .background(cellBackgroundColor)
            .clip(MaterialTheme.shapes.small) // Optional: adds a slight rounding to cell corners
            .clickable(enabled = isEnabled, onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            fontSize = 48.sp, // Increased size for better visibility
            fontWeight = FontWeight.Bold,
            color = textColor
        )
    }
}

@Composable
fun TicTacToeAppTheme(content: @Composable () -> Unit) {
    val isDarkTheme = isSystemInDarkTheme()
    val colorScheme = if (isDarkTheme) {
        darkColorScheme(
            primary = colorResource(id = R.color.purple_200),
            secondary = colorResource(id = R.color.teal_200),
            // Define other dark theme colors if needed
            background = colorResource(id = R.color.board_area_background_dark), // Consistent background
            surface = colorResource(id = R.color.cell_unclicked_background_dark)   // Consistent surface
        )
    } else {
        lightColorScheme(
            primary = colorResource(id = R.color.purple_500),
            secondary = colorResource(id = R.color.teal_700),
            // Define other light theme colors if needed
            background = colorResource(id = R.color.board_area_background_light),
            surface = colorResource(id = R.color.cell_unclicked_background_light)
        )
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography(), // Define your typography if needed
        content = content
    )
}
