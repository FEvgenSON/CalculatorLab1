package ru.fevgenson.calculator

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter

class ExpressionsAdapter : ListAdapter<Expression, ExpressionViewHolder>(ExpressionItemCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExpressionViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val viewHolderRoot = inflater.inflate(R.layout.exoression_item, parent, false)
        return ExpressionViewHolder(viewHolderRoot)
    }

    override fun onBindViewHolder(holder: ExpressionViewHolder, position: Int) {
        holder.expression = getItem(position)
    }

    class ExpressionItemCallback : DiffUtil.ItemCallback<Expression>() {
        override fun areItemsTheSame(oldItem: Expression, newItem: Expression): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Expression, newItem: Expression): Boolean =
            oldItem.value == newItem.value
    }
}