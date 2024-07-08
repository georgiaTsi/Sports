package com.example.sports.ui

import android.os.Bundle
import android.view.View
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

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        recyclerView = binding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(this)

        val starCheckbox = binding.checkboxStar
        starCheckbox.setOnClickListener {
            sportAdapter.filterStarred(starCheckbox.isChecked)
        }

        observeSports()
    }

    private fun observeSports() {
        val sportsApi = RetrofitInstance.getInstance().create(SportsApi::class.java)
        val dbHelper = DatabaseHelper(this)

        val viewModel = ViewModelProvider(
            this,
            MainViewModelFactory(sportsApi, dbHelper)
        )[MainViewModel::class.java]

        viewModel.sports.observe(this) { resource ->
            when (resource) {
                is Resource.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                    binding.linearMain.visibility = View.GONE
                    binding.textviewNoEvents.visibility = View.GONE
                }

                is Resource.Success -> {
                    binding.progressBar.visibility = View.GONE

                    if (resource.data.isNullOrEmpty()) {
                        binding.linearMain.visibility = View.GONE
                        binding.textviewNoEvents.visibility = View.VISIBLE
                        binding.textviewNoEvents.setText(R.string.no_events)
                    } else {
                        binding.linearMain.visibility = View.VISIBLE
                        binding.textviewNoEvents.visibility = View.GONE

                        // Update RecyclerView adapter with resource.data
                        sportAdapter = SportAdapter(resource.data, viewModel)
                        recyclerView.adapter = sportAdapter
                        sportAdapter.notifyDataSetChanged()
                    }
                }

                is Resource.Error -> {
                    binding.progressBar.visibility = View.GONE
                    binding.linearMain.visibility = View.GONE
                    binding.textviewNoEvents.visibility = View.VISIBLE
                    binding.textviewNoEvents.text = resource.message

                    Toast.makeText(this, resource.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
