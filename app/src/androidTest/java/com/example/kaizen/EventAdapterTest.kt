package com.example.kaizen

import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.test.platform.app.InstrumentationRegistry
import com.example.kaizen.ui.adapters.EventAdapter
import org.junit.Assert.assertEquals
import org.junit.Test

class EventAdapterTest {

    @Test
    fun updateCountdownText_formatsTimeCorrectly() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        val inflater = LayoutInflater.from(context)
        val parent = FrameLayout(context)
        val view = inflater.inflate(R.layout.item_event, parent, false)

        val viewHolder = EventAdapter.EventViewHolder(view)

        viewHolder.updateCountdownText(3600000) // 1 hour

        assertEquals("01:00:00", viewHolder.countdownTextView.text)
    }
}