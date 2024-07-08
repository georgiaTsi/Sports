package com.example.sports.ui.adapters

import android.content.Context
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.sports.data.DatabaseHelper
import com.example.sports.R
import com.example.sports.data.Event
import com.example.sports.databinding.ItemEventBinding
import com.example.sports.databinding.ItemSportsEventBinding
import com.example.sports.viewmodel.MainViewModel
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

class EventAdapter(private val events: List<Event>, private val context: Context, private val viewModel: MainViewModel) : RecyclerView.Adapter<EventAdapter.EventViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val binding = ItemEventBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return EventViewHolder(binding)
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        val event = events[position]

        holder.bind(event)

        initCountdown(holder, event)

        holder.iv_favorite.setOnClickListener{
            viewModel.toggleFavoriteEvent(event)
            notifyItemChanged(position)
        }
    }

    private fun initCountdown(holder: EventViewHolder, event: Event) {
        // Cancel any existing timer
        holder.countDownTimer?.cancel()

        // Start a new countdown timer
        val millisInFuture: Long = event.timestamp * 100L
        holder.countDownTimer = object : CountDownTimer(millisInFuture, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                holder.countdownTextView.setText(getDate(millisUntilFinished))
            }

            override fun onFinish() {
                holder.countdownTextView.setText("00:00:00")
            }
        }.start()
    }

    override fun getItemCount(): Int {
        return events.size
    }

    class EventViewHolder(private val binding: ItemEventBinding) : RecyclerView.ViewHolder(binding.root) {
        var countdownTextView: TextView = binding.tvCountdown
        val iv_favorite: ImageView = binding.ivFavorite

        var countDownTimer: CountDownTimer? = null

        fun bind(event: Event) {
            val competitors = event.shortDescription.split(" - ")
            binding.tvCompetitor1.text = competitors[0]
            binding.tvCompetitor2.text = competitors[1]

            binding.ivFavorite .setImageResource(R.drawable.star)
            if(event.isFavorite)
                binding.ivFavorite .setImageResource(R.drawable.star_border)
        }

        fun updateCountdownText(millisUntilFinished: Long) {
            val seconds = (millisUntilFinished / 1000) %60
            val minutes = (millisUntilFinished / (1000 * 60)) % 60
            val hours = (millisUntilFinished / (1000 * 60 * 60)) % 24

            val formattedTime = String.format("%02d:%02d:%02d", hours, minutes, seconds)
            binding.tvCountdown.text = formattedTime
        }
    }

    private fun getDate(milliSeconds: Long): String {
        //create a DateFormatter object for displaying date in specified format
        val formatter: DateFormat = SimpleDateFormat("HH:mm:ss", Locale.US)
        formatter.setTimeZone(TimeZone.getTimeZone("UTC"))
        val time: String = formatter.format(Date(milliSeconds))

        return time
    }

    override fun onViewRecycled(holder: EventViewHolder) {
        super.onViewRecycled(holder)

        holder.countDownTimer?.cancel()
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        super.onDetachedFromRecyclerView(recyclerView)

        // Iterate through allactive ViewHolders and cancel their timers
        for (i in 0 until itemCount) {
            val viewHolder = recyclerView.findViewHolderForAdapterPosition(i) as? EventViewHolder
            viewHolder?.countDownTimer?.cancel()
        }
    }
}
