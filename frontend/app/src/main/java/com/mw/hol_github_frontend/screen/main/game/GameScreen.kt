package com.mw.hol_github_frontend.screen.main.game

import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.mw.hol_github_frontend.api.ApiClient
import com.mw.hol_github_frontend.composable.Spinner
import com.mw.hol_github_frontend.dto.RepoDto
import com.mw.hol_github_frontend.theme.AppTheme
import com.mw.hol_github_frontend.theme.Typography
import kotlinx.coroutines.launch

@Composable
fun GameScreen(viewModel: GameViewModel = hiltViewModel()) {
    val game = viewModel.game.collectAsState()

    LaunchedEffect(Unit) {
        // Use scope to not block navigation and instead display a spinner
        viewModel.viewModelScope.launch {
            if (game.value != null) {
                return@launch
            }
            viewModel.newGame()
        }
    }

    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {
        if (game.value == null) {
            Spinner(size = 64.dp, strokeWidth = 6.dp)
            return
        }

        val game = game.value!!
        Repo(game.firstImage, game.repos.first, "First repo image")

        OutlinedCard(
            modifier = Modifier.size(54.dp, 32.dp),
            shape = CircleShape,
        ) {
            Text(
                "vs",
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .offset(x = 0.dp, y = 3.dp),
                fontWeight = FontWeight.Medium,
                fontSize = 20.sp
            )
        }

        Repo(game.secondImage, game.repos.second, "Second repo image")
    }
}

@Composable
private fun Repo(bitmap: ImageBitmap, repo: RepoDto, contentDescription: String) {
    val config = LocalConfiguration.current

    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Text(repo.name, style = Typography.titleLarge)
                Text(repo.description, style = Typography.labelLarge)
            }

            Box(
                modifier = Modifier
                    .heightIn(max = config.screenHeightDp.dp / 3)
                    .align(Alignment.CenterHorizontally), contentAlignment = Alignment.Center
            ) {
                val tint = if (isSystemInDarkTheme()) 0.4f else 0.8f
                Image(
                    bitmap = bitmap,
                    contentDescription = contentDescription,
                    colorFilter = ColorFilter.tint(Color(tint, tint, tint), BlendMode.Multiply),
                )

                val color = if (isSystemInDarkTheme()) 0f else 1f
                val buttonBg = Color(color, color, color, 0.55f)

                if (repo.starAmount != null) {
                    OutlinedButton(
                        onClick = { /*TODO*/ },
                        colors = ButtonDefaults.outlinedButtonColors(containerColor = buttonBg)
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Text(
                                repo.starAmount.toString(),
                                fontSize = 20.sp,
                                color = Color.White,
                                fontWeight = FontWeight.Black,
                            )

                            Icon(
                                Icons.Filled.Star,
                                contentDescription = "Star",
                                tint = MaterialTheme.colorScheme.tertiary,
                                modifier = Modifier.size(26.dp)
                            )
                        }
                    }
                } else {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(12.dp, 6.dp)
                    ) {
                        OutlinedButton(
                            onClick = { /*TODO*/ },
                            colors = ButtonDefaults.outlinedButtonColors(containerColor = buttonBg)
                        ) {
                            Text(
                                "Higher",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                            )
                        }

                        Text(
                            "or",
                            color = Color.White,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.ExtraBold,
                            modifier = Modifier.padding(horizontal = 4.dp)
                        )

                        OutlinedButton(
                            onClick = { /*TODO*/ },
                            colors = ButtonDefaults.outlinedButtonColors(containerColor = buttonBg)
                        ) {
                            Text(
                                "Lower",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = MaterialTheme.colorScheme.error
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun GameScreenPreview() {
    AppTheme(useDarkTheme = true) {
        GameScreen(viewModel = GameViewModel(ApiClient(LocalContext.current), Gson()))
    }
}