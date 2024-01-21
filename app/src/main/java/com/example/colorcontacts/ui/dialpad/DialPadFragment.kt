package com.example.colorcontacts.ui.dialpad

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager2.widget.ViewPager2
import com.example.colorcontacts.R

class DialPadFragment : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_dial_pad, container, false)
    }

    override fun onResume() {
        super.onResume()
        val viewPager = requireActivity().findViewById<ViewPager2>(R.id.viewPager)
        viewPager.currentItem = 1
    }

}