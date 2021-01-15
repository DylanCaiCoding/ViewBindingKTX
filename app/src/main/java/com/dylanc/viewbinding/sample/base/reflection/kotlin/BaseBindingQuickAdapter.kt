package com.dylanc.viewbinding.sample.base.reflection.kotlin

import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.chad.library.adapter.base.BaseQuickAdapter
import com.dylanc.viewbinding.base.inflateBindingWithGeneric
import com.dylanc.viewbinding.brvah.BaseViewHolderWithBinding

abstract class BaseBindingQuickAdapter<T, VB : ViewBinding>(layoutResId: Int = -1) :
  BaseQuickAdapter<T, BaseViewHolderWithBinding<VB>>(layoutResId) {

  override fun onCreateDefViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolderWithBinding<VB> {
    return BaseViewHolderWithBinding(inflateBindingWithGeneric(parent))
  }

  override fun convert(holder: BaseViewHolderWithBinding<VB>, item: T) {
    convert(holder.binding, item)
  }

  abstract fun convert(binding: VB, item: T)
}