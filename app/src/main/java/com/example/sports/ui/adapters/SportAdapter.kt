package com.example.sports.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Switch
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.sports.ui.utils.CustomGridLayoutManager
import com.example.sports.R
import com.example.sports.data.Sport
import com.example.sports.databinding.ItemSportsEventBinding
import com.example.sports.viewmodel.MainViewModel

class SportAdapter(private val sports: List<Sport>, private val viewModel: MainViewModel) : RecyclerView.Adapter<SportAdapter.SportViewHolder>() {

    private var filteredList: List<Sport> = sports.toList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SportViewHolder {
        val binding = ItemSportsEventBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SportViewHolder(binding, viewModel)
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

        holder.findIcon(sport.name)
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

    class SportViewHolder(private val binding: ItemSportsEventBinding, private val viewModel: MainViewModel) : RecyclerView.ViewHolder(binding.root) {
        val iv_expand: ImageView = binding.ivExpand
        val toggle_star: Switch = binding.switchStar

        fun bind(sport: Sport) {
            binding.tvSport.text = sport.name

            val layoutManager = CustomGridLayoutManager(itemView.context)
            binding.rvEvents.layoutManager = layoutManager

            val eventAdapter = EventAdapter(sport.events, this.itemView.context, viewModel)
            binding.rvEvents.adapter = eventAdapter

            binding.switchStar.isChecked = false
            if (sport.isStarred)
                binding.switchStar.isChecked = true

            toggleSport(sport.isExpanded)
        }

        fun toggleSport(isExpanded: Boolean) {
            if (isExpanded) {
                binding.rvEvents.visibility = View.VISIBLE
                binding.linearCollapse.visibility = View.GONE
                binding.ivExpand.setRotation(0F)
            } else {
                binding.rvEvents.visibility = View.GONE
                binding.linearCollapse.visibility = View.VISIBLE
                binding.ivExpand.setRotation(180F)
            }
        }

        fun findIcon(sportName: String) {
            val drawableResource = when (sportName) {
                "SOCCER" -> R.drawable.ic_soccer
                "BASKETBALL" -> R.drawable.ic_basketball
                "TENNIS", "TABLE TENNIS" -> R.drawable.ic_tennis
                "BADMINTON" -> R.drawable.ic_badminton
                "VOLLEYBALL", "BEACH VOLLEYBALL" -> R.drawable.ic_volleyball
                else -> R.drawable.circle
            }

            binding.imageviewBall.setImageResource(drawableResource)
        }
    }
}
