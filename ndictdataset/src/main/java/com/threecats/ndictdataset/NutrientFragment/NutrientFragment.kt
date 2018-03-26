package com.threecats.ndictdataset.NutrientFragment


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.threecats.ndictdataset.R
import com.threecats.ndictdataset.Shells.TabViewShell.ViewPagerFragment


/**
 * A simple [Fragment] subclass.
 */
class NutrientFragment : ViewPagerFragment() {


    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater!!.inflate(R.layout.fragment_nutrient, container, false)
    }

}// Required empty public constructor
