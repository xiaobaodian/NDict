package com.threecats.ndictdataset.View

import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.threecats.ndictdataset.BDM
import com.threecats.ndictdataset.Bmob.BFoodCategory
import com.threecats.ndictdataset.Bmob.BNutrient
import com.threecats.ndictdataset.Enum.EEditerState
import com.threecats.ndictdataset.R

class NutrientsAdapter(private val nutrients: MutableList<BNutrient>, val parentContext: Context) : RecyclerView.Adapter<NutrientsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.nutrient_recycleritem, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val nutrient = nutrients[position]
        holder.nutrient = nutrient
        holder.title.text = nutrient.name
        //holder.subTotal.text = nutrient.foodTotal.toString()

        holder.view.setOnClickListener {
            //mListener?.onListFragmentInteraction(holder.food)
            //Toast.makeText(holder.context,"点击了：${nutrient.longTitle}", Toast.LENGTH_SHORT).show()
            BDM.ShareSet?.CurrentNutrient = nutrient
            //val intent = Intent(parentContext, FoodListActivity::class.java)
            //parentContext.startActivity(intent)
        }

        holder.view.setOnLongClickListener {
            BDM.ShareSet?.CurrentNutrient = nutrient
            //BDM.ShareSet?.ItemEditState = EEditerState.CategoryEdit
            //val intent = Intent(parentContext, CategoryEditerActivity::class.java)
            //parentContext.startActivity(intent)
            true
        }
    }

    override fun getItemCount(): Int {
        return nutrients.size
    }

    inner class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val context: Context = view.context
        val title: TextView
        //val subTotal: TextView
        var nutrient: BNutrient? = null

        init {
            title = view.findViewById<View>(R.id.nutrientTitle) as TextView
            //subTotal = view.findViewById<View>(R.id.subTotal) as TextView
        }
    }
}