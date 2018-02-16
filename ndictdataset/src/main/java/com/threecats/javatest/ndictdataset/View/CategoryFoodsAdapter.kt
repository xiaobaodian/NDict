package com.threecats.javatest.ndictdataset.View

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.threecats.javatest.ndictdataset.Bmob.FoodCategory
import com.threecats.javatest.ndictdataset.R

class CategoryFoodsAdapter(private val categorys: MutableList<FoodCategory>) : RecyclerView.Adapter<CategoryFoodsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.category_recycleitem, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.category = categorys[position]
        holder.title.text = categorys[position].LongTitle
        holder.subFoodCount.text = "99"

        holder.view.setOnClickListener {
            //mListener?.onListFragmentInteraction(holder.food)
        }
    }

    override fun getItemCount(): Int {
        return categorys.size
    }

    inner class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val title: TextView
        val subFoodCount: TextView
        var category: FoodCategory? = null

        init {
            title = view.findViewById<View>(R.id.categoryTitle) as TextView
            subFoodCount = view.findViewById<View>(R.id.subFoods) as TextView
        }
    }
}