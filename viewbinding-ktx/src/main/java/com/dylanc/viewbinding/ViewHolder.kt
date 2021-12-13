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

package com.dylanc.viewbinding

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

inline fun <reified VB : ViewBinding> RecyclerView.ViewHolder.withBinding(block: VB.(RecyclerView.ViewHolder) -> Unit) = apply {
  block(getBinding(), this@withBinding)
}

fun <VB : ViewBinding> BindingViewHolder<VB>.withBinding(block: VB.(BindingViewHolder<VB>) -> Unit) = apply {
  block(binding, this@withBinding)
}

inline fun <reified VB : ViewBinding> RecyclerView.ViewHolder.getBinding() = itemView.getBinding<VB>()

inline fun <reified VB : ViewBinding> BindingViewHolder(parent: ViewGroup) =
  BindingViewHolder(inflateBinding<VB>(parent))

class BindingViewHolder<VB : ViewBinding>(val binding: VB) : RecyclerView.ViewHolder(binding.root) {

  constructor(parent: ViewGroup, inflate: (LayoutInflater, ViewGroup, Boolean) -> VB) :
      this(inflate(LayoutInflater.from(parent.context), parent, false))
}

fun <T> OnItemClickListener<T>.onItemClick(holder: RecyclerView.ViewHolder, block: (Int) -> T) =
  onItemClick(block(holder.adapterPosition), holder.adapterPosition)

fun interface OnItemClickListener<T> {
  fun onItemClick(item: T, position: Int)
}
