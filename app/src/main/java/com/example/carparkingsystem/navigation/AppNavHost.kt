package com.example.carparkingsystem.navigation


import androidx.compose.runtime.Composable

import androidx.compose.ui.tooling.preview.Preview

import androidx.navigation.NavHostController

import androidx.navigation.compose.NavHost

import androidx.navigation.compose.composable

import androidx.navigation.compose.rememberNavController

import com.example.carparkingsystem.ui.theme.CarParkingSystemTheme

import com.example.carparkingsystem.ui.theme.screens.car.AppCarScreen
import com.example.carparkingsystem.ui.theme.screens.car.CarListScreen

import com.example.carparkingsystem.ui.theme.screens.dashboard.Dashboard

import com.example.carparkingsystem.ui.theme.screens.login.LoginScreen

import com.example.carparkingsystem.ui.theme.screens.register.RegisterScreen
import com.example.carparkingsystem.ui.theme.screens.splash.SplashScreen


@Composable

fun AppNavHost(

    navController: NavHostController = rememberNavController(),

    startDestination: String = ROUTE_REGISTER

) {

    NavHost(

        navController = navController,

        startDestination = startDestination

    ) {



// 🟢 Register Screen

        composable(ROUTE_REGISTER) {

            RegisterScreen(navController)

        }


// 🔵 Login Screen

        composable(ROUTE_LOGIN) {

            LoginScreen(navController)

        }


// 🟣 Dashboard

        composable(ROUTE_DASHBOARD) {

            Dashboard(navController)

        }


// 🚗 Add Car Screen

        composable(ROUTE_ADDCAR) {

            AppCarScreen(navController)

        }

        composable( ROUTE_VIEWCARS ){
            CarListScreen(navController)
        }

        composable(ROUTE_SPLASH) {
            SplashScreen(navController = navController)
        }

    }

} 