package com.example.minimoneybox.ui.account.user_accounts

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.minimoneybox.R
import com.example.minimoneybox.databinding.ViewHolderProductBinding
import com.example.minimoneybox.model.local.Product

class ProductsViewHolder(
    private val binding: ViewHolderProductBinding,
    private val productsEvents: ProductsEvents
) : RecyclerView.ViewHolder(binding.root) {

    private lateinit var product: Product

    init {
        binding.root.setOnClickListener { productsEvents.onProductClick(product.id) }
    }

    fun bind(product: Product) {
        this.product = product
        with(binding) {
            accountNameTextView.text = product.data.name
            planValueTextView.text = String.format(planValueTextView.context.getString(R.string.Plan_Value), product.planValue.formattedAmount)
            moneyboxTextView.text = String.format(moneyboxTextView.context.getString(R.string.Moneybox), product.moneybox.formattedAmount)
        }
    }

    companion object {
        fun create(parent: ViewGroup, productsEvents: ProductsEvents): ProductsViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.view_holder_product, parent, false)
            val binding = ViewHolderProductBinding.bind(view)
            return ProductsViewHolder(binding = binding, productsEvents = productsEvents)
        }
    }
}