@file:Suppress("unused")

package com.dylanc.viewbinding.sample.base.nonreflection.kotlin

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.dylanc.viewbinding.BindingViewHolder
import com.dylanc.viewbinding.base.inflateBindingWithGeneric


abstract class BaseBindingQuickAdapter<T, VB : ViewBinding>(
  private val inflate: (LayoutInflater, ViewGroup, Boolean) -> VB,
  layoutResId: Int = -1
) :
  BaseQuickAdapter<T, BaseBindingQuickAdapter.BaseBindingHolder<VB>>(layoutResId) {

  override fun onCreateDefViewHolder(parent: ViewGroup, viewType: Int): BaseBindingHolder<VB> =
    BaseBindingHolder(inflate(LayoutInflater.from(parent.context), parent, false))

  override fun convert(holder: BaseBindingHolder<VB>, item: T) {
    convert(holder.binding, holder.layoutPosition, item)
  }

  abstract fun convert(binding: VB, position: Int, item: T)

  class BaseBindingHolder<VB : ViewBinding>(val binding: VB) : BaseViewHolder(binding.root)
}