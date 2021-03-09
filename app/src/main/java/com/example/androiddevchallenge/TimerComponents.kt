package com.example.androiddevchallenge

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.androiddevchallenge.ui.theme.MyTheme
import kotlinx.coroutines.delay

val waterColor = Color(red = 125, green = 207, blue = 241)

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
    Column {
        StartButton(
            animationState = animationState.value,
            onToggleAnimationState = { animationState.value = animationState.value.not() }
        )
        BoxWithConstraints(
            modifier = Modifier.padding(8.dp)
        ) {
            val height = maxHeight
            val width = maxWidth
            TopTriangleStroke(height = height, width = width)
            BottomTriangleStroke(height = height, width = width)
            ClockScreen(
                countdownSpan = 60,
                height = height,
                width = width,
                isClockRunning = animationState.value
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

@ExperimentalAnimationApi
@Composable
fun FallingDroplet(
    isDropletFalling: Boolean,
    maxWidth: Dp,
    maxHeight: Dp
) {
    if (isDropletFalling) {
        val resource: Painter
        val modifier: Modifier
        val dropletSize = 25.dp
        val infiniteTransition = rememberInfiniteTransition()
        val bubbleDirectionState = infiniteTransition.animateFloat(
            initialValue = 0f,
            targetValue = 1f,
            animationSpec = infiniteRepeatable(
                animation = tween(
                    durationMillis = 500,
                    easing = LinearEasing
                )
            )
        )
        val yPositionState = infiniteTransition.animateFloat(
            initialValue = 0f,
            targetValue = 1f,
            animationSpec = infiniteRepeatable(
                animation = tween(
                    durationMillis = 2000,
                    easing = LinearEasing
                )
            )
        )
        resource = if (bubbleDirectionState.value <= .5f) {
            painterResource(id = R.drawable.drop_l)
        } else {
            painterResource(id = R.drawable.drop_r)
        }
        modifier =
            Modifier.offset(
                x = (maxWidth - dropletSize) / 2,
                y = (maxHeight / 2) + (((maxHeight / 2) - dropletSize) * yPositionState.value)
            )
        Image(
            modifier = modifier
                .width(dropletSize)
                .height(dropletSize),
            painter = resource,
            contentDescription = "Falling droplet"
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
    width: Dp
) {
    var timer by remember { mutableStateOf(countdownSpan) }
    var dropletFallingState by remember { mutableStateOf(true) }
    var percent by remember { mutableStateOf(100) }
    if (!isClockRunning) {
        timer = countdownSpan
        dropletFallingState = false
        percent = 100
    } else {
        dropletFallingState = true

        LaunchedEffect(dropletFallingState) {
            while (true) {
                delay(1000)
                timer -= 1
                percent = (timer * 100) / countdownSpan
                if (timer <= 0) {
                    dropletFallingState = false
                    break
                }
            }
        }
    }
    TopTriangleFill(height = height, width = width, percent = percent)
    BottomTriangleFill(height = height, width = width, percent = percent)
    FallingDroplet(isDropletFalling = dropletFallingState, maxWidth = width, maxHeight = height)
}

@Composable
fun StartButton(
    animationState: Boolean,
    onToggleAnimationState: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.Center
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
}