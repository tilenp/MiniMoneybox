package com.example.minimoneybox.ui.account.user_accounts

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.minimoneybox.R
import com.example.minimoneybox.dagger.AppComponentProvider
import com.example.minimoneybox.databinding.FragmentUserAccountsBinding
import com.example.minimoneybox.ui.account.user_accounts.*
import com.example.minimoneybox.utils.SchedulerProvider
import io.reactivex.rxjava3.disposables.CompositeDisposable
import javax.inject.Inject

class UserAccountsFragment : Fragment(), ProductsEvents {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var schedulerProvider: SchedulerProvider

    private var _binding: FragmentUserAccountsBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: UserAccountsViewModel
    private lateinit var productsAdapter: ProductsAdapter
    private val compositeDisposable = CompositeDisposable()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (requireActivity().application as AppComponentProvider).provideAppComponent().inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUserAccountsBinding.inflate(inflater, container, false)
        val view = binding.root
        setUpViewModel()
        setUpUI()
        return view
    }

    private fun setUpViewModel() {
        viewModel = ViewModelProvider(this, viewModelFactory)[UserAccountsViewModel::class.java]
    }

    private fun setUpUI() {
        productsAdapter = ProductsAdapter(this)
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            addItemDecoration(ProductsItemDecorator(requireContext()))
            adapter = productsAdapter
        }
        binding.retryButton.setOnClickListener { viewModel.onRetryClick() }
    }

    override fun onStart() {
        super.onStart()
        compositeDisposable.add(
            viewModel.getUiState()
                .observeOn(schedulerProvider.main())
                .subscribe({ updateState(it) }, {})
        )
    }

    private fun updateState(uiState: ProductsUIState) {
        when (uiState) {
            is ProductsUIState.Loading -> showLoading()
            is ProductsUIState.Content -> setContent(uiState.content)
            is ProductsUIState.Navigate -> navigate(uiState.actionId)
            is ProductsUIState.Message -> showMessage(uiState.messageId)
            is ProductsUIState.Retry ->  setRetry(uiState.messageId)
        }
    }

    private fun showLoading() {
        with(binding) {
            totalPlanValueTextView.isVisible = false
            retryButton.isVisible = false
            noProductsTextView.isVisible = false
            recyclerView.isVisible = false
            progressBar.isVisible = true
        }
    }

    private fun setContent(content: ProductsContent) {
        sayHi(content.userName)
        when {
            content.products.isEmpty() -> showNoProducts()
            else -> showProducts(content)
        }
    }

    private fun sayHi(userName: String?) {
        if (userName?.isNotBlank() == true) {
            binding.helloTextView.isVisible = true
            binding.helloTextView.text = String.format(getString(R.string.Hello), userName)
        } else {
            binding.helloTextView.isVisible = false
        }
    }

    private fun showNoProducts() {
        with(binding) {
            totalPlanValueTextView.isVisible = false
            retryButton.isVisible = false
            noProductsTextView.isVisible = true
            recyclerView.isVisible = false
            progressBar.isVisible = false
        }
    }

    private fun showProducts(content: ProductsContent) {
        with(binding) {
            totalPlanValueTextView.isVisible = true
            retryButton.isVisible = false
            noProductsTextView.isVisible = false
            recyclerView.isVisible = true
            progressBar.isVisible = false
            totalPlanValueTextView.text = String.format(getString(R.string.Total_Plan_Value), content.totalPlanValue)
        }
        productsAdapter.setProducts(content.products)
    }

    private fun navigate(actionId: Int) {
        findNavController().navigate(actionId)
    }

    private fun setRetry(messageId: Int) {
        showMessage(messageId)
        with(binding) {
            totalPlanValueTextView.isVisible = false
            retryButton.isVisible = true
            noProductsTextView.isVisible = false
            recyclerView.isVisible = false
            progressBar.isVisible = false
        }
    }

    private fun showMessage(@StringRes messageId: Int) {
        Toast.makeText(requireContext(), getString(messageId), Toast.LENGTH_LONG).show()
    }

    override fun onProductClick(productId: Long?) {
        viewModel.onProductClick(productId)
    }

    override fun onStop() {
        super.onStop()
        compositeDisposable.clear()
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }
}