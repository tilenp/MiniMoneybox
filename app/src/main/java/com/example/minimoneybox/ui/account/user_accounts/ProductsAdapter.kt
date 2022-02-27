package com.example.minimoneybox.ui.account.user_accounts

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.minimoneybox.model.local.Product

class ProductsAdapter(
    private val productsEvents: ProductsEvents
) : RecyclerView.Adapter<ProductsViewHolder>() {

    private var products: List<Product> = emptyList()

    fun setProducts(products: List<Product>) {
        this.products = products
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductsViewHolder {
        return ProductsViewHolder.create(parent = parent, productsEvents = productsEvents)
    }

    override fun onBindViewHolder(holder: ProductsViewHolder, position: Int) {
        holder.bind(products[position])
    }

    override fun getItemCount() = products.size
}