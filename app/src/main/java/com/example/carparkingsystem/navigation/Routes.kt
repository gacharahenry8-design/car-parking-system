package com.example.carparkingsystem.navigation

const val ROUTE_REGISTER = "register"
const val ROUTE_LOGIN = "login"
const val ROUTE_DASHBOARD = "dashboard"
const val ROUTE_ADDCAR = "addcar"
const val ROUTE_VIEWCARS = "viewcars"
const val ROUTE_SPLASH = "splash"
const val ROUTE_UPDATE_CAR = "update_car/{carId}"

fun buildUpdateCarRoute(carId: String): String {
    return "update_car/$carId"
}