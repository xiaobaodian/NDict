package com.threecats.ndict.View

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.threecats.ndict.Models.Category
import com.threecats.ndict.Models.Food
import com.threecats.ndict.R
import com.threecats.ndict.Shells.RecyclerViewShell.*
import kotlinx.android.synthetic.main.content_recycler_view.*
import org.jetbrains.anko.toast

/**
 * A fragment representing a list of Items.
 *
 *
 * Activities containing this fragment MUST implement the [OnListFragmentInteractionListener]
 * interface.
 */
@SuppressLint("ValidFragment")
/**
 * Mandatory empty constructor for the fragment manager to instantiate the
 * fragment (e.g. upon screen orientation changes).
 */

class CategoryFoodsFragment(val category: Category) : Fragment() {

    private var foodRecyclerView: RecyclerViewShell<Any, Food>? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_food_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (foodRecyclerView == null) {
            foodRecyclerView = RecyclerViewShell(requireNotNull(this.context))
        }

        foodRecyclerView?.let {
            it.progressBar(GProgressBar).recyclerView(GRecyclerView).addViewType("item",ItemType.Item,R.layout.recycleritem_food)
            it.setDisplayItemListener(object : DisplayItemListener<Any, Food> {
                override fun onDisplayItem(item: Food, holder: RecyclerViewAdapter<Any, Food>.ItemViewHolder) {
                    holder.displayText(R.id.ItemName,item.name)
                    holder.displayText(R.id.ItemAlias,item.alias)
                }
            })
            it.setOnClickItemListener(object : ClickItemListener<Any, Food> {
                override fun onClickItem(item: Food, holder: RecyclerViewAdapter<Any, Food>.ItemViewHolder) {
                    it.context.toast("点击了：${item.name}")
                }
            })
            it.setQueryDataListener(object : QueryDatasListener<Any, Food>{
                override fun onQueryDatas(shell: RecyclerViewShell<Any, Food>) {
                    category.foods.forEach { shell.addItem(it) }
                    shell.completeQuery()
                }
            })
            it.link()
        }

    }
}
