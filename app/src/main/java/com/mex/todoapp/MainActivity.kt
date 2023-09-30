package com.mex.todoapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.mex.todoapp.navigation.ToDoNavHost
import com.mex.todoapp.theme.MexTodoAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()

            MexTodoAppTheme {
                Scaffold(
//                        floatingActionButton = {
//                            FloatingActionButton(onClick = { navController.navigateToCreateChat() }) {
//                                Icon(
//                                    painter = painterResource(id = android.R.drawable.arrow_up_float),
//                                    contentDescription = ""
//                                )
//                            }
//                        },
//                        floatingActionButtonPosition = FabPosition.End
                ) { innerPadding ->
                    ToDoNavHost(
                        modifier = Modifier.padding(innerPadding),
                        navController = navController,
                    )
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MexTodoAppTheme {
        Greeting("Android")
    }
}