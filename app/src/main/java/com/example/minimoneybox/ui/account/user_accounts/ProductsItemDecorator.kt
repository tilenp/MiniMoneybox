package com.example.minimoneybox.ui.account.user_accounts

import android.content.Context
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.minimoneybox.R

class ProductsItemDecorator(
    context: Context
) : RecyclerView.ItemDecoration() {

    private val spacing = context.resources.getDimensionPixelSize(R.dimen.spacing_8)

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        outRect.top = spacing
        outRect.bottom = spacing
    }
}