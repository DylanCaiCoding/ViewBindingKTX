@file:Suppress("unused")

package com.dylanc.viewbinding.sample

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.dylanc.viewbinding.BindingLifecycleOwner
import com.dylanc.viewbinding.binding
import com.dylanc.viewbinding.sample.databinding.FragmentHomeBinding

/**
 * @author Dylan Cai
 */
class HomeFragment : Fragment(R.layout.fragment_home), BindingLifecycleOwner {

  private val binding: FragmentHomeBinding by binding()

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    binding.tvHelloWorld.text = "Hello Android!"
  }

  override fun onDestroyViewBinding(destroyingBinding: ViewBinding) {
    // Release something before destroying the binding
  }

}