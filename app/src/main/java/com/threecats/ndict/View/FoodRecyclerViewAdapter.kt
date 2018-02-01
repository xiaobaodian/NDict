package com.threecats.ndict.View

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.threecats.ndict.Models.Food
import com.threecats.ndict.R

//import com.threecats.ndict.View.CategoryFoodsFragment.OnListFragmentInteractionListener
import com.threecats.ndict.dummy.DummyContent.DummyItem

/**
 * specified [OnListFragmentInteractionListener].
 * TODO: Replace the implementation with code for your data type.
 */
class FoodRecyclerViewAdapter(private val foods: List<Food>) : RecyclerView.Adapter<FoodRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.fragment_food_listitem, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.food = foods[position]
        holder.nameView.text = foods[position].name
        holder.contentView.text = foods[position].alias

        holder.view.setOnClickListener {
            //mListener?.onListFragmentInteraction(holder.food)
        }
    }

    override fun getItemCount(): Int {
        return foods.size
    }

    inner class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val nameView: TextView
        val contentView: TextView
        var food: Food? = null

        init {
            nameView = view.findViewById<View>(R.id.name) as TextView
            contentView = view.findViewById<View>(R.id.content) as TextView
        }

        override fun toString(): String {
            return super.toString() + " '" + contentView.text + "'"
        }
    }
}
