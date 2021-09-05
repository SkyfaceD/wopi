package org.skyfaced.wopi.ui.detail

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
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
import org.skyfaced.wopi.model.adapter.DetailItem
import org.skyfaced.wopi.ui.base.BaseFragment
import org.skyfaced.wopi.utils.Response
import org.skyfaced.wopi.utils.SquareDecoration
import org.skyfaced.wopi.utils.StartSnapHelper
import org.skyfaced.wopi.utils.di.SavedStateViewModelFactory
import org.skyfaced.wopi.utils.extensions.handleNetworkException
import org.skyfaced.wopi.utils.extensions.isLoading
import org.skyfaced.wopi.utils.extensions.lazySafetyNone
import org.skyfaced.wopi.utils.extensions.toggleShimmer
import org.skyfaced.wopi.utils.result.HttpException
import timber.log.Timber
import javax.inject.Inject
import kotlin.random.Random.Default.nextBoolean

//TODO Fix view state
@AndroidEntryPoint
class DetailFragment : BaseFragment<FragmentDetailBinding>(R.layout.fragment_detail) {

    private val args by navArgs<DetailFragmentArgs>()

    @Inject
    lateinit var viewModelFactory: DetailViewModel.Factory
    private val viewModel: DetailViewModel by viewModels {
        SavedStateViewModelFactory(
            viewModelFactory,
            this,
            bundleOf(BUNDLE_KEY_WOEID to args.woeid)
        )
    }

    private val adapter by lazySafetyNone { WeekAdapter(::onItemClick) }
    private var isFavorite = nextBoolean()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Timber.d("$args")
        binding {
            content.txtCity.text = args.city

            content.recyclerWeek.setHasFixedSize(true)
            val weekMargin = resources.getDimension(R.dimen.margin_4dp).toInt()
            content.recyclerWeek.addItemDecoration(SquareDecoration(weekMargin))
            StartSnapHelper().attachToRecyclerView(content.recyclerWeek)
            content.recyclerWeek.adapter = adapter

//            content.btnHistory.setOnClickListener {
//                val destination =
//                    DetailFragmentDirections.actionDetailFragmentToHistoryFragment(args.woeid)
//                findNavController().navigate(destination)
//            }

            changeFavoriteImage()
            content.btnFavorite.setOnClickListener {
                isFavorite = !isFavorite
                changeFavoriteImage()
            }

            error.btnRetry.setOnClickListener {
                error.anim.cancelAnimation()
                viewModel.retry()
            }
        }

        setupDetailObserver()
    }

    private fun changeFavoriteImage() {
        val favoriteRes = if (isFavorite) R.drawable.ic_favorite else R.drawable.ic_unfavorite
        binding.content.btnFavorite.setImageResource(favoriteRes)
    }

    private fun setupDetailObserver() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.detailsResponse.flowWithLifecycle(lifecycle).collect { response ->
                Timber.d(response.toString())

                if (response.isLoading()) {
                    binding.placeholder.root.toggleShimmer(true)
                    binding.content.root.isVisible = false
                    binding.error.root.isVisible = false
                } else {
                    binding.placeholder.root.toggleShimmer(false)
                }

                when (response) {
                    is Response.Success -> {
                        binding.content.root.isVisible = true

                        val details = response.data.mapIndexed { idx, item ->
                            item.copy(week = item.week.copy(isChecked = idx == 0))
                        }

                        viewModel.updateDetails(details)
                        adapter.submitList(details)

                        updateContent(details[0])
                    }
                    is Response.Error -> {
                        binding.error.root.isVisible = true
                        binding.error.anim.playAnimation()

                        setException(response.cause)
                    }
                }
            }
        }
    }

    private fun setException(cause: Throwable?) {
        Timber.d(cause)
        with(binding.error) {
            txtMessage.text = when (cause) {
                is HttpException -> handleNetworkException(cause)
                else -> getString(R.string.error_unexpected)
            }
        }
    }

    private fun onItemClick(item: DetailItem) {
        val oldItem = viewModel.details.find { it.week.isChecked }
            ?: throw IllegalArgumentException("Can't be null")

        if (item == oldItem) return

        changeCardSelection(item)
        updateContent(item)
    }

    @SuppressLint("SetTextI18n")
    private fun updateContent(item: DetailItem) {
        with(binding.content) {
            imgWeatherState.setImageResource(item.weatherState.drawableRes)
            txtTemperature.text = item.helper.celsius
            txtWeatherState.text = item.weatherState.value
            txtDate.text = item.dateStr
            txtWindSpeed.text =
                getString(R.string.placeholder_wind_speed, item.helper.windMph)
            txtAirPressure.text =
                getString(R.string.placeholder_air_pressure, item.helper.airPressureStr)
            txtHumidity.text =
                getString(R.string.placeholder_humidity, item.helper.humidityStr)
            txtVisibility.text =
                getString(R.string.placeholder_visibility, item.helper.visibilityMiles)
        }
    }

    private fun changeCardSelection(item: DetailItem) {
        val details = viewModel.details

        val itemPosition = details.indexOf(item)
        val newDetails = details.mapIndexed { index, item ->
            item.copy(week = item.week.copy(isChecked = index == itemPosition))
        }

        viewModel.updateDetails(newDetails)
        adapter.submitList(viewModel.details)

        binding.content.recyclerWeek.scrollToPosition(itemPosition)
    }

    override fun setupBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
    ): FragmentDetailBinding {
        return FragmentDetailBinding.inflate(inflater, container, false)
    }

    companion object {
        const val BUNDLE_KEY_WOEID = "woeid"
    }
}