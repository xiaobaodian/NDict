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
import kotlinx.android.synthetic.main.fragment_food_name.*
import kotlinx.android.synthetic.main.fragment_food_note.*
import org.jetbrains.anko.toast


/**
 * A simple [Fragment] subclass.
 */
class FoodNoteFragment: FoodPropertyFragment() {

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        //initShareVar()
        return inflater!!.inflate(R.layout.fragment_food_note, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //getFields(currentFood)
    }

    override fun onResume() {
        super.onResume()
        if (initFieldsFlag) {
            initFieldsFlag = false
            ImportFields(shareSet.CurrentFood!!)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()

        //setFields(shareSet.CurrentFood!!)
    }

    override fun BlockChangeState(parent: FoodEditerActivity) {
        val changeNumber = foodEditTextHelper.ChangeNumber()
        if (changeNumber > 0) {
            parent.addChangeBlock(ChangeBlock.Food)
        }
    }

    override fun ImportFields(food: BFood) {
        getFields(food)
    }

    override fun ExportFields(food: BFood) {
        setFields(food)
    }

    override fun FirstEditTextFocus(){
        with (FoodNoteIEditText){
            isFocusable = true
            isFocusableInTouchMode = true
            requestFocus()
        }
    }

    private fun getFields(food: BFood){

        with (foodEditTextHelper) {
            textBoxs.clear()
            addEditBox(FoodNoteIEditText, food.note)
            initHash()
        }

    }

    private fun setFields(food: BFood){
        foodEditTextHelper.CheckNull("")
        foodEditTextHelper.textBoxs.forEach {
            when (it.editBox){
                FoodNoteIEditText -> food.note = it.editBox.text.toString()
            }
        }
    }
}// Required empty public constructor
