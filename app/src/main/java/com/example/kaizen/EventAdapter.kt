package com.example.kaizen

import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.kaizen.model.Event
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

class EventAdapter(private val events: List<Event>) : RecyclerView.Adapter<EventAdapter.EventViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_event, parent, false)
        return EventViewHolder(view)
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        val event = events[position]

        holder.bind(event)

        initCountdown(holder, event)

        holder.iv_favorite.setOnClickListener{
            if (event.isFavorite) {
                holder.iv_favorite.setImageResource(R.drawable.star)
            } else {
                holder.iv_favorite.setImageResource(R.drawable.star_border)
            }

            event.isFavorite = !event.isFavorite
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

    class EventViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val countdownTextView: TextView = itemView.findViewById(R.id.tv_countdown)
        private val tvCompetitor1: TextView = itemView.findViewById(R.id.tv_competitor_1)
        private val tvCompetitor2: TextView = itemView.findViewById(R.id.tv_competitor_2)
        val iv_favorite: ImageView = itemView.findViewById(R.id.iv_favorite)

        var countDownTimer: CountDownTimer? = null

        fun bind(event: Event) {
            val competitors = event.shortDescription.split(" - ")
            tvCompetitor1.text = competitors[0]
            tvCompetitor2.text = competitors[1]

            iv_favorite.setImageResource(R.drawable.star)
            if(event.isFavorite)
                iv_favorite.setImageResource(R.drawable.star_border)
        }

        fun updateCountdownText(millisUntilFinished: Long) {
            val seconds = (millisUntilFinished / 1000) %60
            val minutes = (millisUntilFinished / (1000 * 60)) % 60
            val hours = (millisUntilFinished / (1000 * 60 * 60)) % 24

            val formattedTime = String.format("%02d:%02d:%02d", hours, minutes, seconds)
            countdownTextView.text = formattedTime
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
