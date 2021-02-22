package ru.fevgenson.calculator

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private companion object {

        const val BOTTOM_ITEM = 0
    }

    private val viewModel: MainViewModel by viewModels()

    private val expressionsAdapter = ExpressionsAdapter().apply {
        //Доскроливаем до низа при добовлении новго элемента, чтобы красиво работала анимация
        registerAdapterDataObserver(
            object : RecyclerView.AdapterDataObserver() {
                override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                    if (positionStart == BOTTOM_ITEM) {
                        val layoutManager = expressions.layoutManager as LinearLayoutManager
                        layoutManager.scrollToPosition(BOTTOM_ITEM)
                    }
                }
            }
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initRecyclerView()
        observeButtons()
        viewModel.expressionList.observe(this, expressionsAdapter::submitList)
        viewModel.error.observe(this, ::showToast)
    }

    private fun initRecyclerView() {
        (expressions.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
        expressions.adapter = expressionsAdapter
    }

    private fun observeButtons() {
        zero.setOnClickListener { viewModel.addExpressionPart(MainViewModel.ExpressionParts.ZERO) }
        one.setOnClickListener { viewModel.addExpressionPart(MainViewModel.ExpressionParts.ONE) }
        two.setOnClickListener { viewModel.addExpressionPart(MainViewModel.ExpressionParts.TWO) }
        three.setOnClickListener { viewModel.addExpressionPart(MainViewModel.ExpressionParts.THREE) }
        four.setOnClickListener { viewModel.addExpressionPart(MainViewModel.ExpressionParts.FOUR) }
        five.setOnClickListener { viewModel.addExpressionPart(MainViewModel.ExpressionParts.FIVE) }
        six.setOnClickListener { viewModel.addExpressionPart(MainViewModel.ExpressionParts.SIX) }
        seven.setOnClickListener { viewModel.addExpressionPart(MainViewModel.ExpressionParts.SEVEN) }
        eight.setOnClickListener { viewModel.addExpressionPart(MainViewModel.ExpressionParts.EIGHT) }
        nine.setOnClickListener { viewModel.addExpressionPart(MainViewModel.ExpressionParts.NINE) }

        dot.setOnClickListener { viewModel.addExpressionPart(MainViewModel.ExpressionParts.DOT) }
        minus.setOnClickListener { viewModel.addExpressionPart(MainViewModel.ExpressionParts.MINUS) }
        plus.setOnClickListener { viewModel.addExpressionPart(MainViewModel.ExpressionParts.PLUS) }
        divide.setOnClickListener { viewModel.addExpressionPart(MainViewModel.ExpressionParts.DIVIDE) }
        multiply.setOnClickListener { viewModel.addExpressionPart(MainViewModel.ExpressionParts.MULTIPLY) }
        sinus.setOnClickListener { viewModel.addExpressionPart(MainViewModel.ExpressionParts.SINUS) }
        leftBracket.setOnClickListener { viewModel.addExpressionPart(MainViewModel.ExpressionParts.LEFT_BRACKET) }
        rightBracket.setOnClickListener { viewModel.addExpressionPart(MainViewModel.ExpressionParts.RIGHT_BRACKET) }

        removeLastSymbol.setOnClickListener { viewModel.removeLastSymbol() }
        equal.setOnClickListener { viewModel.calculate() }
        clear.setOnClickListener { viewModel.clear() }
    }

    private fun showToast(stringRes: Int) {
        Toast.makeText(this, stringRes, Toast.LENGTH_SHORT).show()
    }
}