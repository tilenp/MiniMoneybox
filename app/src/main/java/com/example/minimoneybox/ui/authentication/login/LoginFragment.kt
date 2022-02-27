package com.example.minimoneybox.ui.authentication.login

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.minimoneybox.dagger.AppComponentProvider
import com.example.minimoneybox.databinding.FragmentLoginBinding
import com.example.minimoneybox.model.response.ErrorBody
import com.example.minimoneybox.model.response.ValidationError
import com.example.minimoneybox.utils.SchedulerProvider
import com.example.minimoneybox.ui.authentication.AuthenticationUIState
import com.example.minimoneybox.ui.authentication.login.LoginViewModel.Companion.EMAIL
import com.example.minimoneybox.ui.authentication.login.LoginViewModel.Companion.PASSWORD
import com.google.android.material.textfield.TextInputLayout
import com.jakewharton.rxbinding4.widget.textChanges
import io.reactivex.rxjava3.disposables.CompositeDisposable
import javax.inject.Inject

class LoginFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var schedulerProvider: SchedulerProvider

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: LoginViewModel
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
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        val view = binding.root
        setUpViewModel()
        setUpUi()
        return view
    }

    private fun setUpViewModel() {
        viewModel = ViewModelProvider(this, viewModelFactory)[LoginViewModel::class.java]
    }

    private fun setUpUi() {
        with(binding) {
            loginButton.setOnClickListener {
                val email = emailEditText.text.toString()
                val password = passwordEditText.text.toString()
                val name = nameEditText.text.toString()
                viewModel.login(email, password, name)
            }
        }
    }

    override fun onStart() {
        super.onStart()
        compositeDisposable.add(
            viewModel.observeUIState()
                .observeOn(schedulerProvider.main())
                .subscribe({ updateState(it) }, {})
        )

        compositeDisposable.add(
            binding.emailEditText.textChanges()
                .skipInitialValue()
                .subscribe({binding.emailContainer.isErrorEnabled = false}, {})
        )

        compositeDisposable.add(
            binding.passwordEditText.textChanges()
                .skipInitialValue()
                .subscribe({binding.passwordContainer.isErrorEnabled = false}, {})
        )
    }

    private fun updateState(uiState: AuthenticationUIState) {
        when (uiState) {
            is AuthenticationUIState.Loading -> showLoading(true)
            is AuthenticationUIState.NotLoading -> showLoading(false)
            is AuthenticationUIState.Error -> handleErrors(uiState.error)
            is AuthenticationUIState.Message -> showMessage(getString(uiState.messageId))
        }
    }

    private fun showLoading(showLoading: Boolean) {
        with(binding) {
            emailEditText.isEnabled = !showLoading
            passwordEditText.isEnabled = !showLoading
            nameEditText.isEnabled = !showLoading
            loginButton.isEnabled = !showLoading
            progressBar.isVisible = showLoading
        }
    }

    private fun handleErrors(error: ErrorBody) {
        if (error.ValidationErrors.isEmpty()) {
            showMessage(error.Message)
        } else {
            error.ValidationErrors.forEach { showFieldError(it) }
        }
    }

    private fun showFieldError(validationError: ValidationError) {
        when (validationError.Name) {
            EMAIL -> showError(binding.emailContainer, validationError.Message)
            PASSWORD -> showError(binding.passwordContainer, validationError.Message)
        }
    }

    private fun showError(textInputLayout: TextInputLayout, error: String) {
        textInputLayout.isErrorEnabled = true
        textInputLayout.error = error
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