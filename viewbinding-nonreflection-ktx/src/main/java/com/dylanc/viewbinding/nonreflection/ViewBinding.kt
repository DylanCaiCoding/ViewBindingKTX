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

import android.app.Dialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.ComponentActivity
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.viewbinding.ViewBinding
import com.google.android.material.navigation.NavigationView
import com.google.android.material.tabs.TabLayout
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

/**
 * @author Dylan Cai
 */

fun <VB : ViewBinding> ComponentActivity.binding(inflate: (LayoutInflater) -> VB) = lazy {
  inflate(layoutInflater).also {
    setContentView(it.root)
    if (this is ViewDataBinding) lifecycleOwner = this@binding
  }
}

fun <VB : ViewBinding> Fragment.binding(bind: (View) -> VB) = FragmentBindingDelegate(bind)

fun <VB : ViewBinding> Dialog.binding(inflate: (LayoutInflater) -> VB) = lazy {
  inflate(layoutInflater).also { setContentView(it.root) }
}

fun <VB : ViewBinding> ViewGroup.binding(
  inflate: (LayoutInflater, ViewGroup?, Boolean) -> VB,
  attachToParent: Boolean = true
) = lazy {
  inflate(LayoutInflater.from(context), if (attachToParent) this else null, attachToParent)
}

fun <VB : ViewBinding> TabLayout.Tab.setCustomView(
  inflate: (LayoutInflater) -> VB,
  onBindView: VB.() -> Unit
) {
  customView = inflate(LayoutInflater.from(parent!!.context)).apply(onBindView).root
}

inline fun <reified VB : ViewBinding> TabLayout.Tab.bindCustomView(bind: (View) -> VB, onBindView: VB.() -> Unit) =
  customView?.let { bind(it) }?.run(onBindView)

inline fun <reified VB : ViewBinding> NavigationView.setHeaderView(index: Int = 0, bind: (View) -> VB, onBindView: VB.() -> Unit) =
  getHeaderView(index)?.let { bind(it) }?.run(onBindView)

inline fun Fragment.doOnDestroyView(crossinline block: () -> Unit) =
  viewLifecycleOwner.lifecycle.addObserver(object : LifecycleObserver {
    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroyView() {
      block.invoke()
    }
  })

interface BindingLifecycleOwner {
  fun onDestroyViewBinding(destroyingBinding: ViewBinding)
}

class FragmentBindingDelegate<VB : ViewBinding>(private val bind: (View) -> VB) : ReadOnlyProperty<Fragment, VB> {
  private var binding: VB? = null

  override fun getValue(thisRef: Fragment, property: KProperty<*>): VB {
    if (binding == null) {
      binding = bind(thisRef.requireView()).also {
        if (it is ViewDataBinding) it.lifecycleOwner = thisRef.viewLifecycleOwner
      }
      thisRef.doOnDestroyView {
        if (thisRef is BindingLifecycleOwner) thisRef.onDestroyViewBinding(binding!!)
        binding = null
      }
    }
    return binding!!
  }
}