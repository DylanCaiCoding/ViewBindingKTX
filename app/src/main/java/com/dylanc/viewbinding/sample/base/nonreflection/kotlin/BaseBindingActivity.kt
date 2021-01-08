package com.dylanc.viewbinding.sample.base.nonreflection.kotlin

import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding

abstract class BaseBindingActivity<VB : ViewBinding>(
  private val inflate: (LayoutInflater) -> VB
) : AppCompatActivity() {

  lateinit var binding: VB

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = inflate(layoutInflater)
    setContentView(binding.root)
  }

}