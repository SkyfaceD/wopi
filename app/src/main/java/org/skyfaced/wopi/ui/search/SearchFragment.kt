package org.skyfaced.wopi.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ConcatAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.skyfaced.wopi.R
import org.skyfaced.wopi.databinding.FragmentSearchBinding
import org.skyfaced.wopi.model.adapter.SearchItem
import org.skyfaced.wopi.model.response.Search
import org.skyfaced.wopi.ui.MainActivity
import org.skyfaced.wopi.ui.base.BaseAdapter
import org.skyfaced.wopi.ui.base.BaseFragment
import org.skyfaced.wopi.ui.search.adapter.ItemSearch
import org.skyfaced.wopi.utils.Response
import org.skyfaced.wopi.utils.extensions.hideKeyboard
import org.skyfaced.wopi.utils.extensions.isCoordinates
import timber.log.Timber

@AndroidEntryPoint
class SearchFragment : BaseFragment<FragmentSearchBinding>(R.layout.fragment_search) {
    private val viewModel by viewModels<SearchViewModel>()

    private val itemAdapter = BaseAdapter(listOf(ItemSearch(::onItemClick)))

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setFragmentResultListener(KEY_REQUEST_MAP_SEARCH, ::onMapSearchResult)
    }

    private fun onMapSearchResult(key: String, bundle: Bundle) {
        val lattLong = bundle.getString(KEY_LATT_LONG, "")
        binding {
            edtSearch.setText(lattLong)
            edtSearch.setSelection(lattLong.length)
            edtSearch.onEditorAction(EditorInfo.IME_ACTION_SEARCH)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding {
            swipeRefresh.isEnabled = false

            //TODO Add header
            val adapter = ConcatAdapter(itemAdapter)
            recyclerSearch.adapter = adapter

            edtSearch.setOnEditorActionListener { _, actionId, _ ->
                if (edtSearch.text.isNullOrBlank()) {
                    //TODO Show ui hint for this condition
                    return@setOnEditorActionListener false
                }

                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    val query = edtSearch.text?.toString()!!

                    if (query == viewModel.query) {
                        //TODO Show ui hint for this condition
                        (requireActivity() as MainActivity).hideKeyboard()
                        return@setOnEditorActionListener false
                    }

                    (requireActivity() as MainActivity).hideKeyboard()
                    viewModel.saveQueryAndStartSearching(query)
                    return@setOnEditorActionListener true
                }

                return@setOnEditorActionListener false
            }

            btnMap.setOnClickListener {
                val lattLong = edtSearch.text?.toString() ?: ""
                val destination = SearchFragmentDirections.actionSearchFragmentToMapFragment(
                    if (lattLong.isCoordinates) lattLong else null
                )
                findNavController().navigate(destination)
            }
        }

        setupSearchObserver()
    }

    private fun onItemClick(item: SearchItem) {
        val destination = SearchFragmentDirections.actionSearchFragmentToDetailFragment(item.id)
        findNavController().navigate(destination)
    }

    private fun setupSearchObserver() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.searchResult.flowWithLifecycle(
                viewLifecycleOwner.lifecycle,
                Lifecycle.State.STARTED
            ).collect { response ->
                Timber.d(response.toString())

                binding {
                    swipeRefresh.isRefreshing = when (response) {
                        is Response.Loading -> true
                        else -> false
                    }

                    if (response is Response.Success) {
                        val searchItem = response.data.map(Search::toSearchItem)
                        itemAdapter.submitList(searchItem)
                    }
                }
            }
        }
    }

    override fun setupBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
    ): FragmentSearchBinding {
        return FragmentSearchBinding.inflate(inflater, container, false)
    }

    companion object {
        const val KEY_REQUEST_MAP_SEARCH = "requestMapSearch"
        const val KEY_LATT_LONG = "lattLong"
    }
}