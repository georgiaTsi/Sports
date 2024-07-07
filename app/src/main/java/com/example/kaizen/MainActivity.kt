package com.example.kaizen;

import android.os.Bundle
import android.view.View
import android.widget.CheckBox
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kaizen.api.RetrofitInstance
import com.example.kaizen.model.Event
import com.example.kaizen.model.Sport
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity(), FavoriteSportListener {

    private lateinit var sportAdapter: SportAdapter
    private lateinit var recyclerView: RecyclerView
    private val sports = mutableListOf<Sport>()

    private lateinit var database: AppDatabase
    private lateinit var favoriteSportDao: FavoriteSportDao
    private lateinit var adapter: EventAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        database = AppDatabase.getDatabase(this)
//        favoriteSportDao = database.favoriteSportDao()

        recyclerView = findViewById(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val starCheckbox = findViewById<CheckBox>(R.id.checkbox_star)
        starCheckbox.setOnClickListener{ v ->
            sportAdapter.filterStarred(starCheckbox.isChecked)
        }

        fetchSports()
    }

    private fun fetchSports() {
        val sportsApi: SportsApi = RetrofitInstance.getInstance().create(SportsApi::class.java)

        CoroutineScope(Dispatchers.IO).launch { //CoroutineScope to launch a coroutine in the IO context
            try {
                val response = sportsApi.getSportsEvents().execute()
                if (response.isSuccessful) {
                    if(sports.isNotEmpty()){
                        withContext(Dispatchers.Main) {
                            findViewById<LinearLayout>(R.id.linear_main).visibility = View.GONE
                            findViewById<TextView>(R.id.textview_no_events).visibility = View.VISIBLE
                        }

                        return@launch
                    }

                    sports.addAll(response.body()!!)

                    withContext(Dispatchers.Main) { //switch to Main context to update UI
                        sportAdapter = SportAdapter(sports, this@MainActivity)
                        recyclerView.adapter = sportAdapter
                        sportAdapter.notifyDataSetChanged()
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        withContext(Dispatchers.Main) {
                            findViewById<LinearLayout>(R.id.linear_main).visibility = View.GONE

                            val txtNoEvents = findViewById<TextView>(R.id.textview_no_events)
                            txtNoEvents.visibility = View.VISIBLE
                            txtNoEvents.setText(R.string.api_error)
                        }
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@MainActivity, "Error: " + e.message, Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
    }

    override fun addFavoriteSport(sportName: String) {
        CoroutineScope(Dispatchers.IO).launch {
//            favoriteSportDao.insert(FavoriteSport(sportName = sportName))
        }
    }

    override fun removeFavoriteSport(sportName: String) {
        CoroutineScope(Dispatchers.IO).launch {
//            favoriteSportDao.deleteBySportName(sportName)
        }
    }
}
