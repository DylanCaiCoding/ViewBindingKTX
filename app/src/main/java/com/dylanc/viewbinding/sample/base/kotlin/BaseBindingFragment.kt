package com.dylanc.viewbinding.sample.base.kotlin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.dylanc.viewbinding.inflateBindingWithGeneric

class BaseBindingFragment<VB : ViewBinding> : Fragment() {

  lateinit var binding: VB

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
    binding = inflateBindingWithGeneric(layoutInflater)
    return binding.root
  }
}