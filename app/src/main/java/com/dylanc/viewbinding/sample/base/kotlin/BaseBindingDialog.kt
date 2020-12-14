@file:Suppress("unused")

package com.dylanc.viewbinding.sample.base.kotlin

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import androidx.viewbinding.ViewBinding
import com.dylanc.viewbinding.inflateBindingWithGeneric

abstract class BaseBindingDialog<VB : ViewBinding>(context: Context, themeResId: Int) : Dialog(context, themeResId) {

  lateinit var binding: VB

  constructor(context: Context) : this(context, 0)

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = inflateBindingWithGeneric(layoutInflater)
    setContentView(binding.root)
  }

}