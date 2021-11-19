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


inline fun <reified VB : ViewBinding> ComponentActivity.binding() = lazy {
  inflateBinding<VB>(layoutInflater).also {
    setContentView(it.root)
    if (this is ViewDataBinding) lifecycleOwner = this@binding
  }
}

inline fun <reified VB : ViewBinding> Fragment.binding() = FragmentBindingDelegate<VB> { requireView().bind() }

inline fun <reified VB : ViewBinding> Fragment.binding(method: Method) =
  FragmentBindingDelegate<VB> { if (method == Method.BIND) requireView().bind() else inflateBinding(layoutInflater) }

inline fun <reified VB : ViewBinding> Dialog.binding() = lazy {
  inflateBinding<VB>(layoutInflater).also { setContentView(it.root) }
}

inline fun <reified VB : ViewBinding> ViewGroup.binding(attachToParent: Boolean = false) = lazy {
  inflateBinding<VB>(LayoutInflater.from(context), if (attachToParent) this else null, attachToParent)
}

inline fun <reified VB : ViewBinding> ViewGroup.inflate() =
  inflateBinding<VB>(LayoutInflater.from(context), this, true)

inline fun <reified VB : ViewBinding> TabLayout.Tab.setCustomView(onBindView: VB.() -> Unit) {
  customView = inflateBinding<VB>(LayoutInflater.from(parent!!.context)).apply(onBindView).root
}

inline fun <reified VB : ViewBinding> TabLayout.Tab.bindCustomView(onBindView: VB.() -> Unit) =
  customView?.bind<VB>()?.run(onBindView)

inline fun <reified VB : ViewBinding> TabLayout.Tab.bindCustomView(bind: (View) -> VB, onBindView: VB.() -> Unit) =
  customView?.let { bind(it) }?.run(onBindView)

inline fun <reified VB : ViewBinding> NavigationView.setHeaderView(index: Int = 0, onBindView: VB.() -> Unit) =
  getHeaderView(index)?.bind<VB>()?.run(onBindView)

inline fun <reified VB : ViewBinding> inflateBinding(layoutInflater: LayoutInflater) =
  VB::class.java.getMethod("inflate", LayoutInflater::class.java).invoke(null, layoutInflater) as VB

inline fun <reified VB : ViewBinding> inflateBinding(parent: ViewGroup) =
  inflateBinding<VB>(LayoutInflater.from(parent.context), parent, false)

inline fun <reified VB : ViewBinding> inflateBinding(
  layoutInflater: LayoutInflater, parent: ViewGroup?, attachToParent: Boolean
) =
  VB::class.java.getMethod("inflate", LayoutInflater::class.java, ViewGroup::class.java, Boolean::class.java)
    .invoke(null, layoutInflater, parent, attachToParent) as VB

inline fun <reified VB : ViewBinding> View.bind() =
  VB::class.java.getMethod("bind", View::class.java).invoke(null, this) as VB

inline fun Fragment.doOnDestroyView(crossinline block: () -> Unit) =
  viewLifecycleOwner.lifecycle.addObserver(object : LifecycleObserver {
    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroyView() {
      block.invoke()
    }
  })

enum class Method { BIND, INFLATE }

interface BindingLifecycleOwner {
  fun onDestroyViewBinding(destroyingBinding: ViewBinding)
}

class FragmentBindingDelegate<VB : ViewBinding>(private val block: () -> VB) : ReadOnlyProperty<Fragment, VB> {
  private var binding: VB? = null

  override fun getValue(thisRef: Fragment, property: KProperty<*>): VB {
    if (binding == null) {
      binding = try {
        block().also {
          if (it is ViewDataBinding) it.lifecycleOwner = thisRef.viewLifecycleOwner
        }
      } catch (e: IllegalStateException) {
        throw IllegalStateException("The binding property has been destroyed.")
      }
      thisRef.doOnDestroyView {
        if (thisRef is BindingLifecycleOwner) thisRef.onDestroyViewBinding(binding!!)
        binding = null
      }
    }
    return binding!!
  }
}