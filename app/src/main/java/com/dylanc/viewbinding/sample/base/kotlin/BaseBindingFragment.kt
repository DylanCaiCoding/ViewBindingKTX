@file:Suppress("unused", "MemberVisibilityCanBePrivate")

package com.dylanc.viewbinding.sample.base.kotlin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.dylanc.viewbinding.inflateBindingWithGeneric

abstract class BaseBindingFragment<VB : ViewBinding> : Fragment() {

  private var _binding: VB? = null
  val binding:VB get() = _binding!!

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
    _binding = inflateBindingWithGeneric(layoutInflater)
    return binding.root
  }

  override fun onDestroyView() {
    super.onDestroyView()
    _binding = null
  }
}