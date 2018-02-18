package com.threecats.ndictdataset.View

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.threecats.ndictdataset.BDM
import com.threecats.ndictdataset.R
import kotlinx.android.synthetic.main.activity_food_editer.*

class FoodEditerActivity : AppCompatActivity() {

    val currentFood = BDM.ShareSet?.CurrentFood

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_food_editer)
    }

    override fun onStart() {
        super.onStart()
        //NameIEditText.text = currentFood?.name
        NameIEditText.text.append(currentFood?.name)
    }
}
