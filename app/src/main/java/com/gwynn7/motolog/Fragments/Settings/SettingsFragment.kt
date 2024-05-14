package com.gwynn7.motolog.Fragments.Settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.fragment.app.Fragment
import com.gwynn7.motolog.LocaleHelper
import com.gwynn7.motolog.R
import com.gwynn7.motolog.UnitHelper
import com.gwynn7.motolog.showToast

class SettingsFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.settings, container, false)

        setRadioButtons(view)

        val language = view.findViewById<RadioGroup>(R.id.rg_language)
        language.setOnCheckedChangeListener { _, checkedId ->
            when(checkedId){
                R.id.rb_english -> LocaleHelper.setLocale(requireContext(), LocaleHelper.Language.ENG)
                R.id.rb_italian -> LocaleHelper.setLocale(requireContext(), LocaleHelper.Language.ITA)
            }
            restartActivity()
        }

        val distance = view.findViewById<RadioGroup>(R.id.rg_distance)
        distance.setOnCheckedChangeListener { _, checkedId ->
            when(checkedId){
                R.id.rb_km -> UnitHelper.saveDistance(requireContext(), UnitHelper.Distance.KM)
                R.id.rb_miles -> UnitHelper.saveDistance(requireContext(), UnitHelper.Distance.MILES)
            }
        }

        val currency = view.findViewById<RadioGroup>(R.id.rg_currency)
        currency.setOnCheckedChangeListener { _, checkedId ->
            when(checkedId){
                R.id.rb_eur -> UnitHelper.saveCurrency(requireContext(), UnitHelper.Currency.EUR)
                R.id.rb_gbp -> UnitHelper.saveCurrency(requireContext(), UnitHelper.Currency.GBP)
                R.id.rb_usd -> UnitHelper.saveCurrency(requireContext(), UnitHelper.Currency.USD)
                R.id.rb_jpy -> UnitHelper.saveCurrency(requireContext(), UnitHelper.Currency.JPY)
            }
        }

        setHasOptionsMenu(true)
        return view
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.export_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId)
        {
            R.id.menu_export -> showToast(requireContext(), getString(R.string.todo))
            R.id.menu_import -> showToast(requireContext(), getString(R.string.todo))
        }
        return super.onContextItemSelected(item)
    }

    private fun setRadioButtons(view: View)
    {
        when(LocaleHelper.getLanguage(requireContext()))
        {
            LocaleHelper.Language.ENG -> view.findViewById<RadioButton>(R.id.rb_english).isChecked = true
            LocaleHelper.Language.ITA -> view.findViewById<RadioButton>(R.id.rb_italian).isChecked = true
        }

        when(UnitHelper.distance)
        {
            UnitHelper.Distance.KM -> view.findViewById<RadioButton>(R.id.rb_km).isChecked = true
            UnitHelper.Distance.MILES -> view.findViewById<RadioButton>(R.id.rb_miles).isChecked = true
        }

        when(UnitHelper.currency)
        {
            UnitHelper.Currency.EUR -> view.findViewById<RadioButton>(R.id.rb_eur).isChecked = true
            UnitHelper.Currency.GBP -> view.findViewById<RadioButton>(R.id.rb_gbp).isChecked = true
            UnitHelper.Currency.USD -> view.findViewById<RadioButton>(R.id.rb_usd).isChecked = true
            UnitHelper.Currency.JPY -> view.findViewById<RadioButton>(R.id.rb_jpy).isChecked = true
        }
    }

    private fun restartActivity()
    {
        val intent = activity?.intent
        activity?.finish()
        startActivity(intent!!)
    }
}