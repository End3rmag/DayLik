package com.example.myapplication.ui.screen.Otp


import android.annotation.SuppressLint
import android.os.CountDownTimer
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.myapplication.R
import com.example.myapplication.ui.data.remote.dto.request.AuthorizationRequest
import com.example.myapplication.ui.screen.component.AuthTextField
import com.example.myapplication.ui.screen.component.TitleWithSubtitleText
import com.example.myapplication.ui.theme.MatuleTheme
import java.lang.Error
import java.time.Period
import kotlin.concurrent.timer
import kotlin.time.Duration.Companion.seconds


//@Preview
//@Composable
//fun pr(){
//    MatuleTheme{
//    OptScrn {  }
//}}

@Composable
fun OptScrn(title: String = "", navigation: () -> Unit) {
    var otpValue by remember { mutableStateOf("") }
    val hasError = otpValue.length < 6
    var timerValue by remember { mutableStateOf(30) }
    var isTimerRunning by remember { mutableStateOf(true) }

    fun startTimer() {
        isTimerRunning = true
        timerValue = 30
    }

    Scaffold(
        modifier = Modifier.fillMaxSize().background(MatuleTheme.colors.biskuit).systemBarsPadding(),
        topBar = {
            Row(
                modifier = Modifier
                    .background(MatuleTheme.colors.biskuit)
                    .padding(top = 35.dp)
                    .fillMaxWidth()
                    .height(40.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = navigation,
                    modifier = Modifier.padding(start = 8.dp)
                ) {
                    Icon(
                        painter = painterResource(R.drawable.back_arrow),
                        contentDescription = null,
                        tint = MatuleTheme.colors.text
                    )
                }


            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .background(MatuleTheme.colors.biskuit)
                .padding(paddingValues)
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            TitleWithSubtitleText(
                title = stringResource(R.string.OTP),
                subTitle = stringResource(R.string.subOTP)
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "OTP Код",
                style = MatuleTheme.typography.headingBold24.copy(color = MatuleTheme.colors.text),
                modifier = Modifier.padding(start = 10.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            OtpTextField(
                value = otpValue,
                length = 6,
                hasError = hasError,
                onValueChange = { otpValue = it }
            )

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth().background(MatuleTheme.colors.biskuit),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextButton(
                    onClick = {
                        if (!isTimerRunning) {
                            startTimer()
                        }
                    },
                    enabled = !isTimerRunning
                ) {
                    Text(
                        text = "Отправить заново",
                        style = MatuleTheme.typography.subTitleRegular16.copy(color = MatuleTheme.colors.subTextDark)
                    )
                }

                if (isTimerRunning) {
                    TimerText(
                        text = { time -> Text(text = time) },
                        period = timerValue.toLong()
                    ) {
                        isTimerRunning = false
                    }
                }
            }
        }
    }
}

@Composable
fun OtpTextField(
    value: String,
    length: Int,
    hasError: Boolean = false,
    modifier: Modifier = Modifier,
    boxWidth: Dp = 50.dp,
    boxHeight: Dp= 100.dp,
    keyboardOptions: KeyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
    keyboardActions: KeyboardActions = KeyboardActions(),
    onValueChange: (String) -> Unit)  {
    BasicTextField(
        value = value,
        onValueChange = {
            if(it.length <= length){
                onValueChange(it)
            }
        },
        modifier = modifier,
        keyboardActions = keyboardActions,
        keyboardOptions = keyboardOptions,
        decorationBox = {
            val spaceBoxBetween = 12.dp
            Row(
                modifier = Modifier
                    .size((boxWidth + spaceBoxBetween) * length, height = boxHeight),
                horizontalArrangement = Arrangement.SpaceEvenly
            ){
                val border = BorderStroke(
                    width = 1.dp,
                    color = if(hasError)  Color.Red  else Color.Blue
                )
                repeat(length){
                    Box(
                        modifier = Modifier
                            .width(boxWidth)
                            .height(boxHeight)
                            .clip(shape = RoundedCornerShape(size = 12.dp))
                            .background(Color.LightGray)
                            .border(border, shape = RoundedCornerShape(size = 12.dp)),
                        contentAlignment = Alignment.Center
                    ){
                        Text(text = value.getOrNull(it)?.toString() ?: "")
                    }
                }
            }
        }
    )
}

@Composable
fun TimerText(text: @Composable (text: String) -> Unit, period: Long, action: () -> Unit) {
    val dispText = remember { mutableStateOf("") }
    val counter = object : CountDownTimer(period * 1000, 1000) {
        override fun onTick(millisUntilFinished: Long) {
            (millisUntilFinished / 1000).seconds.toComponents { minutes, seconds, _ ->
                dispText.value = String.format("$minutes:$seconds")
            }
        }

        override fun onFinish() {
            dispText.value = "00:00"
            action.invoke()
        }
    }
    LaunchedEffect(Unit) {
        counter.start()
    }
    if (dispText.value != "00:00") {
        text(dispText.value)
    }
}
