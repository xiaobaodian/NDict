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
import com.threecats.ndictdataset.R

class CategoryFoodsAdapter(private val categorys: MutableList<BFoodCategory>, val parentContext: Context) : RecyclerView.Adapter<CategoryFoodsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.category_recycleritem, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val category = categorys[position]
        holder.category = category
        holder.title.text = category.LongTitle
        holder.subTotal.text = category.FoodTotal.toString()

        holder.view.setOnClickListener {
            //mListener?.onListFragmentInteraction(holder.food)
            //Toast.makeText(holder.context,"点击了：${category.LongTitle}", Toast.LENGTH_SHORT).show()
            BDM.ShareSet?.CurrentCategory = category
            BDM.ShareSet?.CurrentCategoryPosition = position
            val intent = Intent(parentContext, FoodListActivity::class.java)
            parentContext.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return categorys.size
    }

    inner class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val context: Context = view.context
        val title: TextView
        val subTotal: TextView
        var category: BFoodCategory? = null

        init {
            title = view.findViewById<View>(R.id.categoryTitle) as TextView
            subTotal = view.findViewById<View>(R.id.subTotal) as TextView
        }
    }
}