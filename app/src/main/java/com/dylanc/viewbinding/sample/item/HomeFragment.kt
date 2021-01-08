package com.dylanc.viewbinding.sample.item

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.dylanc.viewbinding.binding
import com.dylanc.viewbinding.sample.R
import com.dylanc.viewbinding.sample.databinding.FragmentHomeBinding

class HomeFragment : Fragment(R.layout.fragment_home) {

  private val binding: FragmentHomeBinding by binding()

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
  }
}