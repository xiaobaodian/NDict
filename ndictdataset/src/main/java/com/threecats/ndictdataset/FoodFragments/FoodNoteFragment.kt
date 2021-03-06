package com.threecats.ndictdataset.FoodFragments


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.threecats.ndictdataset.Bmob.BFood

import com.threecats.ndictdataset.R
import com.threecats.ndictdataset.View.FoodEditerActivity
import kotlinx.android.synthetic.main.fragment_food_note.*

/**
 * A simple [Fragment] subclass.
 */

class FoodNoteFragment: FoodPropertyFragment() {

    var isLoadUrl = true

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_food_note, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val article = shareSet.currentFood?.article
        if (isLoadUrl) {
            article?.let {
                NoteWebView.loadUrl(it.url)
                isLoadUrl = false
            }
        }
    }

    override fun itemToViewFields(food: BFood) {

    }

    override fun viewFieldsToItem(food: BFood) {

    }

    override fun firstEditTextFocus(){

    }

}// Required empty public constructor
