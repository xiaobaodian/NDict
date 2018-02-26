package com.threecats.ndictdataset.FoodFragment


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.threecats.ndictdataset.BDM
import com.threecats.ndictdataset.Helper.CheckTextHelper

import com.threecats.ndictdataset.R
import kotlinx.android.synthetic.main.fragment_food_nutrient.*


/**
 * A simple [Fragment] subclass.
 */
class FoodNutrientFragment : FoodPropertyFragment() {

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater!!.inflate(R.layout.fragment_food_nutrient, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        checkTextHelper.addEditBox(CaloriesIEditText, currentFood.calories.toString())
        checkTextHelper.addEditBox(WaterIEditText, currentFood.water.toString())
        checkTextHelper.addEditBox(ProteinIEditText, currentFood.protein.toString())
        checkTextHelper.addEditBox(CholesterolIEditText, currentFood.cholesterol.toString())
        checkTextHelper.addEditBox(FiberIEditText, currentFood.foodFiber.toString())
        checkTextHelper.addEditBox(CarbohydrateIEditText, currentFood.carbohydrate.toString())
        checkTextHelper.addEditBox(FatIEditText, currentFood.fat.toString())
        checkTextHelper.initHash()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        getFoodFields()
    }

    override fun getFoodFields(): Int {
        val changeNumber = checkTextHelper.ChangeNumber()
        if (changeNumber > 0) {
            checkTextHelper.textBoxs.forEach {
                when (it.editBox){
                    CaloriesIEditText -> currentFood.calories = it.editBox.text.toString().toFloat()
                    WaterIEditText -> currentFood.water = it.editBox.text.toString().toFloat()
                    ProteinIEditText -> currentFood.protein = it.editBox.text.toString().toFloat()
                    CholesterolIEditText -> currentFood.cholesterol = it.editBox.text.toString().toFloat()
                    FiberIEditText -> currentFood.foodFiber = it.editBox.text.toString().toFloat()
                    CarbohydrateIEditText -> currentFood.carbohydrate = it.editBox.text.toString().toFloat()
                    FatIEditText -> currentFood.fat = it.editBox.text.toString().toFloat()
                }
            }
        }
        return changeNumber
    }

}// Required empty public constructor
