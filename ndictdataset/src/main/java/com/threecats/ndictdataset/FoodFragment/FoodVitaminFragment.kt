package com.threecats.ndictdataset.FoodFragment


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.threecats.ndictdataset.R


/**
 * A simple [Fragment] subclass.
 */
class FoodVitaminFragment : FoodPropertyFragment() {

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater!!.inflate(R.layout.fragment_food_vitamin, container, false)
    }

    override fun getFoodFields(): Int {
        val changeNumber = checkTextHelper.ChangeNumber()
        if (changeNumber > 0) {

        }
        return changeNumber
    }

}// Required empty public constructor
