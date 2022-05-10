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

import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.viewbinding.ViewBinding
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

enum class Method { BIND, INFLATE }

inline fun <reified VB : ViewBinding> Fragment.binding() =
  FragmentBindingProperty(VB::class.java)

inline fun <reified VB : ViewBinding> Fragment.binding(method: Method) =
  if (method == Method.BIND) FragmentBindingProperty(VB::class.java) else FragmentInflateBindingProperty(VB::class.java)

class FragmentBindingProperty<VB : ViewBinding>(private val clazz: Class<VB>) : ReadOnlyProperty<Fragment, VB> {

  override fun getValue(thisRef: Fragment, property: KProperty<*>): VB =
    requireNotNull(thisRef.view) { "The constructor missing layout id or the property of ${property.name} has been destroyed." }
      .getBinding(clazz).also { binding ->
        if (binding is ViewDataBinding) binding.lifecycleOwner = thisRef.viewLifecycleOwner
      }
}

class FragmentInflateBindingProperty<VB : ViewBinding>(private val clazz: Class<VB>) : ReadOnlyProperty<Fragment, VB> {
  private var binding: VB? = null
  private val handler by lazy { Handler(Looper.getMainLooper()) }

  override fun getValue(thisRef: Fragment, property: KProperty<*>): VB {
    if (binding == null) {
      try {
        @Suppress("UNCHECKED_CAST")
        binding = (clazz.getMethod("inflate", LayoutInflater::class.java).invoke(null, thisRef.layoutInflater) as VB)
          .also { binding -> if (binding is ViewDataBinding) binding.lifecycleOwner = thisRef.viewLifecycleOwner }
      } catch (e: IllegalStateException) {
        throw IllegalStateException("The property of ${property.name} has been destroyed.")
      }
      thisRef.viewLifecycleOwner.lifecycle.addObserver(object : DefaultLifecycleObserver {
        override fun onDestroy(owner: LifecycleOwner) {
          handler.post { binding = null }
        }
      })
    }
    return binding!!
  }
}
