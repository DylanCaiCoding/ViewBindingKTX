@file:Suppress("unused")

package com.dylanc.viewbinding.sample.base.kotlin

import android.content.Context
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.drakeet.multitype.ItemViewDelegate
import com.dylanc.viewbinding.BindingViewHolder
import com.dylanc.viewbinding.newBindingViewHolderWithGeneric

abstract class BindingViewDelegate<T, VB : ViewBinding> : ItemViewDelegate<T, BindingViewHolder<VB>>() {

  override fun onCreateViewHolder(context: Context, parent: ViewGroup) =
    newBindingViewHolderWithGeneric<VB>(parent)
}