package com.example.kaizen

import androidx.test.core.app.ActivityScenario
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.*
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.runner.RunWith
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import com.example.kaizen.data.DatabaseHelper
import com.example.kaizen.ui.MainActivity
import org.junit.Before
import org.junit.Test

@RunWith(AndroidJUnit4::class)
class MainActivityTest {

    private lateinit var scenario: ActivityScenario<MainActivity>
    private lateinit var dbHelper: DatabaseHelper

    @Before
    fun setup() {
        scenario = ActivityScenario.launch(MainActivity::class.java)
        scenario.onActivity { activity ->
            dbHelper = activity.dbHelper
        }
    }

    @After
    fun teardown() {
        scenario.close()
    }

    @Test
    fun testRecyclerViewVisibility_whenDataIsLoaded() {
        // Mock API response or use a test data set TODO

        onView(withId(R.id.recycler_view)).check(matches(isDisplayed()))
    }

    @Test
    fun testEmptyStateVisibility_whenNoData() {
        // Mock an empty API response TODO

        onView(withId(R.id.textview_no_events)).check(matches(isDisplayed()))
    }

    @Test
    fun testApiErrorStateVisibility_whenApiFails() {
        // Mock an API error response TODO

        onView(withId(R.id.textview_no_events)).check(matches(withText(R.string.api_error)))
    }

    @Test
    fun testStarFilter_updatesRecyclerView() {//TODO
        // Mock API response with some starred and non-starred sports

        onView(withId(R.id.checkbox_star)).perform(click())

        // Verify that the RecyclerView only shows starred sports
        // ... (You'll need to adapt this based on your adapter's filtering logic)
    }

    @Test
    fun testAddFavoriteSport_updatesDatabase() {
        val sportName = "Test Sport"

        scenario.onActivity { activity ->//TODO
//            activity.addFavoriteSport(sportName)
        }

        // Verify that the sport is added to the database
        val favoriteSports = dbHelper.getAllFavoriteSports()
        assertTrue(favoriteSports.contains(sportName))
    }

    @Test
    fun testRemoveFavoriteSport_updatesDatabase() {
        val sportName = "Test Sport"

        dbHelper.addFavoriteSport(sportName)

        scenario.onActivity { activity ->//TODO
//            activity.removeFavoriteSport(sportName)
        }

        // Verify that the sport is removed from the database
        val favoriteSports = dbHelper.getAllFavoriteSports()
        assertFalse(favoriteSports.contains(sportName))
    }
}