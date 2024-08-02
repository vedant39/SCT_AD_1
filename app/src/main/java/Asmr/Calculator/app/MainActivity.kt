package Asmr.Calculator.app

import Asmr.Calculator.app.ui.theme.Action_buttons
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import Asmr.Calculator.app.ui.theme.MyApplicationTheme
import Asmr.Calculator.app.ui.theme.Operators
import android.service.autofill.OnClickAction
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class MainActivity : ComponentActivity() {

    private val viewModel: ApplicationViewMode by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApplicationTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.secondary
                ) {
                    val calculatorButtons = remember {
                        mutableListOf(
                            CalculatorButton("AC", CalculatorButtonType.Reset),
                            CalculatorButton("AC", CalculatorButtonType.Reset),
                            CalculatorButton("AC", CalculatorButtonType.Reset),
                            CalculatorButton("/", CalculatorButtonType.Action),
                            CalculatorButton("7", CalculatorButtonType.Normal),
                            CalculatorButton("8", CalculatorButtonType.Normal),
                            CalculatorButton("9", CalculatorButtonType.Normal),
                            CalculatorButton("x", CalculatorButtonType.Action),
                            CalculatorButton("4", CalculatorButtonType.Normal),
                            CalculatorButton("5", CalculatorButtonType.Normal),
                            CalculatorButton("6", CalculatorButtonType.Normal),
                            CalculatorButton("-", CalculatorButtonType.Action),
                            CalculatorButton("1", CalculatorButtonType.Normal),
                            CalculatorButton("2", CalculatorButtonType.Normal),
                            CalculatorButton("3", CalculatorButtonType.Normal),
                            CalculatorButton("+", CalculatorButtonType.Action),
                            CalculatorButton(icon = Icons.Outlined.Refresh, type = CalculatorButtonType.Normal),
                            CalculatorButton("0", CalculatorButtonType.Normal),
                            CalculatorButton(".", CalculatorButtonType.Normal),
                            CalculatorButton("=", CalculatorButtonType.Action),
                        )
                    }
                    val (uiText,setuiText) = remember {
                        mutableStateOf("0")
                    }
                    val (input, setInput) = remember { mutableStateOf<String?>(null) }

                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomCenter) {
                        Column {
                            Text(
                                modifier = Modifier.padding(8.dp),
                                text = uiText,
                                fontSize = 32.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            LazyVerticalGrid(
                                modifier = Modifier
                                    .padding(8.dp)
                                    .clip(RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp))
                                    .background(MaterialTheme.colorScheme.primary)
                                    .padding(8.dp),
                                columns = GridCells.Fixed(4),
                                verticalArrangement = Arrangement.spacedBy(16.dp),
                                horizontalArrangement = Arrangement.spacedBy(16.dp),
                                contentPadding = PaddingValues(16.dp)
                            ) {
                                items(calculatorButtons) {
                                    Calculator(button = it, onClick = {
                                        when (it.type) {
                                            CalculatorButtonType.Normal -> {
                                                setuiText(uiText + it.text)
                                                setInput((input ?: " ") + it.text)
                                            }
                                            CalculatorButtonType.Action -> {
                                                if (it.text == "=") {
                                                    val result = viewModel.getRes()
                                                    setuiText(result.toString())
                                                    viewModel.resetAll()
                                                } else {
                                                    if (input != null) {
                                                        if (viewModel.firstNum.value == 0.0) {
                                                            viewModel.setFirstNum(input.toDouble())
                                                        } else {
                                                            viewModel.setSecondNum(input.toDouble())
                                                        }
                                                        viewModel.setAction(it.text!!)
                                                        setInput(null)
                                                    }
                                                }
                                            }
                                            CalculatorButtonType.Reset -> {
                                                setuiText("")
                                                viewModel.resetAll()
                                            }
                                        }
                                    })
                                }
                            }
                        }
                    }

                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.TopCenter) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                                .clip(RoundedCornerShape(8.dp))
                        ) {
                            Icon(
                                modifier = Modifier.size(20.dp),
                                painter = painterResource(id = R.drawable.darkmode),
                                contentDescription = null,
                                tint = Color.White
                            )
                            Icon(
                                modifier = Modifier.size(20.dp),
                                painter = painterResource(id = R.drawable.lightmode),
                                contentDescription = null,
                                tint = Color.White
                            )
                        }
                    }
                }
            }
        }
    }

    @Composable
    fun Calculator(button: CalculatorButton, onClick: () -> Unit) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(16.dp))
                .background(MaterialTheme.colorScheme.secondary)
                .fillMaxHeight()
                .aspectRatio(1f)
                .clickable { onClick() },
            contentAlignment = Alignment.Center
        ) {
            val contentColor =
                if (button.type == CalculatorButtonType.Normal) Color.White
                else if (button.type == CalculatorButtonType.Action) Operators
                else Action_buttons

            if (button.text != null) {
                Text(
                    button.text,
                    color = contentColor,
                    fontWeight = FontWeight.Bold,
                    fontSize = if (button.type == CalculatorButtonType.Action) 25.sp else 20.sp
                )
            } else {
                Icon(
                    modifier = Modifier.size(32.dp),
                    imageVector = button.icon!!,
                    contentDescription = null,
                    tint = contentColor
                )
            }
        }
    }

    data class CalculatorButton(
        val text: String? = null,
        val type: CalculatorButtonType,
        val icon: ImageVector? = null
    )

    enum class CalculatorButtonType {
        Normal, Action, Reset
    }
}
