@file:Suppress("unused")

package com.dylanc.viewbinding

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

/**
 * @author Dylan Cai
 */

inline fun <reified VB : ViewBinding> newBindingViewHolder(parent: ViewGroup): BindingViewHolder<VB> =
  BindingViewHolder.create(VB::class.java, parent)

fun <VB : ViewBinding> Any.newBindingViewHolderWithGeneric(parent: ViewGroup): BindingViewHolder<VB> =
  BindingViewHolder.createWithGeneric(this, parent)

class BindingViewHolder<VB : ViewBinding>(val binding: VB) : RecyclerView.ViewHolder(binding.root) {
  companion object {
    @JvmStatic
    fun <VB : ViewBinding> create(clazz: Class<VB>, parent: ViewGroup) =
      BindingViewHolder(inflateBinding(clazz, LayoutInflater.from(parent.context), parent, false))

    @JvmStatic
    fun <VB : ViewBinding> createWithGeneric(any: Any, parent: ViewGroup) =
      BindingViewHolder<VB>(any.inflateBindingWithGeneric(LayoutInflater.from(parent.context), parent, false))
  }
}
