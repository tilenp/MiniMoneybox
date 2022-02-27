package com.example.minimoneybox.ui.authentication

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.minimoneybox.R
import com.example.minimoneybox.dagger.AppComponentProvider
import com.example.minimoneybox.databinding.ActivityAuthenticationBinding
import com.example.minimoneybox.utils.SchedulerProvider
import com.example.minimoneybox.utils.setOrientationForDeviceType
import io.reactivex.rxjava3.disposables.CompositeDisposable
import javax.inject.Inject

class AuthenticationActivity : AppCompatActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var schedulerProvider: SchedulerProvider

    private lateinit var binding: ActivityAuthenticationBinding
    private lateinit var viewModel: AuthenticationViewModel

    private val compositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        (applicationContext as AppComponentProvider).provideAppComponent().inject(this)
        super.onCreate(savedInstanceState)
        binding = ActivityAuthenticationBinding.inflate(layoutInflater)
        setOrientationForDeviceType()
        setContentView(binding.root)
        setUpViewModel()
    }

    private fun setUpViewModel() {
        viewModel = ViewModelProvider(this, viewModelFactory)[AuthenticationViewModel::class.java]
    }

    override fun onStart() {
        super.onStart()
        compositeDisposable.add(
            viewModel.getAuthorizationCompleted()
                .observeOn(schedulerProvider.main())
                .subscribe({ authorizationCompleted() }, {})
        )
    }

    private fun authorizationCompleted() {
        setResult(Activity.RESULT_OK)
        finish()
    }

    override fun onStop() {
        super.onStop()
        compositeDisposable.clear()
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.dispose()
    }

    companion object {
        fun newInstance(context: Context): Intent {
            return Intent(context, AuthenticationActivity::class.java)
        }
    }
}