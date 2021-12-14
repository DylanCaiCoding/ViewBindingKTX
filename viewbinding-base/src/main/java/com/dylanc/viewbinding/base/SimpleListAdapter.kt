/*
 * Copyright (c) 2020. Dylan Cai
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

@file:Suppress("unused")

package com.dylanc.viewbinding.base

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

inline fun <T, VB : ViewBinding> simpleListAdapter(
  diffCallback: DiffUtil.ItemCallback<T>,
  crossinline onBindViewHolder: VB.(T) -> Unit
) = object : SimpleListAdapter<T, VB>(diffCallback) {

  override fun onBindViewHolder(binding: VB, item: T, position: Int) {
    onBindViewHolder(binding, item)
  }
}

abstract class SimpleListAdapter<T, VB : ViewBinding>(
  diffCallback: DiffUtil.ItemCallback<T>
) : ListAdapter<T, SimpleListAdapter.BindingViewHolder<VB>>(diffCallback) {

  private var onItemClickListener: ((T, Int) -> Unit)? = null
  private var onItemLongClickListener: ((T, Int) -> Unit)? = null

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BindingViewHolder<VB> =
    BindingViewHolder(ViewBindingUtil.inflateWithGeneric<VB>(this, parent)).apply {
      itemView.setOnClickListener {
        onItemClickListener?.invoke(getItem(adapterPosition), adapterPosition)
      }
      itemView.setOnLongClickListener {
        onItemLongClickListener?.invoke(getItem(adapterPosition), adapterPosition)
        onItemLongClickListener != null
      }
    }

  override fun onBindViewHolder(holder: BindingViewHolder<VB>, position: Int) {
    onBindViewHolder(holder.binding, getItem(position), position)
  }

  fun doOnItemClick(block: (T, Int) -> Unit) {
    onItemClickListener = block
  }

  fun doOnItemLongClick(block: (T, Int) -> Unit) {
    onItemLongClickListener = block
  }

  abstract fun onBindViewHolder(binding: VB, item: T, position: Int)

  class BindingViewHolder<VB : ViewBinding>(val binding: VB) : RecyclerView.ViewHolder(binding.root)
}
