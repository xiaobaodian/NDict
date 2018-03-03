package com.threecats.ndictdataset.FoodFragment


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.threecats.ndictdataset.Bmob.BFood
import com.threecats.ndictdataset.Enum.ChangeBlock

import com.threecats.ndictdataset.R
import com.threecats.ndictdataset.View.FoodEditerActivity
import kotlinx.android.synthetic.main.fragment_food_note.*


/**
 * A simple [Fragment] subclass.
 */
class FoodNoteFragment: FoodPropertyFragment() {

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater!!.inflate(R.layout.fragment_food_note, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        checkTextHelper.addEditBox(FoodNoteIEditText, currentFood.note)
        checkTextHelper.initHash()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        assemblyFields(currentFood)
    }

    override fun BlockChangeState(parent: FoodEditerActivity) {
        val changeNumber = checkTextHelper.ChangeNumber()
        if (changeNumber > 0) {
            parent.addChangeBlock(ChangeBlock.Food)
        }
    }

    private fun assemblyFields(food: BFood){
        checkTextHelper.CheckNull()
        checkTextHelper.textBoxs.forEach {
            when (it.editBox){
                FoodNoteIEditText -> food.note = it.editBox.text.toString()
            }
        }
    }
}// Required empty public constructor
