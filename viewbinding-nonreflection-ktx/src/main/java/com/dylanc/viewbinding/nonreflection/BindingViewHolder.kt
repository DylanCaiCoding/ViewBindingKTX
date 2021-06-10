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

package com.dylanc.viewbinding.nonreflection

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

/**
 * @author Dylan Cai
 */

class BindingViewHolder<VB : ViewBinding>(val binding: VB) : RecyclerView.ViewHolder(binding.root) {
  constructor(parent: ViewGroup, inflate: (LayoutInflater, ViewGroup, Boolean) -> VB) :
      this(inflate(LayoutInflater.from(parent.context), parent, false))
}

inline fun <VB : ViewBinding> BindingViewHolder<VB>.onClick(view: View, crossinline action: VB.(Int) -> Unit) =
  apply { view.setOnClickListener { binding.action(adapterPosition) } }

inline fun <VB : ViewBinding, T> BindingViewHolder<VB>.onClick(
  view: View,
  listener: OnItemClickListener<T>,
  crossinline block: VB.(Int) -> T
) =
  onClick(view) { listener.onItemClick(block(it), it) }

inline fun <VB : ViewBinding> BindingViewHolder<VB>.onItemClick(crossinline action: VB.(Int) -> Unit) =
  onClick(itemView, action)

inline fun <VB : ViewBinding, T> BindingViewHolder<VB>.onItemClick(listener: OnItemClickListener<T>, crossinline block: VB.(Int) -> T) =
  onItemClick { listener.onItemClick(block(it), it) }

fun interface OnItemClickListener<T> {
  fun onItemClick(item: T, position: Int)
}

inline fun <T, reified VB : ViewBinding> ListAdapter(
  diffCallback: DiffUtil.ItemCallback<T>,
  noinline inflate: (LayoutInflater, ViewGroup, Boolean) -> VB,
  crossinline onBindViewHolder: BindingViewHolder<VB>.(T) -> Unit
) = object : ListAdapter<T, BindingViewHolder<VB>>(diffCallback) {
  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
    BindingViewHolder(parent, inflate)

  override fun onBindViewHolder(holder: BindingViewHolder<VB>, position: Int) {
    onBindViewHolder(holder, currentList[position])
  }
}