package org.skyfaced.wopi.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding

/**
 * @param layoutId unused, required only for define and quick access to corresponding .xml
 */
abstract class BaseFragment<VB : ViewBinding>(@LayoutRes layoutId: Int) : Fragment() {
    private var _binding: VB? = null
    protected val binding get() = requireNotNull(_binding) { "Binding isn't initialized" }

    protected fun binding(block: VB.() -> Unit) = binding.apply(block)

    protected abstract fun setupBinding(inflater: LayoutInflater, container: ViewGroup?): VB

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = setupBinding(inflater, container)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}