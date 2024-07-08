package com.example.kaizen.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Switch
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.kaizen.ui.utils.CustomGridLayoutManager
import com.example.kaizen.R
import com.example.kaizen.data.Sport
import com.example.kaizen.viewmodel.MainViewModel

class SportAdapter(private val sports: List<Sport>, private val viewModel: MainViewModel) : RecyclerView.Adapter<SportAdapter.SportViewHolder>() {

    private var filteredList: List<Sport> = sports.toList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SportViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_sports_event, parent, false)
        return SportViewHolder(view, viewModel)
    }

    override fun onBindViewHolder(holder: SportViewHolder, position: Int) {
        val sport = filteredList[position]
        holder.bind(sport)

        holder.iv_expand.setOnClickListener {
            viewModel.toggleSportExpansion(sport)
            notifyItemChanged(position)
        }

        holder.toggle_star.setOnClickListener {
            viewModel.toggleFavoriteSport(sport)
            notifyItemChanged(position)
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

    class SportViewHolder(itemView: View, private val viewModel: MainViewModel) : RecyclerView.ViewHolder(itemView) {
        private val tv_sport: TextView = itemView.findViewById(R.id.tv_sport)
        val rv_events: RecyclerView = itemView.findViewById(R.id.rv_events)
        val iv_expand: ImageView = itemView.findViewById(R.id.iv_expand)
        val toggle_star: Switch = itemView.findViewById(R.id.switch_star)
        val linear_collapse: View = itemView.findViewById(R.id.linear_collapse)

        fun bind(sport: Sport) {
            tv_sport.text = sport.name

            val layoutManager = CustomGridLayoutManager(itemView.context)
            rv_events.layoutManager = layoutManager

            val eventAdapter = EventAdapter(sport.events, this.itemView.context, viewModel)
            rv_events.adapter = eventAdapter

            toggle_star.isChecked = false
            if (sport.isStarred)
                toggle_star.isChecked = true

            toggleSport(sport.isExpanded)
        }

        fun toggleSport(isExpanded: Boolean) {
            if (isExpanded) {
                rv_events.visibility = View.VISIBLE
                linear_collapse.visibility = View.GONE
                iv_expand.setRotation(0F)
            } else {
                rv_events.visibility = View.GONE
                linear_collapse.visibility = View.VISIBLE
                iv_expand.setRotation(180F)
            }
        }
    }
}
