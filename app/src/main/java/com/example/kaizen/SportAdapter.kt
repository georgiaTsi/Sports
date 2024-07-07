package com.example.kaizen

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Switch
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.kaizen.model.Sport

interface FavoriteSportListener {
    fun addFavoriteSport(sportName: String)
    fun removeFavoriteSport(sportName: String)
}

class SportAdapter(private val sports: List<Sport>, private val listener: FavoriteSportListener) : RecyclerView.Adapter<SportAdapter.SportViewHolder>() {

    private var filteredList: List<Sport> = sports.toList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SportViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_sports_event, parent, false)
        return SportViewHolder(view)
    }

    override fun onBindViewHolder(holder: SportViewHolder, position: Int) {
        holder.bind(filteredList[position])

        holder.iv_expand.setOnClickListener {
            val isExpanded = holder.rv_events.visibility == View.VISIBLE
            holder.rv_events.visibility = if (isExpanded) View.GONE else View.VISIBLE

            holder.linear_collapse.visibility = if (!isExpanded) View.GONE else View.VISIBLE

            holder.iv_expand.setRotation(holder.iv_expand.rotation+180);
        }

        holder.toggle_star.setOnClickListener {
            filteredList[position].isStarred = !filteredList[position].isStarred

            if (filteredList[position].isStarred)
                listener.addFavoriteSport(filteredList[position].name)
            else
                listener.removeFavoriteSport(filteredList[position].name)
        }
    }

    override fun getItemCount(): Int {
        return filteredList.size
    }

    fun filterStarred(isStarred: Boolean) {
        filteredList = if (isStarred) {
            sports.filter { it.isStarred }
        } else {
            sports
        }

        notifyDataSetChanged()
    }

    class SportViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imageview_ball: ImageView = itemView.findViewById(R.id.imageview_ball)
        private val tv_sport: TextView = itemView.findViewById(R.id.tv_sport)
        val rv_events: RecyclerView = itemView.findViewById(R.id.rv_events)
        val iv_expand: ImageView = itemView.findViewById(R.id.iv_expand)
        val toggle_star: Switch = itemView.findViewById(R.id.switch_star)
        val linear_collapse: View = itemView.findViewById(R.id.linear_collapse)

        fun bind(sport: Sport) {
            tv_sport.text = sport.name

            if(sport.name == "SOCCER")
                imageview_ball.setImageResource(R.drawable.soccer)
            else if(sport.name == "BASKETBALL")
                imageview_ball.setImageResource(R.drawable.basketball)

            val layoutManager = CustomGridLayoutManager(itemView.context)
            rv_events.layoutManager = layoutManager

            val eventAdapter = EventAdapter(sport.events, this.itemView.context)
            rv_events.adapter = eventAdapter

            toggle_star.isChecked = false
            if(sport.isStarred)
                toggle_star.isChecked = true

            rv_events.visibility = View.VISIBLE
            linear_collapse.visibility = View.GONE

            iv_expand.setRotation(0F)
        }
    }
}
