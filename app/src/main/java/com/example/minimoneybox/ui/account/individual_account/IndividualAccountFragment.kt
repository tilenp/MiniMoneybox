package com.example.minimoneybox.ui.account.individual_account

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.minimoneybox.R
import com.example.minimoneybox.dagger.AppComponentProvider
import com.example.minimoneybox.databinding.FragmentIndividualAccountBinding
import com.example.minimoneybox.utils.SchedulerProvider
import io.reactivex.rxjava3.disposables.CompositeDisposable
import javax.inject.Inject

class IndividualAccountFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var schedulerProvider: SchedulerProvider

    private var _binding: FragmentIndividualAccountBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: IndividualAccountViewModel
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
        _binding = FragmentIndividualAccountBinding.inflate(inflater, container, false)
        val view = binding.root
        setUpViewModel()
        setUpUi()
        return view
    }

    private fun setUpViewModel() {
        viewModel = ViewModelProvider(this, viewModelFactory)[IndividualAccountViewModel::class.java]
    }

    private fun setUpUi() {
        with(binding) {
            addAmountButton.setOnClickListener {
                viewModel.addAmount()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        compositeDisposable.add(
            viewModel.getUiState()
                .observeOn(schedulerProvider.main())
                .subscribe({ updateState(it) }, {})
        )
    }

    private fun updateState(uiState: IndividualAccountUIState) {
        when (uiState) {
            is IndividualAccountUIState.Loading -> showLoading(true)
            is IndividualAccountUIState.NotLoading -> showLoading(false)
            is IndividualAccountUIState.Content -> setContent(uiState.content)
            is IndividualAccountUIState.ServerMessage -> showMessage(uiState.message)
            is IndividualAccountUIState.Message -> showMessage(getString(uiState.messageId))
        }
    }

    private fun showLoading(showLoading: Boolean) {
        with(binding) {
            progressBar.isVisible = showLoading
            addAmountButton.isEnabled = !showLoading
        }
    }

    private fun setContent(content: IndividualAccountContent) {
        with(binding) {
            progressBar.isVisible = false
            accountNameTextView.text = content.product?.data?.name
            planValueTextView.text = String.format(getString(R.string.Plan_Value), content.product?.planValue?.formattedAmount)
            moneyboxTextView.text = String.format(getString(R.string.Moneybox), content.product?.moneybox?.formattedAmount)
            addAmountButton.text = content.amount.formattedAmount
        }
    }

    private fun showMessage(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
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