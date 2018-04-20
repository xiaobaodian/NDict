package com.threecats.ndictdataset.NutrientFragments


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.threecats.ndictdataset.R


/**
 * A simple [Fragment] subclass.
 */
class NutrientDosisFragment : Fragment() {


    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater!!.inflate(R.layout.fragment_nutrient_dosis, container, false)
    }

}// Required empty public constructor
