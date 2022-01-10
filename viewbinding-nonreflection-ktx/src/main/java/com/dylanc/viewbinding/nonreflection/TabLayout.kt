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
import androidx.viewbinding.ViewBinding
import com.google.android.material.tabs.TabLayout

fun <VB : ViewBinding> TabLayout.Tab.setCustomView(inflate: (LayoutInflater) -> VB, block: VB.() -> Unit) {
  requireNotNull(parent) { "Tab not attached to a TabLayout" }
  customView = inflate(LayoutInflater.from(parent!!.context)).apply(block).root
}

fun <VB : ViewBinding> TabLayout.updateCustomTab(bind: (View) -> VB, index: Int, block: VB.() -> Unit) =
  getTabAt(index)?.customView?.getBinding(bind)?.also(block)

fun <VB : ViewBinding> TabLayout.doOnCustomTabSelected(
  bind: (View) -> VB,
  onTabUnselected: (VB.(TabLayout.Tab) -> Unit)? = null,
  onTabReselected: (VB.(TabLayout.Tab) -> Unit)? = null,
  onTabSelected: (VB.(TabLayout.Tab) -> Unit)? = null,
) =
  addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
    override fun onTabSelected(tab: TabLayout.Tab) {
      tab.customView?.getBinding(bind)?.let { onTabSelected?.invoke(it, tab) }
    }

    override fun onTabUnselected(tab: TabLayout.Tab) {
      tab.customView?.getBinding(bind)?.let { onTabUnselected?.invoke(it, tab) }
    }

    override fun onTabReselected(tab: TabLayout.Tab) {
      tab.customView?.getBinding(bind)?.let { onTabReselected?.invoke(it, tab) }
    }
  })
