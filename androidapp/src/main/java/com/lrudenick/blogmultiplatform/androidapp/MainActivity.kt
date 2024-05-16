package com.lrudenick.blogmultiplatform.androidapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController
import com.lrudenick.blogmultiplatform.androidapp.navigation.SetupNavGraph
import com.lrudenick.blogmultiplatform.androidapp.ui.theme.BlogMultiplatformTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BlogMultiplatformTheme {
                val navController = rememberNavController()
                SetupNavGraph(navController = navController)
            }
        }
    }
}