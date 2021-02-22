package ru.fevgenson.calculator

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.exoression_item.view.*

class ExpressionViewHolder(root: View) : RecyclerView.ViewHolder(root) {

    var expression: Expression? = null
        set(value) {
            field = expression
            itemView.expression.text = value?.value
        }
}