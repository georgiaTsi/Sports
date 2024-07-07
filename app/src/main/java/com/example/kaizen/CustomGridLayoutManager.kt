package com.example.kaizen

import android.content.Context
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

class CustomGridLayoutManager(context: Context) : GridLayoutManager(context, 4) {

    override fun onLayoutChildren(recycler: RecyclerView.Recycler?, state: RecyclerView.State?) {
        try {
            super.onLayoutChildren(recycler, state)
        } catch (e: IndexOutOfBoundsException) {
            // Handle exceptions gracefully (e.g., log the error)
        }
    }

    override fun supportsPredictiveItemAnimations(): Boolean {
        return false // Disable predictive animations to avoid potential issues
    }
}