package org.skyfaced.wopi.ui.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import org.skyfaced.wopi.R
import org.skyfaced.wopi.databinding.FragmentHistoryBinding
import org.skyfaced.wopi.ui.base.BaseFragment
import timber.log.Timber

class HistoryFragment : BaseFragment<FragmentHistoryBinding>(R.layout.fragment_history) {
    private val args by navArgs<HistoryFragmentArgs>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Timber.e("$args")
    }

    override fun setupBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
    ): FragmentHistoryBinding {
        return FragmentHistoryBinding.inflate(
            inflater,
            container,
            false
        )
    }
}