/*
 * Copyright (c) by Andreas (oentoro.andreas@gmail.com)
 * created at 25 - 7 - 2020.
 */

package starbright.com.projectegg.view

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.items.AbstractItem
import com.mikepenz.fastadapter.listeners.ClickEventHook
import kotlinx.android.synthetic.main.item_footer_submit_button.view.*
import starbright.com.projectegg.R

class FooterSubmitButton(
    private var title: String
) : AbstractItem<FooterSubmitButton.ViewHolder>() {

    /** The layout for the given item */
    override val layoutRes: Int
        get() = R.layout.item_footer_submit_button

    /** The type of the Item. Can be a hardcoded INT, but preferred is a defined id */
    override val type: Int
        get() = R.id.footerSubmitButton

    /**
     * This method returns the ViewHolder for our item, using the provided View.
     *
     * @return the ViewHolder for this Item
     */
    override fun getViewHolder(v: View): ViewHolder = ViewHolder(v)

    inner class ViewHolder(itemView: View) : FastAdapter.ViewHolder<FooterSubmitButton>(itemView) {
        /** Binds the data of this item onto the viewHolder */
        override fun bindView(item: FooterSubmitButton, payloads: List<Any>) {
            itemView.btn_submit.text = item.title
        }

        /** View needs to release resources when its recycled */
        override fun unbindView(item: FooterSubmitButton) {
        }
    }

    class ItemClickListener(
        private var clickListener: () -> Unit
    ): ClickEventHook<FooterSubmitButton>() {
        override fun onBind(viewHolder: RecyclerView.ViewHolder): View? {
            return if (viewHolder is ViewHolder) {
                viewHolder.itemView.btn_submit
            } else super.onBind(viewHolder)
        }

        override fun onClick(
            v: View,
            position: Int,
            fastAdapter: FastAdapter<FooterSubmitButton>,
            item: FooterSubmitButton
        ) {
            clickListener.invoke()
        }
    }
}