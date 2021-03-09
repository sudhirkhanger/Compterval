/*
 * Copyright 2021 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.androiddevchallenge

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.androiddevchallenge.ui.theme.MyTheme
import kotlinx.coroutines.delay

val waterColor = Color(red = 125, green = 207, blue = 241)
const val TIMEOUT = 10

@ExperimentalAnimationApi
@Composable
fun TimerApp() {
    Surface(color = MaterialTheme.colors.background) {
        HourGlass()
    }
}

@ExperimentalAnimationApi
@Preview("Light Theme", widthDp = 360, heightDp = 640)
@Composable
fun TimerLightPreview() {
    MyTheme {
        TimerApp()
    }
}

@ExperimentalAnimationApi
@Preview("Dark Theme", widthDp = 360, heightDp = 640)
@Composable
fun TimerDarkPreview() {
    MyTheme(darkTheme = true) {
        TimerApp()
    }
}

@ExperimentalAnimationApi
@Composable
fun HourGlass() {
    val animationState = remember { mutableStateOf(false) }
    var timer by remember { mutableStateOf(TIMEOUT) }
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            StartButton(
                animationState = animationState.value,
                onToggleAnimationState = { animationState.value = animationState.value.not() }
            )
            Text(
                text = "$timer Seconds",
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp,
                color = MaterialTheme.colors.onSurface
            )
        }

        BoxWithConstraints(
            modifier = Modifier.padding(8.dp)
        ) {
            val height = maxHeight
            val width = maxWidth
            TopTriangleStroke(height = height, width = width)
            BottomTriangleStroke(height = height, width = width)
            ClockScreen(
                countdownSpan = TIMEOUT,
                height = height,
                width = width,
                isClockRunning = animationState.value,
                onTimeUpdate = { timer = it }
            )
        }
    }
}

@Composable
fun TopTriangleStroke(height: Dp, width: Dp) {
    val color = MaterialTheme.colors.onSurface
    Canvas(
        modifier = Modifier
            .height(height)
            .width(width)
    ) {

        val canvasWidth = size.width
        val canvasHeight = size.height

        val path = Path().apply {
            moveTo(0f, 0f)
            lineTo(canvasWidth, 0f)
            lineTo(canvasWidth / 2, canvasHeight / 2)
            lineTo(0f, 0f)
            close()
        }

        drawPath(
            path = path,
            color = color,
            style = Stroke(width = 8f)
        )
    }
}

@Composable
fun BottomTriangleStroke(height: Dp, width: Dp) {
    val color = MaterialTheme.colors.onSurface
    Canvas(
        modifier = Modifier
            .height(height)
            .width(width)
    ) {

        val canvasWidth = size.width
        val canvasHeight = size.height

        val path = Path().apply {
            moveTo(canvasWidth / 2, canvasHeight / 2)
            lineTo(canvasWidth, canvasHeight)
            lineTo(0f, canvasHeight)
            lineTo(canvasWidth / 2, canvasHeight / 2)
            close()
        }

        drawPath(
            path = path,
            color = color,
            style = Stroke(width = 8f)
        )
    }
}

@Composable
fun TopTriangleFill(height: Dp, width: Dp, percent: Int) {
    if (percent >= 0)
        Canvas(
            modifier = Modifier
                .height(height)
                .width(width)
                .animateContentSize()
        ) {
            val canvasWidth = size.width
            val canvasHeight = size.height

            val widthFactor = ((canvasWidth / 2) * (100 - percent)) / 100
            val heightFactor = ((canvasHeight / 2) * (100 - percent)) / 100

            val p1x = widthFactor
            val p1y = heightFactor
            val p2x = canvasWidth - widthFactor
            val p2y = p1y
            val p3x = canvasWidth / 2
            val p3y = canvasHeight / 2

            val path = Path().apply {
                moveTo(p1x, p1y)
                lineTo(p2x, p2y)
                lineTo(p3x, p3y)
                lineTo(p1x, p1y)
                close()
            }

            drawPath(
                path = path,
                color = waterColor
            )
        }
}

@Composable
fun BottomTriangleFill(height: Dp, width: Dp, percent: Int) {
    Canvas(
        modifier = Modifier
            .height(height)
            .width(width)
            .animateContentSize()
    ) {
        val canvasWidth = size.width
        val canvasHeight = size.height

        val widthFactor = ((canvasWidth / 2) * (100 - if (percent < 0) 0 else percent)) / 100
        val heightFactor = ((canvasHeight / 2) * (100 - if (percent < 0) 0 else percent)) / 100

        val p1x = 0f
        val p1y = canvasHeight
        val p2x = canvasWidth
        val p2y = p1y

        val p3x = canvasWidth - widthFactor
        val p3y = canvasHeight - heightFactor
        val p4x = widthFactor
        val p4y = p3y

        val path = Path().apply {
            moveTo(p1x, p1y)
            lineTo(p2x, p2y)
            lineTo(p3x, p3y)
            lineTo(p4x, p4y)
            lineTo(p1x, p1y)
            close()
        }

        drawPath(
            path = path,
            color = waterColor
        )
    }
}

@ExperimentalAnimationApi
@Composable
fun ClockScreen(
    isClockRunning: Boolean,
    countdownSpan: Int,
    height: Dp,
    width: Dp,
    onTimeUpdate: (Int) -> Unit
) {
    var timer by remember { mutableStateOf(countdownSpan) }
    var percent by remember { mutableStateOf(100) }
    if (!isClockRunning) {
        timer = countdownSpan
        percent = 100
    } else {
        LaunchedEffect(0) {
            while (true) {
                delay(1000)
                timer -= 1
                percent = (timer * 100) / countdownSpan
                if (timer <= 0) break
            }
        }
    }

    onTimeUpdate(timer)
    TopTriangleFill(height = height, width = width, percent = percent)
    BottomTriangleFill(height = height, width = width, percent = percent)
}

@Composable
fun StartButton(
    animationState: Boolean,
    onToggleAnimationState: () -> Unit,
) {
    if (animationState) {
        Button(
            onClick = onToggleAnimationState,
            colors = ButtonDefaults.buttonColors(
                backgroundColor = Color.Red,
                contentColor = Color.White
            )
        ) {
            Text("Stop")
        }
    } else {
        Button(
            onClick = onToggleAnimationState,
        ) {
            Text("Start")
        }
    }
}
