package com.threecats.ndictdataset.FoodFragment


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.threecats.ndictdataset.BDM
import com.threecats.ndictdataset.Helper.CheckTextHelper

import com.threecats.ndictdataset.R
import kotlinx.android.synthetic.main.fragment_food_note.*


/**
 * A simple [Fragment] subclass.
 */
class FoodNoteFragment: FoodPropertyFragment() {

    val currentFood = BDM.ShareSet!!.CurrentFood!!
    val checkTextHelper = CheckTextHelper()

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater!!.inflate(R.layout.fragment_food_note, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        checkTextHelper.addEditBox(FoodNoteIEditText, currentFood.note)
    }
}// Required empty public constructor
