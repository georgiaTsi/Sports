package com.example.sports.ui;

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
import com.example.sports.R
import com.example.sports.data.DatabaseHelper
import com.example.sports.ui.adapters.SportAdapter
import com.example.sports.api.RetrofitInstance
import com.example.sports.api.SportsApi
import com.example.sports.databinding.ActivityMainBinding
import com.example.sports.viewmodel.MainViewModel
import com.example.sports.viewmodel.MainViewModelFactory
import com.example.sports.viewmodel.Resource

class MainActivity : AppCompatActivity() {

    lateinit var sportAdapter: SportAdapter
    lateinit var recyclerView: RecyclerView
    lateinit var progressBar: ProgressBar
    lateinit var linearMain: LinearLayout
    lateinit var txtNoEvents: TextView

    lateinit var viewModel: MainViewModel

    lateinit var dbHelper: DatabaseHelper

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dbHelper = DatabaseHelper(this)

        recyclerView = binding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(this)

        progressBar = binding.progressBar
        linearMain = binding.linearMain
        txtNoEvents = binding.textviewNoEvents

        val starCheckbox = binding.checkboxStar
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
