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
import com.threecats.ndictdataset.Enum.EditerState
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
        holder.name.text = food.name
        holder.alias.text = "alias"

        holder.view.setOnClickListener {
            //mListener?.onListFragmentInteraction(holder.food)
            //Toast.makeText(holder.context,"点击了：${food.name}", Toast.LENGTH_SHORT).show()
//            BDM.ShareSet?.CurrentFood = food
//            BDM.ShareSet?.CurrentFoodPosition = position
//            BDM.ShareSet?.ItemEditState = EditerState.FoodEdit
            BDM.ShareSet?.editFoodItem(food)
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
        val alias: TextView
        var food: BFood? = null

        init {
            name = view.findViewById<View>(R.id.ItemName) as TextView
            alias = view.findViewById<View>(R.id.ItemAlias) as TextView
        }
    }
}