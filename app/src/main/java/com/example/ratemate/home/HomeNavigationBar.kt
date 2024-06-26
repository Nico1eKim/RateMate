package com.example.ratemate.home

import SurveyListScreen
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.colorResource
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.navArgument
import com.example.ratemate.R
import com.example.ratemate.Store.StoreScreen
import com.example.ratemate.myPage.MyPageScreen
import com.example.ratemate.setting.Option
import com.example.ratemate.survey.AnswerSurveyScreen
import com.example.ratemate.survey.CreateSurveyScreen
import com.example.ratemate.survey.SurveyResultScreen


sealed class HomeNavRoutes(val route: String) {
    object Home : HomeNavRoutes("Home")
    object Question : HomeNavRoutes("Question")
    object MyPage : HomeNavRoutes("MyPage")
    object Shop : HomeNavRoutes("Shop")
    object Option : HomeNavRoutes("Option")
    object AnswerSurvey : HomeNavRoutes("AnswerSurvey")
    object SurveyResult : HomeNavRoutes("SurveyResult")
}

@Composable
fun HomeNavigationHost(navController: NavHostController, startnavController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = HomeNavRoutes.Home.route
    ) {
        composable(HomeNavRoutes.Home.route) {
            SurveyListScreen(navController)
        }
        composable(HomeNavRoutes.Question.route) {
            CreateSurveyScreen(startnavController)
        }
        composable(HomeNavRoutes.MyPage.route) {
            MyPageScreen(startnavController)
        }
        composable(HomeNavRoutes.Shop.route) {
            StoreScreen(navController)
        }
        composable(HomeNavRoutes.Option.route) {
            Option(startnavController)
        }

        composable(
            HomeNavRoutes.AnswerSurvey.route + "/{SurveyID}",
            arguments = listOf(
                navArgument(name = "SurveyID") {
                    type = NavType.StringType
                }
            )) {
            AnswerSurveyScreen(
                navController = navController,
                surveyId = it.arguments?.getString("SurveyID")
            )
        }

        composable(
            HomeNavRoutes.SurveyResult.route + "/{SurveyID}",
            arguments = listOf(
                navArgument(name = "SurveyID") {
                    type = NavType.StringType
                }
            )
        ) {
            SurveyResultScreen(
                navController = navController,
                SurveyID = it.arguments?.getString("SurveyID")
            )
        }

    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomeNavigationBar(navController: NavController) {

    NavigationBar(
        containerColor = colorResource(R.color.gray_50)
    ) {
        val backStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = backStackEntry?.destination?.route

        HomeNavBarItems.HomeBarItems.forEach { navItem ->
            NavigationBarItem(
                selected = currentRoute == navItem.route,
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = colorResource(R.color.gray_50),
                    selectedIconColor = colorResource(R.color.main_blue_300),
                    selectedTextColor = colorResource(R.color.main_blue_300),
                    unselectedIconColor = colorResource(R.color.gray_700),
                    unselectedTextColor = colorResource(R.color.gray_700)
                ),
                onClick = {
                    navController.navigate(navItem.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = {
                    Icon(
                        imageVector = if (currentRoute == navItem.route) navItem.onSelectedIcon else navItem.selectIcon,
                        contentDescription = navItem.title
                    )
                },
                label = {
                    Text(text = navItem.title)
                }
            )
        }
    }
}