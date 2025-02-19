package com.example.infininteplatformer

import android.media.MediaPlayer
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.infininteplatformer.ui.theme.InfinintePlatformerTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.roundToInt
import kotlin.random.Random

// Your mutableStateOf variables for platforms
var PH by mutableStateOf(0)
var P1X by mutableStateOf(0)
var P1Y by mutableStateOf(0)
var MP1X by mutableStateOf(0)
var P1W by mutableStateOf(0)
var P2X by mutableStateOf(0)
var P2Y by mutableStateOf(0)
var MP2X by mutableStateOf(0)
var P2W by mutableStateOf(0)
var P3X by mutableStateOf(0)
var P3Y by mutableStateOf(0)
var MP3X by mutableStateOf(0)
var P3W by mutableStateOf(0)
var P4X by mutableStateOf(0)
var P4Y by mutableStateOf(0)
var MP4X by mutableStateOf(0)
var P4W by mutableStateOf(0)
var P5X by mutableStateOf(0)
var P5Y by mutableStateOf(0)
var MP5X by mutableStateOf(0)
var P5W by mutableStateOf(0)
var P6X by mutableStateOf(0)
var P6Y by mutableStateOf(0)
var MP6X by mutableStateOf(0)
var P6W by mutableStateOf(0)
var P7X by mutableStateOf(0)
var P7Y by mutableStateOf(0)
var MP7X by mutableStateOf(0)
var P7W by mutableStateOf(0)
var P8X by mutableStateOf(0)
var P8Y by mutableStateOf(0)
var MP8X by mutableStateOf(0)
var P8W by mutableStateOf(0)
var P9X by mutableStateOf(0)
var P9Y by mutableStateOf(0)
var MP9X by mutableStateOf(0)
var P9W by mutableStateOf(0)
var P10X by mutableStateOf(0)
var P10Y by mutableStateOf(0)
var MP10X by mutableStateOf(0)
var P10W by mutableStateOf(0)
var scoreState = mutableStateOf(0)
val mp3Files = listOf(
    R.raw.blue_skies,
    R.raw.carefree,
    R.raw.cipher,
    R.raw.dog_and_pony_show,
    R.raw.easy_lemon,
    R.raw.if_i_had_a_chicken,
    R.raw.investigations,
    R.raw.local_elevator,
    R.raw.monkeys_spinning_monkeys,
    R.raw.scheming_weasel_faster,
    R.raw.sneaky_adventure,
    R.raw.sneaky_snitch,
    R.raw.spring_in_my_step,
    R.raw.sugar_zone,
    R.raw.summer_smile,
    R.raw.wallpaper
)
val Lightblue = Color(0xff8caaf2)
val Lightgray = Color(0xff646368)
var playerX by mutableStateOf(0)
var playerY by mutableStateOf(0)
var playerradius by mutableStateOf(10.dp) // Set a default radius


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            InfinintePlatformerTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MyApp()
                }
            }
        }
    }

    @Composable
    fun MyApp() {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            AppNavigation()
            MusicPlayer(mp3Files)
        }
    }

    @Composable
    fun AppNavigation() {
        val navController = rememberNavController()

        NavHost(navController = navController, startDestination = "game") {
            composable("mainMenu") { MainMenuScreen(navController) }
            composable("game") { GameScreen(navController) }
            composable("debugSettings") { DebugSettingsScreen(navController) }
        }
    }

    // Main Menu Screen
    @Composable
    fun MainMenuScreen(navController: NavController) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Button(onClick = { navController.navigate("game") }) {
                    Text("Start Game")
                }
                Button(onClick = { navController.navigate("debugSettings") }) {
                    Text("Debug Settings")
                }
            }
        }
    }

    // Game Screen
    @Composable
    fun GameScreen(navController: NavController) {
        Box(modifier = Modifier.fillMaxSize()) {
            PlatformCalculations()
            Playerinteraction()
            Score()
            DoDebug()
            if (isDebugModeEnabled) {
                Debug(navController) // Use your custom Debug() composable here
            }
        }
    }

    @Composable
    fun Playerinteraction() {
        var touchX by remember { mutableStateOf(0f) }
        val dpValue = 10
        val context = LocalContext.current
        val displayMetrics: DisplayMetrics = context.resources.displayMetrics
        val pixelValue = dpValue * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT)

        Box(
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(Unit) {
                    detectDragGestures(
                        onDragStart = { offset ->
                            touchX = offset.x
                            Log.d("TouchX", "Drag Start X: $touchX")
                        },
                        onDrag = { change, _ ->
                            touchX = change.position.x
                            Log.d("TouchX", "Dragging X: $touchX")
                            change.consume()
                        },
                        onDragEnd = {
                            Log.d("TouchX", "Drag End")
                        }
                    )
                }
        ) {
            touchXdb = touchX.toInt()
            var playerX = touchX - pixelValue
            Player(xpos = playerX)
        }
    }

    @Composable
    fun PlatformCalculations() {
        val platformHeightDp = 5.dp
        val context = LocalContext.current
        val displayMetrics = context.resources.displayMetrics
        val screenWidth = displayMetrics.widthPixels.toFloat()
        val screenHeight = displayMetrics.heightPixels.toFloat()
        val baseMinWidth = 200 // Base minimum width for a standard screen size
        val baseMaxWidth = 400 // Base maximum width for a standard screen size
        val standardScreenWidth = 1080f // Example standard screen width in pixels
        val scaleFactor = screenWidth / standardScreenWidth
        val scaledMinWidth = (baseMinWidth * scaleFactor).toInt()
        val scaledMaxWidth = (baseMaxWidth * scaleFactor).toInt()
        val platformSpeed = 1f
        val density = LocalDensity.current
        val spacing = screenHeight / 10
        val platformHeightPx = with(density) { platformHeightDp.toPx() }
        Platform1(
            startpos = spacing,
            minWidth = scaledMinWidth,
            maxWidth = scaledMaxWidth,
            screenWidth = screenWidth,
            platformHeight = platformHeightPx,
            speed = platformSpeed
        )
        Platform2(
            startpos = spacing,
            minWidth = scaledMinWidth,
            maxWidth = scaledMaxWidth,
            screenWidth = screenWidth,
            platformHeight = platformHeightPx,
            speed = platformSpeed
        )
        Platform3(
            startpos = spacing,
            minWidth = scaledMinWidth,
            maxWidth = scaledMaxWidth,
            screenWidth = screenWidth,
            platformHeight = platformHeightPx,
            speed = platformSpeed
        )
        Platform4(
            startpos = spacing,
            minWidth = scaledMinWidth,
            maxWidth = scaledMaxWidth,
            screenWidth = screenWidth,
            platformHeight = platformHeightPx,
            speed = platformSpeed
        )
        Platform5(
            startpos = spacing,
            minWidth = scaledMinWidth,
            maxWidth = scaledMaxWidth,
            screenWidth = screenWidth,
            platformHeight = platformHeightPx,
            speed = platformSpeed
        )
        Platform6(
            startpos = spacing,
            minWidth = scaledMinWidth,
            maxWidth = scaledMaxWidth,
            screenWidth = screenWidth,
            platformHeight = platformHeightPx,
            speed = platformSpeed
        )
        Platform7(
            startpos = spacing,
            minWidth = scaledMinWidth,
            maxWidth = scaledMaxWidth,
            screenWidth = screenWidth,
            platformHeight = platformHeightPx,
            speed = platformSpeed
        )
        Platform8(
            startpos = spacing,
            minWidth = scaledMinWidth,
            maxWidth = scaledMaxWidth,
            screenWidth = screenWidth,
            platformHeight = platformHeightPx,
            speed = platformSpeed
        )
        Platform9(
            startpos = spacing,
            minWidth = scaledMinWidth,
            maxWidth = scaledMaxWidth,
            screenWidth = screenWidth,
            platformHeight = platformHeightPx,
            speed = platformSpeed
        )
        Platform10(
            startpos = spacing,
            minWidth = scaledMinWidth,
            maxWidth = scaledMaxWidth,
            screenWidth = screenWidth,
            platformHeight = platformHeightPx,
            speed = platformSpeed
        )
    }

    @Composable
    fun Platform1(
        startpos: Float,
        minWidth: Int,
        maxWidth: Int,
        screenWidth: Float,
        platformHeight: Float,
        speed: Float
    ) {
        var platformX by remember { mutableStateOf(0f) }
        var platformY by remember { mutableStateOf(0f) }
        var width by remember { mutableStateOf(0f) }
        var maxX by remember { mutableStateOf(0f) }
        val platformSpeed = speed // Adjust this value to change the speed
        val context = LocalContext.current
        val displayMetrics = context.resources.displayMetrics
        val screenHeight = displayMetrics.heightPixels.toFloat()
        P1X = platformX.toInt()
        P1Y = platformY.toInt()
        MP1X = maxX.toInt()
        P1W = width.toInt()


        LaunchedEffect(Unit) {
            width = (Random.nextInt(maxWidth - minWidth) + minWidth).toFloat()
            maxX = (screenWidth - width)
            platformY = startpos
            platformX = Random.nextFloat() * maxX
            while (true) {
                platformY -= platformSpeed
                if (platformY < 0) { // Reset logic is now inside the loop
                    scoreState.value++
                    width = (Random.nextInt(maxWidth - minWidth) + minWidth).toFloat()
                    maxX = (screenWidth - width)
                    platformY = screenHeight
                    platformX = Random.nextFloat() * maxX
                }
                delay(16) // Approximately 60 frames per second
            }
        }

        Canvas(modifier = Modifier.fillMaxSize()) {
            drawRect(
                color = Color.Green,
                topLeft = Offset(platformX, platformY),
                size = Size(width, platformHeight)
            )
        }
    }

    @Composable
    fun Platform2(
        startpos: Float,
        minWidth: Int,
        maxWidth: Int,
        screenWidth: Float,
        platformHeight: Float,
        speed: Float
    ) {
        var platformX by remember { mutableStateOf(0f) }
        var platformY by remember { mutableStateOf(0f) }
        var width by remember { mutableStateOf(0f) }
        var maxX by remember { mutableStateOf(0f) }
        val platformSpeed = speed // Adjust this value to change the speed
        val context = LocalContext.current
        val displayMetrics = context.resources.displayMetrics
        val screenHeight = displayMetrics.heightPixels.toFloat()
        P2X = platformX.toInt()
        P2Y = platformY.toInt()
        MP2X = maxX.toInt()
        P2W = width.toInt()

        LaunchedEffect(Unit) {
            width = (Random.nextInt(maxWidth - minWidth) + minWidth).toFloat()
            maxX = (screenWidth - width)
            platformY = startpos * 2
            platformX = Random.nextFloat() * maxX
            while (true) {
                platformY -= platformSpeed
                if (platformY < 0) { // Reset logic is now inside the loop
                    scoreState.value++
                    width = (Random.nextInt(maxWidth - minWidth) + minWidth).toFloat()
                    maxX = (screenWidth - width * 2)
                    platformY = screenHeight
                    platformX = Random.nextFloat() * maxX
                }
                delay(16) // Approximately 60 frames per second
            }
        }

        Canvas(modifier = Modifier.fillMaxSize()) {
            drawRect(
                color = Color.Green,
                topLeft = Offset(platformX, platformY),
                size = Size(width, platformHeight)
            )
        }
    }

    @Composable
    fun Platform3(
        startpos: Float,
        minWidth: Int,
        maxWidth: Int,
        screenWidth: Float,
        platformHeight: Float,
        speed: Float
    ) {
        var platformX by remember { mutableStateOf(0f) }
        var platformY by remember { mutableStateOf(0f) }
        var width by remember { mutableStateOf(0f) }
        var maxX by remember { mutableStateOf(0f) }
        val platformSpeed = speed // Adjust this value to change the speed
        val context = LocalContext.current
        val displayMetrics = context.resources.displayMetrics
        val screenHeight = displayMetrics.heightPixels.toFloat()
        P3X = platformX.toInt()
        P3Y = platformY.toInt()
        MP3X = maxX.toInt()
        P3W = width.toInt()

        LaunchedEffect(Unit) {
            width = (Random.nextInt(maxWidth - minWidth) + minWidth).toFloat()
            maxX = (screenWidth - width)
            platformY = startpos * 3
            platformX = Random.nextFloat() * maxX
            while (true) {
                platformY -= platformSpeed
                if (platformY < 0) { // Reset logic is now inside the loop
                    scoreState.value++
                    width = (Random.nextInt(maxWidth - minWidth) + minWidth).toFloat()
                    maxX = screenWidth - width * 2
                    platformY = screenHeight
                    platformX = Random.nextFloat() * maxX
                }
                delay(16) // Approximately 60 frames per second
            }
        }

        Canvas(modifier = Modifier.fillMaxSize()) {
            drawRect(
                color = Color.Green,
                topLeft = Offset(platformX, platformY),
                size = Size(width, platformHeight)
            )
        }
    }

    @Composable
    fun Platform4(
        startpos: Float,
        minWidth: Int,
        maxWidth: Int,
        screenWidth: Float,
        platformHeight: Float,
        speed: Float
    ) {
        var platformX by remember { mutableStateOf(0f) }
        var platformY by remember { mutableStateOf(0f) }
        var width by remember { mutableStateOf(0f) }
        var maxX by remember { mutableStateOf(0f) }
        val platformSpeed = speed // Adjust this value to change the speed
        val context = LocalContext.current
        val displayMetrics = context.resources.displayMetrics
        val screenHeight = displayMetrics.heightPixels.toFloat()
        P4X = platformX.toInt()
        P4Y = platformY.toInt()
        MP4X = maxX.toInt()
        P4W = width.toInt()

        LaunchedEffect(Unit) {
            width = (Random.nextInt(maxWidth - minWidth) + minWidth).toFloat()
            maxX = (screenWidth - width)
            platformY = startpos * 4
            platformX = Random.nextFloat() * maxX
            while (true) {
                platformY -= platformSpeed
                if (platformY < 0) { // Reset logic is now inside the loop
                    scoreState.value++
                    width = (Random.nextInt(maxWidth - minWidth) + minWidth).toFloat()
                    maxX = screenWidth - width * 2
                    platformY = screenHeight
                    platformX = Random.nextFloat() * maxX
                }
                delay(16) // Approximately 60 frames per second
            }
        }

        Canvas(modifier = Modifier.fillMaxSize()) {
            drawRect(
                color = Color.Green,
                topLeft = Offset(platformX, platformY),
                size = Size(width, platformHeight)
            )
        }
    }

    @Composable
    fun Platform5(
        startpos: Float,
        minWidth: Int,
        maxWidth: Int,
        screenWidth: Float,
        platformHeight: Float,
        speed: Float
    ) {
        var platformX by remember { mutableStateOf(0f) }
        var platformY by remember { mutableStateOf(0f) }
        var width by remember { mutableStateOf(0f) }
        var maxX by remember { mutableStateOf(0f) }
        val platformSpeed = speed // Adjust this value to change the speed
        val context = LocalContext.current
        val displayMetrics = context.resources.displayMetrics
        val screenHeight = displayMetrics.heightPixels.toFloat()
        P5X = platformX.toInt()
        P5Y = platformY.toInt()
        MP5X = maxX.toInt()
        P5W = width.toInt()

        LaunchedEffect(Unit) {
            width = (Random.nextInt(maxWidth - minWidth) + minWidth).toFloat()
            maxX = (screenWidth - width)
            platformY = startpos * 5
            platformX = Random.nextFloat() * maxX
            while (true) {
                platformY -= platformSpeed
                if (platformY < 0) { // Reset logic is now inside the loop
                    scoreState.value++
                    width = (Random.nextInt(maxWidth - minWidth) + minWidth).toFloat()
                    maxX = screenWidth - width * 2
                    platformY = screenHeight
                    platformX = Random.nextFloat() * maxX
                }
                delay(16) // Approximately 60 frames per second
            }
        }

        Canvas(modifier = Modifier.fillMaxSize()) {
            drawRect(
                color = Color.Green,
                topLeft = Offset(platformX, platformY),
                size = Size(width, platformHeight)
            )
        }
    }

    @Composable
    fun Platform6(
        startpos: Float,
        minWidth: Int,
        maxWidth: Int,
        screenWidth: Float,
        platformHeight: Float,
        speed: Float
    ) {
        var platformX by remember { mutableStateOf(0f) }
        var platformY by remember { mutableStateOf(0f) }
        var width by remember { mutableStateOf(0f) }
        var maxX by remember { mutableStateOf(0f) }
        val platformSpeed = speed // Adjust this value to change the speed
        val context = LocalContext.current
        val displayMetrics = context.resources.displayMetrics
        val screenHeight = displayMetrics.heightPixels.toFloat()
        P6X = platformX.toInt()
        P6Y = platformY.toInt()
        MP6X = maxX.toInt()
        P6W = width.toInt()

        LaunchedEffect(Unit) {
            width = (Random.nextInt(maxWidth - minWidth) + minWidth).toFloat()
            maxX = (screenWidth - width)
            platformY = startpos * 6
            platformX = Random.nextFloat() * maxX
            while (true) {
                platformY -= platformSpeed
                if (platformY < 0) { // Reset logic is now inside the loop
                    scoreState.value++
                    width = (Random.nextInt(maxWidth - minWidth) + minWidth).toFloat()
                    maxX = screenWidth - width * 2
                    platformY = screenHeight
                    platformX = Random.nextFloat() * maxX
                }
                delay(16) // Approximately 60 frames per second
            }
        }

        Canvas(modifier = Modifier.fillMaxSize()) {
            drawRect(
                color = Color.Green,
                topLeft = Offset(platformX, platformY),
                size = Size(width, platformHeight)
            )
        }
    }

    @Composable
    fun Platform7(
        startpos: Float,
        minWidth: Int,
        maxWidth: Int,
        screenWidth: Float,
        platformHeight: Float,
        speed: Float
    ) {
        var platformX by remember { mutableStateOf(0f) }
        var platformY by remember { mutableStateOf(0f) }
        var width by remember { mutableStateOf(0f) }
        var maxX by remember { mutableStateOf(0f) }
        val platformSpeed = speed // Adjust this value to change the speed
        val context = LocalContext.current
        val displayMetrics = context.resources.displayMetrics
        val screenHeight = displayMetrics.heightPixels.toFloat()
        P7X = platformX.toInt()
        P7Y = platformY.toInt()
        MP7X = maxX.toInt()
        P7W = width.toInt()

        LaunchedEffect(Unit) {
            width = (Random.nextInt(maxWidth - minWidth) + minWidth).toFloat()
            maxX = (screenWidth - width)
            platformY = startpos * 7
            platformX = Random.nextFloat() * maxX
            while (true) {
                platformY -= platformSpeed
                if (platformY < 0) { // Reset logic is now inside the loop
                    scoreState.value++
                    width = (Random.nextInt(maxWidth - minWidth) + minWidth).toFloat()
                    maxX = screenWidth - width * 2
                    platformY = screenHeight
                    platformX = Random.nextFloat() * maxX
                }
                delay(16) // Approximately 60 frames per second
            }
        }

        Canvas(modifier = Modifier.fillMaxSize()) {
            drawRect(
                color = Color.Green,
                topLeft = Offset(platformX, platformY),
                size = Size(width, platformHeight)
            )
        }
    }

    @Composable
    fun Platform8(
        startpos: Float,
        minWidth: Int,
        maxWidth: Int,
        screenWidth: Float,
        platformHeight: Float,
        speed: Float
    ) {
        var platformX by remember { mutableStateOf(0f) }
        var platformY by remember { mutableStateOf(0f) }
        var width by remember { mutableStateOf(0f) }
        var maxX by remember { mutableStateOf(0f) }
        val platformSpeed = speed // Adjust this value to change the speed
        val context = LocalContext.current
        val displayMetrics = context.resources.displayMetrics
        val screenHeight = displayMetrics.heightPixels.toFloat()
        P8X = platformX.toInt()
        P8Y = platformY.toInt()
        MP8X = maxX.toInt()
        P8W = width.toInt()

        LaunchedEffect(Unit) {
            width = (Random.nextInt(maxWidth - minWidth) + minWidth).toFloat()
            maxX = (screenWidth - width)
            platformY = startpos * 8
            platformX = Random.nextFloat() * maxX
            while (true) {
                platformY -= platformSpeed
                if (platformY < 0) { // Reset logic is now inside the loop
                    scoreState.value++
                    width = (Random.nextInt(maxWidth - minWidth) + minWidth).toFloat()
                    maxX = screenWidth - width * 2
                    platformY = screenHeight
                    platformX = Random.nextFloat() * maxX
                }
                delay(16) // Approximately 60 frames per second
            }
        }

        Canvas(modifier = Modifier.fillMaxSize()) {
            drawRect(
                color = Color.Green,
                topLeft = Offset(platformX, platformY),
                size = Size(width, platformHeight)
            )
        }
    }

    @Composable
    fun Platform9(
        startpos: Float,
        minWidth: Int,
        maxWidth: Int,
        screenWidth: Float,
        platformHeight: Float,
        speed: Float
    ) {
        var platformX by remember { mutableStateOf(0f) }
        var platformY by remember { mutableStateOf(0f) }
        var width by remember { mutableStateOf(0f) }
        var maxX by remember { mutableStateOf(0f) }
        val platformSpeed = speed // Adjust this value to change the speed
        val context = LocalContext.current
        val displayMetrics = context.resources.displayMetrics
        val screenHeight = displayMetrics.heightPixels.toFloat()
        P9X = platformX.toInt()
        P9Y = platformY.toInt()
        MP9X = maxX.toInt()
        P9W = width.toInt()

        LaunchedEffect(Unit) {
            width = (Random.nextInt(maxWidth - minWidth) + minWidth).toFloat()
            maxX = (screenWidth - width)
            platformY = startpos * 9
            platformX = Random.nextFloat() * maxX
            while (true) {
                platformY -= platformSpeed
                if (platformY < 0) { // Reset logic is now inside the loop
                    scoreState.value++
                    width = (Random.nextInt(maxWidth - minWidth) + minWidth).toFloat()
                    maxX = screenWidth - width * 2
                    platformY = screenHeight
                    platformX = Random.nextFloat() * maxX
                }
                delay(16) // Approximately 60 frames per second
            }
        }

        Canvas(modifier = Modifier.fillMaxSize()) {
            drawRect(
                color = Color.Green,
                topLeft = Offset(platformX, platformY),
                size = Size(width, platformHeight)
            )
        }
    }

    @Composable
    fun Platform10(
        startpos: Float,
        minWidth: Int,
        maxWidth: Int,
        screenWidth: Float,
        platformHeight: Float,
        speed: Float
    ) {
        var platformX by remember { mutableStateOf(0f) }
        var platformY by remember { mutableStateOf(0f) }
        var width by remember { mutableStateOf(0f) }
        var maxX by remember { mutableStateOf(0f) }
        val platformSpeed = speed // Adjust this value to change the speed
        val context = LocalContext.current
        val displayMetrics = context.resources.displayMetrics
        val screenHeight = displayMetrics.heightPixels.toFloat()
        P10X = platformX.toInt()
        P10Y = platformY.toInt()
        MP10X = maxX.toInt()
        P10W = width.toInt()

        LaunchedEffect(Unit) {
            width = (Random.nextInt(maxWidth - minWidth) + minWidth).toFloat()
            maxX = (screenWidth - width)
            platformY = startpos * 10
            platformX = Random.nextFloat() * maxX
            while (true) {
                platformY -= platformSpeed
                if (platformY < 0) { // Reset logic is now inside the loop
                    scoreState.value++
                    width = (Random.nextInt(maxWidth - minWidth) + minWidth).toFloat()
                    maxX = screenWidth - width * 2
                    platformY = screenHeight
                    platformX = Random.nextFloat() * maxX
                }
                delay(16) // Approximately 60 frames per second
            }
        }

        Canvas(modifier = Modifier.fillMaxSize()) {
            drawRect(
                color = Color.Green,
                topLeft = Offset(platformX, platformY),
                size = Size(width, platformHeight)
            )
        }
    }

    @Composable
    fun Bounce() {
//future feature
    }

    @Composable
    fun Gravity() {
//future feature
    }

    @Composable
    fun Death() {
//future feature
    }

    @Composable
    fun Score() {
        Text(
            text = "Score: ${scoreState.value}",
            modifier = Modifier
                .systemBarsPadding()
                .offset(16.dp, 0.dp)
        )
    }

    @Composable
    fun Player(xpos: Float, ypos: Int = 0) {
        playerX = xpos.toInt()
        playerY = ypos
        playerradius = 10.dp

        Canvas(modifier = Modifier.offset { IntOffset(xpos.roundToInt(), ypos) }) {
            drawCircle(
                color = Color.Red,
                radius = 10.dp.toPx(),
                center = Offset(10.dp.toPx(), 10.dp.toPx())
            )
        }
    }

    @Composable
    fun MusicPlayer(mp3Files: List<Int>? = null, songResourceId: Int? = null) {
        val context = LocalContext.current
        val lifecycleOwner = androidx.lifecycle.compose.LocalLifecycleOwner.current
        var mediaPlayer: MediaPlayer? by remember { mutableStateOf(null) }
        var currentSongIndex by remember { mutableStateOf(0) }
        var shuffledMp3Files by remember { mutableStateOf(mp3Files?.shuffled() ?: emptyList()) }

        val songNames = mapOf(
            R.raw.blue_skies to "Blue Skies",
            R.raw.carefree to "Carefree",
            R.raw.cipher to "Cipher",
            R.raw.dog_and_pony_show to "Dog and Pony Show",
            R.raw.easy_lemon to "Easy Lemon",
            R.raw.if_i_had_a_chicken to "If I Had a Chicken",
            R.raw.investigations to "Investigations",
            R.raw.local_elevator to "Local Elevator",
            R.raw.monkeys_spinning_monkeys to " Spinning Monkeys",
            R.raw.scheming_weasel_faster to "Scheming Weasel",
            R.raw.sneaky_adventure to "Sneaky Adventure",
            R.raw.sneaky_snitch to "Sneaky Snitch",
            R.raw.spring_in_my_step to "Spring in My Step",
            R.raw.sugar_zone to "Sugar Zone",
            R.raw.summer_smile to "Summer Smile",
            R.raw.wallpaper to "Wallpaper"
        )

        // Initialize MediaPlayer for single song
        LaunchedEffect(key1 = songResourceId) {
            if (songResourceId != null && shuffledMp3Files.isEmpty()) {
                mediaPlayer = MediaPlayer.create(context, songResourceId).apply {
                    isLooping = true
                }
            }
        }

        DisposableEffect(lifecycleOwner) {
            val observer = LifecycleEventObserver { _, event ->
                when (event) {
                    Lifecycle.Event.ON_START -> {
                        if (mp3Files != null) {
                            if (mediaPlayer == null || !mediaPlayer!!.isPlaying) {
                                mediaPlayer?.release()
                                if (shuffledMp3Files.isNotEmpty()) {
                                    mediaPlayer = MediaPlayer.create(
                                        context,
                                        shuffledMp3Files[currentSongIndex]
                                    ).apply {
                                        setOnCompletionListener {
                                            currentSongIndex =
                                                (currentSongIndex + 1) % shuffledMp3Files.size
                                            mediaPlayer?.release()
                                            mediaPlayer = MediaPlayer.create(
                                                context,
                                                shuffledMp3Files[currentSongIndex]
                                            )
                                            mediaPlayer?.start()
                                            currentSongNameState.value =
                                                songNames[shuffledMp3Files[currentSongIndex]]
                                                    ?: "Unknown Song"
                                            currentSongCountState++
                                        }
                                    }
                                    mediaPlayer?.start()
                                    currentSongNameState.value =
                                        songNames[shuffledMp3Files[currentSongIndex]]
                                            ?: "Unknown Song"
                                }
                            } else {
                                mediaPlayer?.start()
                            }
                        } else if (songResourceId != null) {
                            mediaPlayer?.start()
                        }
                    }

                    Lifecycle.Event.ON_PAUSE -> {
                        mediaPlayer?.pause()
                    }

                    Lifecycle.Event.ON_STOP -> {
                        mediaPlayer?.pause()
                    }

                    Lifecycle.Event.ON_RESUME -> {
                        if (mediaPlayer?.isPlaying == false) {
                            mediaPlayer?.start()
                        }
                    }

                    Lifecycle.Event.ON_DESTROY -> {
                        mediaPlayer?.release()
                        mediaPlayer = null
                    }

                    else -> {}
                }
            }
            lifecycleOwner.lifecycle.addObserver(observer)
            onDispose {
                lifecycleOwner.lifecycle.removeObserver(observer)
                mediaPlayer?.release()
                mediaPlayer = null
            }
        }
    }

//Start Of Debug Code
    var isDebugModeEnabled by mutableStateOf(false)
    @Composable
    fun DoDebug() {
        var tapCount by remember { mutableStateOf(0) }
        var lastTapTime by remember { mutableStateOf(0L) }
        val tapWindowMillis = 2000 // 2 seconds
        val requiredTaps = 10
        val coroutineScope = CoroutineScope(Dispatchers.Main)
        var resetJob: Job? by remember { mutableStateOf(null) }

        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Box(
                modifier = Modifier
                    .systemBarsPadding()
                    .offset(16.dp, 4.dp)
                    .size(44.dp, 16.dp)
                    .clickable {
                        val currentTime = System.currentTimeMillis()
                        if (currentTime - lastTapTime < tapWindowMillis) {
                            tapCount++
                        } else {
                            tapCount = 1
                        }
                        lastTapTime = currentTime

                        resetJob?.cancel()
                        resetJob = coroutineScope.launch {
                            delay(tapWindowMillis.toLong())
                            tapCount = 0
                        }

                        if (tapCount >= requiredTaps) {
                            isDebugModeEnabled = true
                            tapCount = 0
                        }
                    }
            )
        }
    }

    // Debug Settings Screen
    @Composable
    fun DebugSettingsScreen(navController: NavController) {
        Box(modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding()
        ) {
            var P1isChecked by remember { mutableStateOf(false) }
            var P2isChecked by remember { mutableStateOf(false) }
            var P3isChecked by remember { mutableStateOf(false) }
            var P4isChecked by remember { mutableStateOf(false) }
            var P5isChecked by remember { mutableStateOf(false) }
            var P6isChecked by remember { mutableStateOf(false) }
            var P7isChecked by remember { mutableStateOf(false) }
            var P8isChecked by remember { mutableStateOf(false) }
            var P9isChecked by remember { mutableStateOf(false) }
            var P10isChecked by remember { mutableStateOf(false) }
            if (whichP == 1) {
                P1isChecked = true
            } else if (whichP == 2) {
                P2isChecked = true
            } else if (whichP == 3) {
                P3isChecked = true
            } else if (whichP == 4) {
                P4isChecked = true
            } else if (whichP == 5) {
                P5isChecked = true
            } else if (whichP == 6) {
                P6isChecked = true
                } else if (whichP == 7) {
                P7isChecked = true
            } else if (whichP == 8) {
                P8isChecked = true
            } else if (whichP == 9) {
                P9isChecked = true
            } else if (whichP == 10) {
                P10isChecked = true
            }

            Text(
                text = "Which Platform",
                modifier = Modifier
                    .offset(16.dp, 0.dp)
            )
            Text(
                text = "Platform 1",
                modifier = Modifier
                    .offset(16.dp, 24.dp)
            )
            Switch(
                checked = P1isChecked,
                onCheckedChange = { P1isChecked = it
                    P2isChecked = false
                    P3isChecked = false
                    P4isChecked = false
                    P5isChecked = false
                    P6isChecked = false
                    P7isChecked = false
                    P8isChecked = false
                    P9isChecked = false
                    P10isChecked = false
                    whichP = 1 },
                colors = SwitchDefaults.colors(
                    checkedThumbColor = Color.White,
                    checkedTrackColor = Lightblue,
                    uncheckedThumbColor = Color.White,
                    uncheckedTrackColor = Lightgray,
                    disabledCheckedThumbColor = Lightgray,
                    disabledCheckedTrackColor = Lightgray,
                    disabledUncheckedThumbColor = Lightgray,
                    disabledUncheckedTrackColor = Lightgray
                ),
                modifier = Modifier
                    .offset(16.dp, 40.dp)
            )
            Text(
                text = "Platform 2",
                modifier = Modifier
                    .offset(16.dp, 84.dp)
            )
            Switch(
                checked = P2isChecked,
                onCheckedChange = { P2isChecked = it
                    P1isChecked = false
                    P3isChecked = false
                    P4isChecked = false
                    P5isChecked = false
                    P6isChecked = false
                    P7isChecked = false
                    P8isChecked = false
                    P9isChecked = false
                    P10isChecked = false
                    whichP = 2 },
                colors = SwitchDefaults.colors(
                    checkedThumbColor = Color.White,
                    checkedTrackColor = Lightblue,
                    uncheckedThumbColor = Color.White,
                    uncheckedTrackColor = Lightgray,
                    disabledCheckedThumbColor = Lightgray,
                    disabledCheckedTrackColor = Lightgray,
                    disabledUncheckedThumbColor = Lightgray,
                    disabledUncheckedTrackColor = Lightgray
                ),
                modifier = Modifier
                    .offset(16.dp, 100.dp)
            )
            Text(
                text = "Platform 3",
                modifier = Modifier
                    .offset(16.dp, 144.dp)
            )
            Switch(
                checked = P3isChecked,
                onCheckedChange = { P3isChecked = it
                    P1isChecked = false
                    P2isChecked = false
                    P4isChecked = false
                    P5isChecked = false
                    P6isChecked = false
                    P7isChecked = false
                    P8isChecked = false
                    P9isChecked = false
                    P10isChecked = false
                    whichP = 3 },
                colors = SwitchDefaults.colors(
                    checkedThumbColor = Color.White,
                    checkedTrackColor = Lightblue,
                    uncheckedThumbColor = Color.White,
                    uncheckedTrackColor = Lightgray,
                    disabledCheckedThumbColor = Lightgray,
                    disabledCheckedTrackColor = Lightgray,
                    disabledUncheckedThumbColor = Lightgray,
                    disabledUncheckedTrackColor = Lightgray
                ),
                modifier = Modifier
                    .offset(16.dp, 160.dp)
            )
            Text(
                text = "Platform 4",
                modifier = Modifier
                    .offset(16.dp, 204.dp)
            )
            Switch(
                checked = P4isChecked,
                onCheckedChange = { P4isChecked = it
                    P1isChecked = false
                    P2isChecked = false
                    P3isChecked = false
                    P5isChecked = false
                    P6isChecked = false
                    P7isChecked = false
                    P8isChecked = false
                    P9isChecked = false
                    P10isChecked = false
                    whichP = 4 },
                colors = SwitchDefaults.colors(
                    checkedThumbColor = Color.White,
                    checkedTrackColor = Lightblue,
                    uncheckedThumbColor = Color.White,
                    uncheckedTrackColor = Lightgray,
                    disabledCheckedThumbColor = Lightgray,
                    disabledCheckedTrackColor = Lightgray,
                    disabledUncheckedThumbColor = Lightgray,
                    disabledUncheckedTrackColor = Lightgray
                ),
                modifier = Modifier
                    .offset(16.dp, 220.dp)
            )
            Text(
                text = "Platform 5",
                modifier = Modifier
                    .offset(16.dp, 264.dp)
            )
            Switch(
                checked = P5isChecked,
                onCheckedChange = { P5isChecked = it
                    P1isChecked = false
                    P2isChecked = false
                    P3isChecked = false
                    P4isChecked = false
                    P6isChecked = false
                    P7isChecked = false
                    P8isChecked = false
                    P9isChecked = false
                    P10isChecked = false
                    whichP = 5 },
                colors = SwitchDefaults.colors(
                    checkedThumbColor = Color.White,
                    checkedTrackColor = Lightblue,
                    uncheckedThumbColor = Color.White,
                    uncheckedTrackColor = Lightgray,
                    disabledCheckedThumbColor = Lightgray,
                    disabledCheckedTrackColor = Lightgray,
                    disabledUncheckedThumbColor = Lightgray,
                    disabledUncheckedTrackColor = Lightgray
                ),
                modifier = Modifier
                    .offset(16.dp, 280.dp)
            )
            Text(
                text = "Platform 6",
                modifier = Modifier
                    .offset(16.dp, 324.dp)
            )
            Switch(
                checked = P6isChecked,
                onCheckedChange = { P6isChecked = it
                    P1isChecked = false
                    P2isChecked = false
                    P3isChecked = false
                    P4isChecked = false
                    P5isChecked = false
                    P7isChecked = false
                    P8isChecked = false
                    P9isChecked = false
                    P10isChecked = false
                    whichP = 6 },
                colors = SwitchDefaults.colors(
                    checkedThumbColor = Color.White,
                    checkedTrackColor = Lightblue,
                    uncheckedThumbColor = Color.White,
                    uncheckedTrackColor = Lightgray,
                    disabledCheckedThumbColor = Lightgray,
                    disabledCheckedTrackColor = Lightgray,
                    disabledUncheckedThumbColor = Lightgray,
                    disabledUncheckedTrackColor = Lightgray
                ),
                modifier = Modifier
                    .offset(16.dp, 340.dp)
            )
            Text(
                text = "Platform 7",
                modifier = Modifier
                    .offset(16.dp, 384.dp)
            )
            Switch(
                checked = P7isChecked,
                onCheckedChange = { P7isChecked = it
                    P1isChecked = false
                    P2isChecked = false
                    P3isChecked = false
                    P4isChecked = false
                    P5isChecked = false
                    P6isChecked = false
                    P8isChecked = false
                    P9isChecked = false
                    P10isChecked = false
                    whichP = 7 },
                colors = SwitchDefaults.colors(
                    checkedThumbColor = Color.White,
                    checkedTrackColor = Lightblue,
                    uncheckedThumbColor = Color.White,
                    uncheckedTrackColor = Lightgray,
                    disabledCheckedThumbColor = Lightgray,
                    disabledCheckedTrackColor = Lightgray,
                    disabledUncheckedThumbColor = Lightgray,
                    disabledUncheckedTrackColor = Lightgray
                ),
                modifier = Modifier
                    .offset(16.dp, 400.dp)
            )
            Text(
                text = "Platform 8",
                modifier = Modifier
                    .offset(16.dp, 444.dp)
            )
            Switch(
                checked = P8isChecked,
                onCheckedChange = { P8isChecked = it
                    P1isChecked = false
                    P2isChecked = false
                    P3isChecked = false
                    P4isChecked = false
                    P5isChecked = false
                    P6isChecked = false
                    P7isChecked = false
                    P9isChecked = false
                    P10isChecked = false
                    whichP = 8 },
                colors = SwitchDefaults.colors(
                    checkedThumbColor = Color.White,
                    checkedTrackColor = Lightblue,
                    uncheckedThumbColor = Color.White,
                    uncheckedTrackColor = Lightgray,
                    disabledCheckedThumbColor = Lightgray,
                    disabledCheckedTrackColor = Lightgray,
                    disabledUncheckedThumbColor = Lightgray,
                    disabledUncheckedTrackColor = Lightgray
                ),
                modifier = Modifier
                    .offset(16.dp, 460.dp)
            )
            Text(
                text = "Platform 9",
                modifier = Modifier
                    .offset(16.dp, 504.dp)
            )
            Switch(
                checked = P9isChecked,
                onCheckedChange = { P9isChecked = it
                    P1isChecked = false
                    P2isChecked = false
                    P3isChecked = false
                    P4isChecked = false
                    P5isChecked = false
                    P6isChecked = false
                    P7isChecked = false
                    P8isChecked = false
                    P10isChecked = false
                    whichP = 9 },
                colors = SwitchDefaults.colors(
                    checkedThumbColor = Color.White,
                    checkedTrackColor = Lightblue,
                    uncheckedThumbColor = Color.White,
                    uncheckedTrackColor = Lightgray,
                    disabledCheckedThumbColor = Lightgray,
                    disabledCheckedTrackColor = Lightgray,
                    disabledUncheckedThumbColor = Lightgray,
                    disabledUncheckedTrackColor = Lightgray
                ),
                modifier = Modifier
                    .offset(16.dp, 520.dp)
            )
            Text(
                text = "Platform 10",
                modifier = Modifier
                    .offset(16.dp, 564.dp)
            )
            Switch(
                checked = P10isChecked,
                onCheckedChange = { P10isChecked = it
                    P1isChecked = false
                    P2isChecked = false
                    P3isChecked = false
                    P4isChecked = false
                    P5isChecked = false
                    P6isChecked = false
                    P7isChecked = false
                    P8isChecked = false
                    P9isChecked = false
                    whichP = 10 },
                colors = SwitchDefaults.colors(
                    checkedThumbColor = Color.White,
                    checkedTrackColor = Lightblue,
                    uncheckedThumbColor = Color.White,
                    uncheckedTrackColor = Lightgray,
                    disabledCheckedThumbColor = Lightgray,
                    disabledCheckedTrackColor = Lightgray,
                    disabledUncheckedThumbColor = Lightgray,
                    disabledUncheckedTrackColor = Lightgray
                ),
                modifier = Modifier
                    .offset(16.dp, 580.dp)
            )
            Box(
                modifier = Modifier
                    .padding(16.dp)
                    .offset(0.dp, 0.dp)
                    .size(40.dp, 16.dp)
                    .align(Alignment.BottomEnd)
                    .clickable {
                        navController.navigate("game")
                    }
            )
            Text(
                text = "Back",
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .offset(-16.dp, -16.dp)
            )
        }
    }

    var currentSongNameState = mutableStateOf("No song playing")
    var currentSongCountState by mutableStateOf(0)
    var touchXdb by mutableStateOf(0)
    var whichP by mutableStateOf(1)

    @Composable
    fun Debug(navController: NavController) {
        val context = LocalContext.current
        val displayMetrics = context.resources.displayMetrics
        val screenWidth = displayMetrics.widthPixels.toFloat()
        val screenHeight = displayMetrics.heightPixels.toFloat()
        val density = LocalDensity.current
        val Dp = 5.dp
        val platformHeight = with(density) { Dp.toPx() }
        PH = platformHeight.toInt()

        Box(modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding()
        ) {
            Box(
                modifier = Modifier
                    .systemBarsPadding()
                    .offset(16.dp, 52.dp)
                    .size(96.dp, 16.dp)
                    .align(Alignment.TopStart)
                    .clickable {
                        navController.navigate("debugSettings")
                    }
            )
            Text(
                text = "Width: $screenWidth",
                modifier = Modifier
                    .systemBarsPadding()
                    .offset(16.dp, 16.dp)
            )
            Text(
                text = "Song: ${currentSongNameState.value}",
                modifier = Modifier
                    .systemBarsPadding()
                    .offset(16.dp, 32.dp)
            )
            Text(
                text = "Debug Menu",
                modifier = Modifier
                    .systemBarsPadding()
                    .offset(16.dp, 48.dp)
            )
            Text(
                text = "Touch X: $touchXdb",
                modifier = Modifier
                    .systemBarsPadding()
                    .align(Alignment.TopEnd)
                    .offset(-16.dp, 0.dp)
            )
            Text(
                text = "Height: $screenHeight",
                modifier = Modifier
                    .systemBarsPadding()
                    .align(Alignment.TopEnd)
                    .offset(-16.dp, 16.dp)
            )
            Text(
                text = "Song#: $currentSongCountState",
                modifier = Modifier
                    .systemBarsPadding()
                    .align(Alignment.TopEnd)
                    .offset(-16.dp, 32.dp)
            )
            Text(
                text = "Which Platform: $whichP",
                modifier = Modifier
                    .systemBarsPadding()
                    .align(Alignment.TopEnd)
                    .offset(-16.dp, 48.dp)
            )
        }
        Box(modifier = Modifier
            .fillMaxSize()
        )
        if (whichP == 1) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                drawLine(
                    color = Color.Red,
                    start = Offset(0f, screenHeight / 2),
                    end = Offset(screenWidth, screenHeight / 2),
                    strokeWidth = 5f
                )
                drawLine(
                    color = Color.White,
                    start = Offset(screenWidth / 2, 0f),
                    end = Offset(screenWidth / 2, screenHeight),
                    strokeWidth = 5f
                )
                drawLine(
                    color = Color.Green,
                    start = Offset(P1X.toFloat(), 0f),
                    end = Offset(P1X.toFloat(), screenHeight),
                    strokeWidth = 5f
                )
                drawLine(
                    color = Color.Cyan,
                    start = Offset(MP1X.toFloat(), 0f),
                    end = Offset(MP1X.toFloat(), screenHeight),
                    strokeWidth = 5f
                )
                drawRect(
                    color = Color.Yellow,
                    topLeft = Offset(P1X.toFloat(), P1Y.toFloat()),
                    size = Size(P1W.toFloat(), platformHeight)
                )
            }
            if (MP1X <= (P1X + P1W)) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    drawRect(
                        color = Color.Blue,
                        topLeft = Offset(MP1X.toFloat(), (P1Y + platformHeight)),
                        size = Size(P1W.toFloat(), platformHeight)
                    )
                }
            } else {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    drawRect(
                        color = Color.Blue,
                        topLeft = Offset(MP1X.toFloat(), (P1Y).toFloat()),
                        size = Size(P1W.toFloat(), platformHeight)
                    )
                }
            }
        } else if (whichP == 2) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                drawLine(
                    color = Color.Red,
                    start = Offset(0f, screenHeight / 2),
                    end = Offset(screenWidth, screenHeight / 2),
                    strokeWidth = 5f
                )
                drawLine(
                    color = Color.White,
                    start = Offset(screenWidth / 2, 0f),
                    end = Offset(screenWidth / 2, screenHeight),
                    strokeWidth = 5f
                )
                drawLine(
                    color = Color.Green,
                    start = Offset(P2X.toFloat(), 0f),
                    end = Offset(P2X.toFloat(), screenHeight),
                    strokeWidth = 5f
                )
                drawLine(
                    color = Color.Cyan,
                    start = Offset(MP2X.toFloat(), 0f),
                    end = Offset(MP2X.toFloat(), screenHeight),
                    strokeWidth = 5f
                )
                drawRect(
                    color = Color.Yellow,
                    topLeft = Offset(P2X.toFloat(), P2Y.toFloat()),
                    size = Size(P2W.toFloat(), platformHeight)
                )
            }
            if (MP2X <= (P2X + P2W)) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    drawRect(
                        color = Color.Blue,
                        topLeft = Offset(MP2X.toFloat(), (P2Y + platformHeight)),
                        size = Size(P2W.toFloat(), platformHeight)
                    )
                }
            } else {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    drawRect(
                        color = Color.Blue,
                        topLeft = Offset(MP2X.toFloat(), (P2Y).toFloat()),
                        size = Size(P2W.toFloat(), platformHeight)
                    )
                }
            }
        } else if (whichP == 3) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                drawLine(
                    color = Color.Red,
                    start = Offset(0f, screenHeight / 2),
                    end = Offset(screenWidth, screenHeight / 2),
                    strokeWidth = 5f
                )
                drawLine(
                    color = Color.White,
                    start = Offset(screenWidth / 2, 0f),
                    end = Offset(screenWidth / 2, screenHeight),
                    strokeWidth = 5f
                )
                drawLine(
                    color = Color.Green,
                    start = Offset(P3X.toFloat(), 0f),
                    end = Offset(P3X.toFloat(), screenHeight),
                    strokeWidth = 5f
                )
                drawLine(
                    color = Color.Cyan,
                    start = Offset(MP3X.toFloat(), 0f),
                    end = Offset(MP3X.toFloat(), screenHeight),
                    strokeWidth = 5f
                )
                drawRect(
                    color = Color.Yellow,
                    topLeft = Offset(P3X.toFloat(), P3Y.toFloat()),
                    size = Size(P3W.toFloat(), platformHeight)
                )
            }
            if (MP3X <= (P3X + P3W)) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    drawRect(
                        color = Color.Blue,
                        topLeft = Offset(MP3X.toFloat(), (P3Y + platformHeight)),
                        size = Size(P3W.toFloat(), platformHeight)
                    )
                }
            } else {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    drawRect(
                        color = Color.Blue,
                        topLeft = Offset(MP3X.toFloat(), (P3Y).toFloat()),
                        size = Size(P3W.toFloat(), platformHeight)
                    )
                }
            }
        } else if (whichP == 4) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                drawLine(
                    color = Color.Red,
                    start = Offset(0f, screenHeight / 2),
                    end = Offset(screenWidth, screenHeight / 2),
                    strokeWidth = 5f
                )
                drawLine(
                    color = Color.White,
                    start = Offset(screenWidth / 2, 0f),
                    end = Offset(screenWidth / 2, screenHeight),
                    strokeWidth = 5f
                )
                drawLine(
                    color = Color.Green,
                    start = Offset(P4X.toFloat(), 0f),
                    end = Offset(P4X.toFloat(), screenHeight),
                    strokeWidth = 5f
                )
                drawLine(
                    color = Color.Cyan,
                    start = Offset(MP4X.toFloat(), 0f),
                    end = Offset(MP4X.toFloat(), screenHeight),
                    strokeWidth = 5f
                )
                drawRect(
                    color = Color.Yellow,
                    topLeft = Offset(P4X.toFloat(), P4Y.toFloat()),
                    size = Size(P4W.toFloat(), platformHeight)
                )
            }
            if (MP4X <= (P4X + P4W)) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    drawRect(
                        color = Color.Blue,
                        topLeft = Offset(MP4X.toFloat(), (P4Y + platformHeight)),
                        size = Size(P4W.toFloat(), platformHeight)
                    )
                }
            } else {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    drawRect(
                        color = Color.Blue,
                        topLeft = Offset(MP4X.toFloat(), (P4Y).toFloat()),
                        size = Size(P4W.toFloat(), platformHeight)
                    )
                }
            }
        } else if (whichP == 5) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                drawLine(
                    color = Color.Red,
                    start = Offset(0f, screenHeight / 2),
                    end = Offset(screenWidth, screenHeight / 2),
                    strokeWidth = 5f
                )
                drawLine(
                    color = Color.White,
                    start = Offset(screenWidth / 2, 0f),
                    end = Offset(screenWidth / 2, screenHeight),
                    strokeWidth = 5f
                )
                drawLine(
                    color = Color.Green,
                    start = Offset(P5X.toFloat(), 0f),
                    end = Offset(P5X.toFloat(), screenHeight),
                    strokeWidth = 5f
                )
                drawLine(
                    color = Color.Cyan,
                    start = Offset(MP5X.toFloat(), 0f),
                    end = Offset(MP5X.toFloat(), screenHeight),
                    strokeWidth = 5f
                )
                drawRect(
                    color = Color.Yellow,
                    topLeft = Offset(P5X.toFloat(), P5Y.toFloat()),
                    size = Size(P5W.toFloat(), platformHeight)
                )
            }
            if (MP5X <= (P5X + P5W)) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    drawRect(
                        color = Color.Blue,
                        topLeft = Offset(MP5X.toFloat(), (P5Y + platformHeight)),
                        size = Size(P5W.toFloat(), platformHeight)
                    )
                }
            } else {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    drawRect(
                        color = Color.Blue,
                        topLeft = Offset(MP5X.toFloat(), (P5Y).toFloat()),
                        size = Size(P5W.toFloat(), platformHeight)
                    )
                }
            }
        } else if (whichP == 6) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                drawLine(
                    color = Color.Red,
                    start = Offset(0f, screenHeight / 2),
                    end = Offset(screenWidth, screenHeight / 2),
                    strokeWidth = 5f
                )
                drawLine(
                    color = Color.White,
                    start = Offset(screenWidth / 2, 0f),
                    end = Offset(screenWidth / 2, screenHeight),
                    strokeWidth = 5f
                )
                drawLine(
                    color = Color.Green,
                    start = Offset(P6X.toFloat(), 0f),
                    end = Offset(P6X.toFloat(), screenHeight),
                    strokeWidth = 5f
                )
                drawLine(
                    color = Color.Cyan,
                    start = Offset(MP6X.toFloat(), 0f),
                    end = Offset(MP6X.toFloat(), screenHeight),
                    strokeWidth = 5f
                )
                drawRect(
                    color = Color.Yellow,
                    topLeft = Offset(P6X.toFloat(), P6Y.toFloat()),
                    size = Size(P6W.toFloat(), platformHeight)
                )
            }
            if (MP6X <= (P6X + P6W)) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    drawRect(
                        color = Color.Blue,
                        topLeft = Offset(MP6X.toFloat(), (P6Y + platformHeight)),
                        size = Size(P6W.toFloat(), platformHeight)
                    )
                }
            } else {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    drawRect(
                        color = Color.Blue,
                        topLeft = Offset(MP6X.toFloat(), (P6Y).toFloat()),
                        size = Size(P6W.toFloat(), platformHeight)
                    )
                }
            }
        } else if (whichP == 7) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                drawLine(
                    color = Color.Red,
                    start = Offset(0f, screenHeight / 2),
                    end = Offset(screenWidth, screenHeight / 2),
                    strokeWidth = 5f
                )
                drawLine(
                    color = Color.White,
                    start = Offset(screenWidth / 2, 0f),
                    end = Offset(screenWidth / 2, screenHeight),
                    strokeWidth = 5f
                )
                drawLine(
                    color = Color.Green,
                    start = Offset(P7X.toFloat(), 0f),
                    end = Offset(P7X.toFloat(), screenHeight),
                    strokeWidth = 5f
                )
                drawLine(
                    color = Color.Cyan,
                    start = Offset(MP7X.toFloat(), 0f),
                    end = Offset(MP7X.toFloat(), screenHeight),
                    strokeWidth = 5f
                )
                drawRect(
                    color = Color.Yellow,
                    topLeft = Offset(P7X.toFloat(), P7Y.toFloat()),
                    size = Size(P7W.toFloat(), platformHeight)
                )
            }
            if (MP7X <= (P7X + P7W)) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    drawRect(
                        color = Color.Blue,
                        topLeft = Offset(MP7X.toFloat(), (P7Y + platformHeight)),
                        size = Size(P7W.toFloat(), platformHeight)
                    )
                }
            } else {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    drawRect(
                        color = Color.Blue,
                        topLeft = Offset(MP7X.toFloat(), (P7Y).toFloat()),
                        size = Size(P7W.toFloat(), platformHeight)
                    )
                }
            }
        } else if (whichP == 8) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                drawLine(
                    color = Color.Red,
                    start = Offset(0f, screenHeight / 2),
                    end = Offset(screenWidth, screenHeight / 2),
                    strokeWidth = 5f
                )
                drawLine(
                    color = Color.White,
                    start = Offset(screenWidth / 2, 0f),
                    end = Offset(screenWidth / 2, screenHeight),
                    strokeWidth = 5f
                )
                drawLine(
                    color = Color.Green,
                    start = Offset(P8X.toFloat(), 0f),
                    end = Offset(P8X.toFloat(), screenHeight),
                    strokeWidth = 5f
                )
                drawLine(
                    color = Color.Cyan,
                    start = Offset(MP8X.toFloat(), 0f),
                    end = Offset(MP8X.toFloat(), screenHeight),
                    strokeWidth = 5f
                )
                drawRect(
                    color = Color.Yellow,
                    topLeft = Offset(P8X.toFloat(), P8Y.toFloat()),
                    size = Size(P8W.toFloat(), platformHeight)
                )
            }
            if (MP8X <= (P8X + P8W)) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    drawRect(
                        color = Color.Blue,
                        topLeft = Offset(MP8X.toFloat(), (P8Y + platformHeight)),
                        size = Size(P8W.toFloat(), platformHeight)
                    )
                }
            } else {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    drawRect(
                        color = Color.Blue,
                        topLeft = Offset(MP8X.toFloat(), (P8Y).toFloat()),
                        size = Size(P8W.toFloat(), platformHeight)
                    )
                }
            }
        } else if (whichP == 9) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                drawLine(
                    color = Color.Red,
                    start = Offset(0f, screenHeight / 2),
                    end = Offset(screenWidth, screenHeight / 2),
                    strokeWidth = 5f
                )
                drawLine(
                    color = Color.White,
                    start = Offset(screenWidth / 2, 0f),
                    end = Offset(screenWidth / 2, screenHeight),
                    strokeWidth = 5f
                )
                drawLine(
                    color = Color.Green,
                    start = Offset(P9X.toFloat(), 0f),
                    end = Offset(P9X.toFloat(), screenHeight),
                    strokeWidth = 5f
                )
                drawLine(
                    color = Color.Cyan,
                    start = Offset(MP9X.toFloat(), 0f),
                    end = Offset(MP9X.toFloat(), screenHeight),
                    strokeWidth = 5f
                )
                drawRect(
                    color = Color.Yellow,
                    topLeft = Offset(P8X.toFloat(), P1Y.toFloat()),
                    size = Size(P9W.toFloat(), platformHeight)
                )
            }
            if (MP9X <= (P9X + P9W)) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    drawRect(
                        color = Color.Blue,
                        topLeft = Offset(MP9X.toFloat(), (P9Y + platformHeight)),
                        size = Size(P9W.toFloat(), platformHeight)
                    )
                }
            } else {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    drawRect(
                        color = Color.Blue,
                        topLeft = Offset(MP9X.toFloat(), (P9Y).toFloat()),
                        size = Size(P9W.toFloat(), platformHeight)
                    )
                }
            }
        } else if (whichP == 10) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                drawLine(
                    color = Color.Red,
                    start = Offset(0f, screenHeight / 2),
                    end = Offset(screenWidth, screenHeight / 2),
                    strokeWidth = 5f
                )
                drawLine(
                    color = Color.White,
                    start = Offset(screenWidth / 2, 0f),
                    end = Offset(screenWidth / 2, screenHeight),
                    strokeWidth = 5f
                )
                drawLine(
                    color = Color.Green,
                    start = Offset(P10X.toFloat(), 0f),
                    end = Offset(P10X.toFloat(), screenHeight),
                    strokeWidth = 5f
                )
                drawLine(
                    color = Color.Cyan,
                    start = Offset(MP10X.toFloat(), 0f),
                    end = Offset(MP10X.toFloat(), screenHeight),
                    strokeWidth = 5f
                )
                drawRect(
                    color = Color.Yellow,
                    topLeft = Offset(P10X.toFloat(), P1Y.toFloat()),
                    size = Size(P1W.toFloat(), platformHeight)
                )
            }
            if (MP10X <= (P10X + P10W)) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    drawRect(
                        color = Color.Blue,
                        topLeft = Offset(MP10X.toFloat(), (P10Y + platformHeight)),
                        size = Size(P10W.toFloat(), platformHeight)
                    )
                }
            } else {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    drawRect(
                        color = Color.Blue,
                        topLeft = Offset(MP10X.toFloat(), (P10Y).toFloat()),
                        size = Size(P10W.toFloat(), platformHeight)
                    )
                }
            }
        }
    }
}