@file:Suppress("unused")

package com.dylanc.viewbinding.brvah

import android.view.View
import androidx.viewbinding.ViewBinding
import com.chad.library.adapter.base.viewholder.BaseViewHolder

@Suppress("UNCHECKED_CAST")
fun <VB : ViewBinding> BaseViewHolder.getBinding(bind: (View) -> VB): VB =
  itemView.getTag(Int.MIN_VALUE) as? VB ?: bind(itemView).also { itemView.setTag(Int.MIN_VALUE, it) }