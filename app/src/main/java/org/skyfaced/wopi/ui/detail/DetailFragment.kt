package org.skyfaced.wopi.ui.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.skyfaced.wopi.R
import org.skyfaced.wopi.databinding.FragmentDetailBinding
import org.skyfaced.wopi.ui.base.BaseFragment
import org.skyfaced.wopi.utils.Response
import org.skyfaced.wopi.utils.extensions.isLoading
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class DetailFragment : BaseFragment<FragmentDetailBinding>(R.layout.fragment_detail) {

    private val args by navArgs<DetailFragmentArgs>()
    @Inject
    lateinit var viewModelFactory: DetailViewModel.Factory
    private val viewModel: DetailViewModel by viewModels {
        DetailViewModel.provideFactory(viewModelFactory, args.woeid)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Timber.d("$args")
        binding {
            content.swipeRefresh.setOnRefreshListener {
                viewModel.refresh()
            }
        }

        setupDetailObserver()
    }

    private fun setupDetailObserver() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.detail.flowWithLifecycle(lifecycle).collect { response ->
                Timber.d(response.toString())

                val isLoading = response.isLoading()
                if (!isLoading) {
                    binding.placeholder.root.hideShimmer()
                } else {
                    binding.placeholder.root.showShimmer(true)
                }
                binding.content.root.isVisible = !isLoading
                binding.content.swipeRefresh.isRefreshing = isLoading

                if (response is Response.Success) {
                    binding.placeholder.root.isVisible = false
                    binding.content.root.isVisible = true
                    val (general, today, week) = response.data
                    with(binding.content) {
                        txtCity.text = general.city
                        imgWeatherState.setImageResource(general.weatherState.drawableRes)
                        txtTemperature.text = general.temperature
                        txtWeatherState.text = getString(general.weatherState.stringRes)
                        txtDate.text = general.date
                        txtWindSpeed.text = general.wind
                        txtAirPressure.text = general.pressure
                        txtHumidity.text = general.humidity

                        recyclerToday.adapter = SimpleTodayAdapter(today)
                        recyclerToday.setHasFixedSize(true)
                    }
                }
            }
        }
    }

    override fun setupBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
    ): FragmentDetailBinding {
        return FragmentDetailBinding.inflate(inflater, container, false)
    }
}