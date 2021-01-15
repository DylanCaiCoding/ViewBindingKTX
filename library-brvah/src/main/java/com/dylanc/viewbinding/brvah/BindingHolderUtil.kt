@file:JvmName("BindingHolderUtil")
@file:Suppress("unused")

package com.dylanc.viewbinding.brvah

import android.view.View
import androidx.viewbinding.ViewBinding
import com.chad.library.adapter.base.viewholder.BaseViewHolder

@JvmName("bind")
fun <VB : ViewBinding> BaseViewHolder.withBinding(bind: (View) -> VB): BaseViewHolder =
  BaseViewHolderWithBinding(bind(itemView))

@JvmName("getBinding")
@Suppress("UNCHECKED_CAST")
fun <VB : ViewBinding> BaseViewHolder.getViewBinding(): VB {
  if (this is BaseViewHolderWithBinding<*>) {
    return binding as VB
  } else {
    throw IllegalStateException("The binding could not be found.")
  }
}

class BaseViewHolderWithBinding<VB : ViewBinding>(val binding: VB) : BaseViewHolder(binding.root)