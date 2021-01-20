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