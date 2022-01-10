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
import androidx.viewbinding.ViewBinding
import com.google.android.material.tabs.TabLayout

inline fun <reified VB : ViewBinding> TabLayout.Tab.setCustomView(block: VB.() -> Unit) {
  requireNotNull(parent) { "Tab not attached to a TabLayout" }
  inflateBinding<VB>(LayoutInflater.from(parent!!.context)).apply(block).let { binding ->
    customView = binding.root
    customView?.tag = binding
  }
}

inline fun <reified VB : ViewBinding> TabLayout.updateCustomTab(index: Int, block: VB.() -> Unit) =
  getTabAt(index)?.customView?.getBinding<VB>()?.also(block)

inline fun <reified VB : ViewBinding> TabLayout.doOnCustomTabSelected(
  crossinline onTabUnselected: VB.(TabLayout.Tab) -> Unit = {},
  crossinline onTabReselected: VB.(TabLayout.Tab) -> Unit = {},
  crossinline onTabSelected: VB.(TabLayout.Tab) -> Unit = {},
) =
  addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
    override fun onTabSelected(tab: TabLayout.Tab) {
      tab.customView?.getBinding<VB>()?.onTabSelected(tab)
    }

    override fun onTabUnselected(tab: TabLayout.Tab) {
      tab.customView?.getBinding<VB>()?.onTabUnselected(tab)
    }

    override fun onTabReselected(tab: TabLayout.Tab) {
      tab.customView?.getBinding<VB>()?.onTabReselected(tab)
    }
  })
