package com.dylanc.viewbinding.sample.base.kotlin

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.dylanc.viewbinding.BindingViewHolder
import com.dylanc.viewbinding.newBindingViewHolderWithGeneric

abstract class BaseBindingAdapter<VB : ViewBinding, T> : RecyclerView.Adapter<BindingViewHolder<VB>>() {

  var list: List<T> = arrayListOf()

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = newBindingViewHolderWithGeneric<VB>(parent)

  override fun onBindViewHolder(holder: BindingViewHolder<VB>, position: Int) {
    onBindViewHolder(holder, list[position])
  }

  abstract fun onBindViewHolder(holder: BindingViewHolder<VB>, item: T)

  override fun getItemCount() = list.size
}