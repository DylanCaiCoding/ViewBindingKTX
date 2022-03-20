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
import kotlin.LazyThreadSafetyMode.*

inline fun <reified VB : ViewBinding> simpleIntListAdapter(crossinline onBindViewHolder: VB.(Int) -> Unit) =
  simpleListAdapter(IntDiffCallback(), onBindViewHolder)

inline fun <reified VB : ViewBinding> simpleLongListAdapter(crossinline onBindViewHolder: VB.(Long) -> Unit) =
  simpleListAdapter(LongDiffCallback(), onBindViewHolder)

inline fun <reified VB : ViewBinding> simpleBooleanListAdapter(crossinline onBindViewHolder: VB.(Boolean) -> Unit) =
  simpleListAdapter(BooleanDiffCallback(), onBindViewHolder)

inline fun <reified VB : ViewBinding> simpleFloatListAdapter(crossinline onBindViewHolder: VB.(Float) -> Unit) =
  simpleListAdapter(FloatDiffCallback(), onBindViewHolder)

inline fun <reified VB : ViewBinding> simpleDoubleListAdapter(crossinline onBindViewHolder: VB.(Double) -> Unit) =
  simpleListAdapter(DoubleDiffCallback(), onBindViewHolder)

inline fun <reified VB : ViewBinding> simpleStringListAdapter(crossinline onBindViewHolder: VB.(String) -> Unit) =
  simpleListAdapter(StringDiffCallback(), onBindViewHolder)

inline fun <T, reified VB : ViewBinding> simpleListAdapter(
  diffCallback: DiffUtil.ItemCallback<T>,
  crossinline onBindViewHolder: VB.(T) -> Unit
) =
  lazy<SimpleListAdapter<T, VB>>(NONE) {
    object : SimpleListAdapter<T, VB>(diffCallback) {
      override fun onBindViewHolder(binding: VB, item: T, position: Int) =
        onBindViewHolder(binding, item)
    }
  }

abstract class SimpleListAdapter<T, VB : ViewBinding>(
  diffCallback: DiffUtil.ItemCallback<T>
) : ListAdapter<T, SimpleListAdapter.BindingViewHolder<VB>>(diffCallback) {

  private var onItemClickListener: OnItemClickListener<T>? = null
  private var onItemLongClickListener: OnItemClickListener<T>? = null

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BindingViewHolder<VB> =
    BindingViewHolder(ViewBindingUtil.inflateWithGeneric<VB>(this, parent)).apply {
      itemView.setOnClickListener {
        onItemClickListener?.onItemClick(getItem(adapterPosition), adapterPosition)
      }
      itemView.setOnLongClickListener {
        onItemLongClickListener?.onItemClick(getItem(adapterPosition), adapterPosition)
        onItemLongClickListener != null
      }
    }

  override fun onBindViewHolder(holder: BindingViewHolder<VB>, position: Int) {
    onBindViewHolder(holder.binding, getItem(position), position)
  }

  fun doOnItemClick(block: OnItemClickListener<T>) {
    onItemClickListener = block
  }

  fun doOnItemLongClick(block: OnItemClickListener<T>) {
    onItemLongClickListener = block
  }

  abstract fun onBindViewHolder(binding: VB, item: T, position: Int)

  class BindingViewHolder<VB : ViewBinding>(val binding: VB) : RecyclerView.ViewHolder(binding.root)

  fun interface OnItemClickListener<T> {
    fun onItemClick(item: T, position: Int)
  }
}

abstract class SimpleIntListAdapter<VB : ViewBinding> : SimpleListAdapter<Int, VB>(IntDiffCallback())

abstract class SimpleLongListAdapter<VB : ViewBinding> : SimpleListAdapter<Long, VB>(LongDiffCallback())

abstract class SimpleBooleanListAdapter<VB : ViewBinding> : SimpleListAdapter<Boolean, VB>(BooleanDiffCallback())

abstract class SimpleFloatListAdapter<VB : ViewBinding> : SimpleListAdapter<Float, VB>(FloatDiffCallback())

abstract class SimpleDoubleListAdapter<VB : ViewBinding> : SimpleListAdapter<Double, VB>(DoubleDiffCallback())

abstract class SimpleStringListAdapter<VB : ViewBinding> : SimpleListAdapter<String, VB>(StringDiffCallback())

class IntDiffCallback : DiffUtil.ItemCallback<Int>() {
  override fun areItemsTheSame(oldItem: Int, newItem: Int) = oldItem == newItem
  override fun areContentsTheSame(oldItem: Int, newItem: Int) = oldItem == newItem
}

class LongDiffCallback : DiffUtil.ItemCallback<Long>() {
  override fun areItemsTheSame(oldItem: Long, newItem: Long) = oldItem == newItem
  override fun areContentsTheSame(oldItem: Long, newItem: Long) = oldItem == newItem
}

class BooleanDiffCallback : DiffUtil.ItemCallback<Boolean>() {
  override fun areItemsTheSame(oldItem: Boolean, newItem: Boolean) = oldItem == newItem
  override fun areContentsTheSame(oldItem: Boolean, newItem: Boolean) = oldItem == newItem
}

class FloatDiffCallback : DiffUtil.ItemCallback<Float>() {
  override fun areItemsTheSame(oldItem: Float, newItem: Float) = oldItem == newItem
  override fun areContentsTheSame(oldItem: Float, newItem: Float) = oldItem == newItem
}

class DoubleDiffCallback : DiffUtil.ItemCallback<Double>() {
  override fun areItemsTheSame(oldItem: Double, newItem: Double) = oldItem == newItem
  override fun areContentsTheSame(oldItem: Double, newItem: Double) = oldItem == newItem
}

class StringDiffCallback : DiffUtil.ItemCallback<String>() {
  override fun areItemsTheSame(oldItem: String, newItem: String) = oldItem == newItem
  override fun areContentsTheSame(oldItem: String, newItem: String) = oldItem == newItem
}
