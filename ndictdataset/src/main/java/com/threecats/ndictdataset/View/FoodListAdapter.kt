package com.threecats.ndictdataset.View

import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.threecats.ndictdataset.BDM
import com.threecats.ndictdataset.Bmob.BFood
import com.threecats.ndictdataset.R

class FoodListAdapter(private val foods: MutableList<BFood>, val context: Context) : RecyclerView.Adapter<FoodListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.food_recycleritem, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val food = foods[position]
        holder.food = food
        holder.name.text = if (food.alias.length == 0) food.name else "${food.name}、${food.alias}"
        holder.updateTime.text = food.updatedAt

        holder.view.setOnClickListener {
            //mListener?.onListFragmentInteraction(holder.food)

            BDM.ShareSet?.editFood(food)
            val intent = Intent(context, FoodEditerActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return foods.size
    }

    inner class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val context: Context = view.context
        val name: TextView
        val updateTime: TextView
        var food: BFood? = null

        init {
            name = view.findViewById<View>(R.id.ItemName) as TextView
            updateTime = view.findViewById<View>(R.id.ItemAlias) as TextView
        }
    }
}