package com.example.sports

import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.test.platform.app.InstrumentationRegistry
import com.example.sports.databinding.ItemEventBinding
import com.example.sports.ui.adapters.EventAdapter
import org.junit.Assert.assertEquals
import org.junit.Test

class EventAdapterTest {

    @Test
    fun updateCountdownText_formatsTimeCorrectly() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        val parent = FrameLayout(context)

        val binding = ItemEventBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        val viewHolder = EventAdapter.EventViewHolder(binding)

        viewHolder.updateCountdownText(3600000) // 1 hour

        assertEquals("01:00:00", viewHolder.countdownTextView.text)
    }
}