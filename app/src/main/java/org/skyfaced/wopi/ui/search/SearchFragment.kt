package org.skyfaced.wopi.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
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
import timber.log.Timber

@AndroidEntryPoint
class SearchFragment : BaseFragment<FragmentSearchBinding>(R.layout.fragment_search) {
    private val viewModel by viewModels<SearchViewModel>()

    private val itemAdapter = BaseAdapter(listOf(ItemSearch(::onItemClick)))

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding {
            swipeRefresh.isEnabled = false

            //TODO Add header
            val adapter = ConcatAdapter(itemAdapter)
            recyclerSearch.adapter = adapter

            edtSearch.setOnEditorActionListener { _, actionId, _ ->
                if (edtSearch.text.isBlank()) {
                    //TODO Show ui hint for this condition
                    return@setOnEditorActionListener false
                }

                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    if (edtSearch.text.toString() == viewModel.query) {
                        //TODO Show ui hint for this condition
                        (requireActivity() as MainActivity).hideKeyboard()
                        return@setOnEditorActionListener false
                    }

                    (requireActivity() as MainActivity).hideKeyboard()
                    viewModel.saveQueryAndStartSearching(edtSearch.text?.toString()!!)
                    return@setOnEditorActionListener true
                }

                return@setOnEditorActionListener false
            }

            btnMap.setOnClickListener {
                //TODO Navigate to map screen
            }
        }

        setupSearchObserver()
    }

    private fun onItemClick(item: SearchItem) {
        //TODO Navigate to detail screen
    }

    private fun setupSearchObserver() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.searchResult.flowWithLifecycle(
                viewLifecycleOwner.lifecycle,
                Lifecycle.State.STARTED
            ).collect { response ->
                Timber.d(when (response) {
                    is Response.Success -> response.data.joinToString()
                    is Response.Error -> response.message ?: response.cause?.stackTraceToString()
                    is Response.Load -> "Loading..."
                })

                binding {
                    swipeRefresh.isRefreshing = when (response) {
                        is Response.Load -> true
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
}