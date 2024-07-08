package com.example.sports

import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.sports.api.SportsApi
import com.example.sports.data.DatabaseHelper
import com.example.sports.data.Sport
import com.example.sports.ui.MainActivity
import com.example.sports.ui.adapters.SportAdapter
import com.example.sports.viewmodel.MainViewModel
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.mock

@RunWith(AndroidJUnit4::class)
class MainActivityTest {

    @get:Rule
    var activityRule: ActivityScenarioRule<MainActivity> = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun testFilterStarred() {
        val sports = listOf(
            Sport("FOOT", "SOCCER", emptyList(), false, false),
            Sport("BASK", "BASKETBALL", emptyList(), true, true)
        )

        activityRule.scenario.onActivity { activity ->
            val sportsApi = mock(SportsApi::class.java)
            val dbHelper = DatabaseHelper(activity)
            val viewModel = MainViewModel(sportsApi, dbHelper)

            val sportAdapter = SportAdapter(sports, viewModel)
            activity.sportAdapter = sportAdapter
            activity.recyclerView.adapter = sportAdapter

            assertEquals(2, activity.recyclerView.adapter?.itemCount)

            sportAdapter.filterStarred(true)

            assertEquals(1, activity.recyclerView.adapter?.itemCount)
        }
    }
}