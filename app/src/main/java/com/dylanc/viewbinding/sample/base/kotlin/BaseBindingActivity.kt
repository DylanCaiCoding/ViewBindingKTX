package com.dylanc.viewbinding.sample.base.kotlin

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import com.dylanc.viewbinding.inflateBindingWithGeneric

abstract class BaseBindingActivity<VB : ViewBinding> : AppCompatActivity() {

  lateinit var binding: VB

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = inflateBindingWithGeneric(layoutInflater)
    setContentView(binding.root)
  }

}