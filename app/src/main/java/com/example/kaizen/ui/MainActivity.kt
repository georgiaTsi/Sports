package com.example.kaizen.ui;

import android.os.Bundle
import android.view.View
import android.widget.CheckBox
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kaizen.data.DatabaseHelper
import com.example.kaizen.R
import com.example.kaizen.ui.adapters.SportAdapter
import com.example.kaizen.api.RetrofitInstance
import com.example.kaizen.api.SportsApi
import com.example.kaizen.viewmodel.MainViewModel
import com.example.kaizen.viewmodel.MainViewModelFactory
import com.example.kaizen.viewmodel.Resource

class MainActivity : AppCompatActivity() {

    private lateinit var sportAdapter: SportAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var linearMain: LinearLayout
    private lateinit var txtNoEvents: TextView

    private lateinit var viewModel: MainViewModel

    lateinit var dbHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        dbHelper = DatabaseHelper(this)

        recyclerView = findViewById(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this)

        progressBar = findViewById(R.id.progress_bar)
        linearMain = findViewById(R.id.linear_main)
        txtNoEvents = findViewById(R.id.textview_no_events)

        val starCheckbox = findViewById<CheckBox>(R.id.checkbox_star)
        starCheckbox.setOnClickListener {
            sportAdapter.filterStarred(starCheckbox.isChecked)
        }

        observeSports()
    }

    private fun observeSports() {
        val sportsApi = RetrofitInstance.getInstance().create(SportsApi::class.java)
        val dbHelper = DatabaseHelper(this)

        viewModel = ViewModelProvider(
            this,
            MainViewModelFactory(sportsApi, dbHelper)
        )[MainViewModel::class.java]

        viewModel.sports.observe(this) { resource ->
            when (resource) {
                is Resource.Loading -> {
                    progressBar.visibility = View.VISIBLE
                    linearMain.visibility = View.GONE
                    txtNoEvents.visibility = View.GONE
                }

                is Resource.Success -> {
                    progressBar.visibility = View.GONE

                    if (resource.data.isNullOrEmpty()) {
                        linearMain.visibility = View.GONE
                        txtNoEvents.visibility = View.VISIBLE
                        txtNoEvents.setText(R.string.no_events)
                    } else {
                        linearMain.visibility = View.VISIBLE
                        txtNoEvents.visibility = View.GONE

                        // Update RecyclerView adapter with resource.data
                        sportAdapter = SportAdapter(resource.data, viewModel)
                        recyclerView.adapter = sportAdapter
                        sportAdapter.notifyDataSetChanged()
                    }
                }

                is Resource.Error -> {
                    progressBar.visibility = View.GONE
                    linearMain.visibility = View.GONE
                    txtNoEvents.visibility = View.VISIBLE
                    txtNoEvents.text = resource.message

                    Toast.makeText(this, resource.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
