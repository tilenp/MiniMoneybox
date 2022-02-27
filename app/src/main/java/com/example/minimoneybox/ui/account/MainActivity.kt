package com.example.minimoneybox.ui.account

import android.app.Activity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.example.minimoneybox.R
import com.example.minimoneybox.dagger.AppComponentProvider
import com.example.minimoneybox.databinding.ActivityMainBinding
import com.example.minimoneybox.ui.authentication.AuthenticationActivity
import com.example.minimoneybox.utils.SchedulerProvider
import io.reactivex.rxjava3.disposables.CompositeDisposable
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var schedulerProvider: SchedulerProvider

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: AccountViewModel

    private val compositeDisposable = CompositeDisposable()

    private val resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode != Activity.RESULT_OK) {
                finish()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        (applicationContext as AppComponentProvider).provideAppComponent().inject(this)
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setUpViewModel()
        setUpToolbar()
    }

    private fun setUpViewModel() {
        viewModel = ViewModelProvider(this, viewModelFactory)[AccountViewModel::class.java]
    }

    private fun setUpToolbar() {
        setSupportActionBar(binding.myToolbar)
        val navController = findNavController(R.id.main_host_fragment)
        val appBarConfiguration = AppBarConfiguration(navController.graph)
        NavigationUI.setupWithNavController(binding.myToolbar, navController, appBarConfiguration)
    }

    override fun onStart() {
        super.onStart()
        compositeDisposable.add(
            viewModel.authenticationCompleted()
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.main())
                .subscribe({ openAuthenticationActivity() }, {})
        )
    }

    private fun openAuthenticationActivity() {
        val transitionAnimation = ActivityOptionsCompat.makeCustomAnimation(
            this,
            R.anim.slide_in_right,
            R.anim.slide_out_left
        )
        resultLauncher.launch(AuthenticationActivity.newInstance(this), transitionAnimation)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_log_out-> {
                viewModel.logOut()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onStop() {
        super.onStop()
        compositeDisposable.clear()
    }

    override fun finish() {
        super.finish()
        compositeDisposable.clear()
    }
}