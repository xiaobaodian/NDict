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
        assemblyFields(currentFood)
    }

    override fun BlockChangeState(parent: FoodEditerActivity) {
        val changeNumber = checkTextHelper.ChangeNumber()
        if (changeNumber > 0) {
            parent.addChangeBlock(ChangeBlock.Food)
        }
    }

    private fun assemblyFields(food: BFood){
        checkTextHelper.textBoxs.forEach {
            when (it.editBox){
                CaloriesIEditText -> food.calories = it.editBox.text.toString().toFloat()
                WaterIEditText -> food.water = it.editBox.text.toString().toFloat()
                ProteinIEditText -> food.protein = it.editBox.text.toString().toFloat()
                CholesterolIEditText -> food.cholesterol = it.editBox.text.toString().toFloat()
                FiberIEditText -> food.foodFiber = it.editBox.text.toString().toFloat()
                CarbohydrateIEditText -> food.carbohydrate = it.editBox.text.toString().toFloat()
                FatIEditText -> food.fat = it.editBox.text.toString().toFloat()
            }
        }
    }

}// Required empty public constructor
