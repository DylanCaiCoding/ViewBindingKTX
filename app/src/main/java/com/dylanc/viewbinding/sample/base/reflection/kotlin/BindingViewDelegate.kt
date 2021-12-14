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

package com.dylanc.viewbinding.sample.base.reflection.kotlin

import android.content.Context
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.drakeet.multitype.ItemViewDelegate
import com.dylanc.viewbinding.base.ViewBindingUtil

/**
 * @author Dylan Cai
 */
abstract class BindingViewDelegate<T, VB : ViewBinding> :
  ItemViewDelegate<T, BindingViewDelegate.BindingViewHolder<VB>>() {

  override fun onCreateViewHolder(context: Context, parent: ViewGroup) =
    BindingViewHolder(ViewBindingUtil.inflateWithGeneric<VB>(this, parent))

  class BindingViewHolder<VB : ViewBinding>(val binding: VB) : RecyclerView.ViewHolder(binding.root)
}