package com.dylanc.viewbinding.base

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding

interface BindingOwner<VB : ViewBinding> {
  val Activity.binding: VB
  val Fragment.binding: VB
  fun Activity.setContentViewWithBinding()
  fun Fragment.createViewWithBinding(inflater: LayoutInflater, container: ViewGroup?): View
}

class BindingOwnerDelegate<VB : ViewBinding> : BindingOwner<VB> {
  private lateinit var _binding: VB

  override val Activity.binding: VB get() = _binding

  @Suppress("UNCHECKED_CAST")
  override val Fragment.binding: VB
    get() = requireNotNull(view) { "The property of binding has been destroyed." }
      .getTag(R.id.tag_view_binding) as VB

  override fun Activity.setContentViewWithBinding() {
    _binding = ViewBindingUtil.inflateWithGeneric(this, layoutInflater)
    setContentView(binding.root)
  }

  override fun Fragment.createViewWithBinding(inflater: LayoutInflater, container: ViewGroup?): View =
    ViewBindingUtil.inflateWithGeneric<VB>(this, inflater, container, false)
      .run {
        root.setTag(R.id.tag_view_binding, this)
        root
      }
}