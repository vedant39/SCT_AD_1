package Asmr.Calculator.app

import Asmr.Calculator.app.ui.theme.Action_buttons
import Asmr.Calculator.app.ui.theme.MyApplicationTheme
import Asmr.Calculator.app.ui.theme.Operators
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class MainActivity : ComponentActivity() {

    private val viewModel: ApplicationViewModel by viewModels()

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
                            CalculatorButton("%", CalculatorButtonType.Action),
                            CalculatorButton("x²", CalculatorButtonType.Action),
                            CalculatorButton("±", CalculatorButtonType.Action),
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
                            CalculatorButton(icon = Icons.Outlined.Refresh, type = CalculatorButtonType.Reset),
                            CalculatorButton("0", CalculatorButtonType.Normal),
                            CalculatorButton(".", CalculatorButtonType.Normal),
                            CalculatorButton("=", CalculatorButtonType.Action),
                        )
                    }
                    val (uiText,setuiText) = remember {
                        mutableStateOf("0")
                    }
                    LaunchedEffect(uiText){
                        if(uiText.startsWith("0")&& uiText != "0"){
                            setuiText(uiText.substring(1))
                        }
                    }
                    val (input, setInput) = remember { mutableStateOf<String?>(null) }

                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomCenter) {
                        Column {
                            Text(
                                modifier = Modifier.padding(8.dp),
                                text = uiText,
                                fontSize = 48.sp,
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
                                                runCatching {
                                                    setuiText(uiText.toInt().toString()+ it.text)
                                                }.onFailure {
                                                        throwable ->  setuiText(uiText + it.text)
                                                }
                                                setInput((input ?: "") + it.text)
                                                if(viewModel.action.value.isNotEmpty()){
                                                    if (viewModel.secondNum.value == null){
                                                        viewModel.setSecondNum(it.text!!.toDouble())
                                                    }else{
                                                        if (viewModel.secondNum.value.toString().split( ".")[1] == "0"){
                                                            viewModel.setSecondNum((viewModel.secondNum.value.toString().split(".").first()+it.text!!).toDouble())
                                                        }else{
                                                            viewModel.setSecondNum((viewModel.secondNum.value.toString()+it.text!!).toDouble())
                                                        }
                                                    }
                                                }
                                            }
                                            CalculatorButtonType.Action -> {
                                                if (it.text == "=") {
                                                    if (viewModel.firstNum.value != null && viewModel.secondNum.value != null) {
                                                        val result = viewModel.getRes()
                                                        setuiText(result.toString())
                                                    } else {
                                                        setuiText("Error")
                                                    }
                                                    setInput(null)
                                                    viewModel.resetAll()
                                                } else {
                                                    runCatching {
                                                        setuiText(uiText.toInt().toString() + it.text )
                                                    }.onFailure {  throwable ->  setuiText(uiText + it.text) }
                                                    if (input != null) {
                                                        if (viewModel.firstNum.value == null) {
                                                            viewModel.setFirstNum(input.toDouble())
                                                        } else {
                                                            viewModel.setSecondNum(input.toDouble())
                                                        }
                                                        viewModel.setAction(it.text!!)
                                                        setInput(null)
                                                    }
                                                }
                                                when (it.text) {
                                                    "%" -> {
                                                        setuiText((uiText.toDouble() / 100).toString())
                                                    }
                                                    "x²" -> {
                                                        setuiText((uiText.toDouble() * uiText.toDouble()).toString())
                                                    }
                                                    "±" -> {
                                                        setuiText((uiText.toDouble() * -1).toString())
                                                    }
                                                }
                                            }
                                            CalculatorButtonType.Reset -> {
                                                setuiText("0")
                                                setInput(null)
                                                viewModel.resetAll()
                                            }
                                        }
                                    })
                                }
                            }
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
