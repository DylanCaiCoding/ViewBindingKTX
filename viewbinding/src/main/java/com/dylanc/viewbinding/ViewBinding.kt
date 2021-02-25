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

import android.app.Activity
import android.app.Dialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.viewbinding.ViewBinding
import com.google.android.material.tabs.TabLayout
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty


/**
 * @author Dylan Cai
 */

inline fun <reified VB : ViewBinding> Activity.binding() = lazy {
  inflateBinding<VB>(layoutInflater).apply { setContentView(root) }
}

inline fun <reified VB : ViewBinding> Fragment.binding() =
  FragmentBindingDelegate(VB::class.java)

inline fun <reified VB : ViewBinding> Dialog.binding() = lazy {
  inflateBinding<VB>(layoutInflater).apply { setContentView(root) }
}

inline fun <reified VB : ViewBinding> ViewGroup.binding(attachToParent: Boolean = true): VB =
  inflateBinding(LayoutInflater.from(context), if (attachToParent) this else null, attachToParent)

inline fun <reified VB : ViewBinding> TabLayout.Tab.setCustomView(onBindView: VB.() -> Unit) {
  customView = inflateBinding<VB>(LayoutInflater.from(parent!!.context)).apply(onBindView).root
}

inline fun <reified VB : ViewBinding> inflateBinding(layoutInflater: LayoutInflater) =
  VB::class.java.getMethod("inflate", LayoutInflater::class.java).invoke(null, layoutInflater) as VB

inline fun <reified VB : ViewBinding> inflateBinding(parent: ViewGroup) =
  inflateBinding<VB>(LayoutInflater.from(parent.context), parent, false)

inline fun <reified VB : ViewBinding> inflateBinding(
  layoutInflater: LayoutInflater, parent: ViewGroup?, attachToParent: Boolean
) =
  VB::class.java.getMethod("inflate", LayoutInflater::class.java, ViewGroup::class.java, Boolean::class.java)
    .invoke(null, layoutInflater, parent, attachToParent) as VB

class FragmentBindingDelegate<VB : ViewBinding>(
  private val clazz: Class<VB>
) : ReadOnlyProperty<Fragment, VB> {

  private var isInitialized = false
  private var _binding: VB? = null
  private val binding: VB get() = _binding!!

  @Suppress("UNCHECKED_CAST")
  override fun getValue(thisRef: Fragment, property: KProperty<*>): VB {
    if (!isInitialized) {
      thisRef.viewLifecycleOwner.lifecycle.addObserver(object : LifecycleObserver {
        @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
        fun onDestroyView() {
          _binding = null
        }
      })
      _binding = clazz.getMethod("bind", View::class.java)
        .invoke(null, thisRef.requireView()) as VB
      isInitialized = true
    }
    return binding
  }
}