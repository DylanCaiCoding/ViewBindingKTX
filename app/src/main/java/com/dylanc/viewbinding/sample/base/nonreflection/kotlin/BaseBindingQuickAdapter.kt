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

package com.dylanc.viewbinding.sample.base.nonreflection.kotlin

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder


abstract class BaseBindingQuickAdapter<T, VB : ViewBinding>(
  private val inflate: (LayoutInflater, ViewGroup, Boolean) -> VB,
  layoutResId: Int = -1
) :
  BaseQuickAdapter<T, BaseBindingQuickAdapter.BaseBindingHolder>(layoutResId) {

  override fun onCreateDefViewHolder(parent: ViewGroup, viewType: Int) =
    BaseBindingHolder(inflate(LayoutInflater.from(parent.context), parent, false))

  class BaseBindingHolder(private val binding: ViewBinding) : BaseViewHolder(binding.root) {
    constructor(itemView: View) : this(ViewBinding { itemView })

    @Suppress("UNCHECKED_CAST")
    fun <VB : ViewBinding> getViewBinding() = binding as VB
  }
}