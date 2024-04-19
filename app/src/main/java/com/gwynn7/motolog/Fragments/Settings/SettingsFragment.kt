package com.gwynn7.motolog.Fragments.Settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.gwynn7.motolog.LocaleHelper
import com.gwynn7.motolog.R


class SettingsFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.settings, container, false)

        val en_button = view.findViewById<Button>(R.id.en)
        val it_button = view.findViewById<Button>(R.id.it)

        en_button.setOnClickListener {
            LocaleHelper.setLocale(requireContext(), "en")
            refresh_activity()
        }
        it_button.setOnClickListener {
            LocaleHelper.setLocale(requireContext(), "it")
            refresh_activity()
        }


        return view
    }

    fun refresh_activity()
    {
        val intent = activity?.intent
        activity?.finish()
        startActivity(intent!!)
    }
}