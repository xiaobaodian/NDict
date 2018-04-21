package com.threecats.ndictdataset.FoodFragments


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.threecats.ndictdataset.Bmob.BFood
import com.threecats.ndictdataset.Enum.EChangeBlock

import com.threecats.ndictdataset.R
import com.threecats.ndictdataset.View.FoodEditerActivity
import kotlinx.android.synthetic.main.fragment_food_nutrient.*


/**
 * A simple [Fragment] subclass.
 */
class FoodNutrientFragment : FoodPropertyFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.fragment_food_nutrient, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //getFields(currentFood)
    }

    override fun onResume() {
        super.onResume()
        if (initFieldsFlag) {
            initFieldsFlag = false
            shareSet.currentFood?.let { importFields(it) }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()

        //setFields(shareSet.CurrentFood!!)
    }

    override fun blockChangeState(parent: FoodEditerActivity) {
        val changeNumber = foodEditTextHelper.changeNumber()
        if (changeNumber > 0) {
            parent.addChangeBlock(EChangeBlock.Food)
        }
    }

    override fun importFields(food: BFood) {
        getFields(food)
    }

    override fun exportFields(food: BFood) {
        setFields(food)
    }

    override fun firstEditTextFocus(){
        with (CaloriesIEditText){
            isFocusable = true
            isFocusableInTouchMode = true
            requestFocus()
        }
    }

    private fun getFields(food: BFood){

        with (foodEditTextHelper) {
            textBoxs.clear()
            addEditBox(CaloriesIEditText, food.calories.toString())
            addEditBox(WaterIEditText, food.water.toString())
            addEditBox(ProteinIEditText, food.protein.toString())
            addEditBox(CholesterolIEditText, food.cholesterol.toString())
            addEditBox(FiberIEditText, food.foodFiber.toString())
            addEditBox(CarbohydrateIEditText, food.carbohydrate.toString())
            addEditBox(FatIEditText, food.fat.toString())
            initHash()
        }

    }

    private fun setFields(food: BFood){
        foodEditTextHelper.checkNull("0.0")
        foodEditTextHelper.textBoxs.forEach {
            when (it.editBox){
                CaloriesIEditText       -> food.calories = it.editBox.text.toString().toFloat()
                WaterIEditText          -> food.water = it.editBox.text.toString().toFloat()
                ProteinIEditText        -> food.protein = it.editBox.text.toString().toFloat()
                CholesterolIEditText    -> food.cholesterol = it.editBox.text.toString().toFloat()
                FiberIEditText          -> food.foodFiber = it.editBox.text.toString().toFloat()
                CarbohydrateIEditText   -> food.carbohydrate = it.editBox.text.toString().toFloat()
                FatIEditText            -> food.fat = it.editBox.text.toString().toFloat()
            }
        }
    }

}// Required empty public constructor