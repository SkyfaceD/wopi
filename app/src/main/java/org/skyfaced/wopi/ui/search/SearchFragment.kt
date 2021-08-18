package org.skyfaced.wopi.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.skyfaced.wopi.R
import org.skyfaced.wopi.databinding.FragmentSearchBinding
import org.skyfaced.wopi.model.adapter.SearchItem
import org.skyfaced.wopi.model.response.Search
import org.skyfaced.wopi.ui.BaseFragment
import org.skyfaced.wopi.utils.Response
import timber.log.Timber

@AndroidEntryPoint
class SearchFragment : BaseFragment<FragmentSearchBinding>(R.layout.fragment_search) {
    private val viewModel by viewModels<SearchViewModel>()

    private val adapter = SearchAdapter(::onSearchItemClick)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding {
            rvSearch.adapter = adapter

            srl.setOnRefreshListener {
                viewModel.searchByLocation()
            }

            edtSearch.doAfterTextChanged {
                val query = it?.toString() ?: return@doAfterTextChanged
                if (query.isBlank()) return@doAfterTextChanged

                viewModel.updateSearchQuery(query)
            }

            edtSearch.setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    viewModel.searchByLocation()
                    return@setOnEditorActionListener true
                }

                return@setOnEditorActionListener false
            }
        }

        setupSearchObserver()
    }

    private fun onSearchItemClick(searchItem: SearchItem) {
        Timber.d(searchItem.toString())
    }

    private fun setupSearchObserver() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.searchLocation.flowWithLifecycle(
                viewLifecycleOwner.lifecycle,
                Lifecycle.State.STARTED
            ).collect { search ->
                Timber.d(when (search) {
                    is Response.Success -> search.data.joinToString()
                    is Response.Error -> search.message
                    is Response.Load -> "Loading..."
                })

                binding {
                    srl.isRefreshing = when (search) {
                        is Response.Success -> false
                        is Response.Error -> false
                        is Response.Load -> true
                    }

                    if (search is Response.Success) {
                        val searchItem = search.data.map(Search::toSearchItem)
                        adapter.submitList(searchItem)

                        emptyView.isVisible = searchItem.isEmpty()
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