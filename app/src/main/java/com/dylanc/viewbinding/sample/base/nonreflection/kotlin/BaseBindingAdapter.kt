package com.dylanc.viewbinding.sample.base.nonreflection.kotlin

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.dylanc.viewbinding.BindingViewHolder

abstract class BaseBindingAdapter<VB : ViewBinding>(
  private val inflate: (LayoutInflater, ViewGroup, Boolean) -> VB
) : RecyclerView.Adapter<BindingViewHolder<VB>>() {

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
    BindingViewHolder(inflate, parent)
}