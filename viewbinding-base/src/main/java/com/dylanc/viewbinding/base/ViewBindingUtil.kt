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

@file:Suppress("UNCHECKED_CAST", "unused")

package com.dylanc.viewbinding.base

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.ComponentActivity
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import java.lang.reflect.InvocationTargetException
import java.lang.reflect.ParameterizedType

object ViewBindingUtil {

  @JvmStatic
  fun <VB : ViewBinding> inflateWithGeneric(genericOwner: Any, layoutInflater: LayoutInflater): VB =
    withGenericBindingClass<VB>(genericOwner) { clazz ->
      clazz.getMethod("inflate", LayoutInflater::class.java).invoke(null, layoutInflater) as VB
    }.withLifecycleOwner(genericOwner)

  @JvmStatic
  fun <VB : ViewBinding> inflateWithGeneric(genericOwner: Any, parent: ViewGroup): VB =
    inflateWithGeneric(genericOwner, LayoutInflater.from(parent.context), parent, false)

  @JvmStatic
  fun <VB : ViewBinding> inflateWithGeneric(genericOwner: Any, layoutInflater: LayoutInflater, parent: ViewGroup?, attachToParent: Boolean): VB =
    withGenericBindingClass<VB>(genericOwner) { clazz ->
      clazz.getMethod("inflate", LayoutInflater::class.java, ViewGroup::class.java, Boolean::class.java)
        .invoke(null, layoutInflater, parent, attachToParent) as VB
    }.withLifecycleOwner(genericOwner)

  @JvmStatic
  @Suppress("DEPRECATION")
  fun <VB : ViewBinding> getBindingWithGeneric(genericOwner: Any, view: View): VB =
    view.getTag(R.id.tag_view_binding) as? VB ?: bindWithGeneric<VB>(genericOwner, view)
      .also { view.setTag(R.id.tag_view_binding, it) }

  @JvmStatic
  @Deprecated(
    "Use ViewBindingUtil.getBindingWithGeneric<VB>(genericOwner, view) instead.",
    ReplaceWith("ViewBindingUtil.getBindingWithGeneric<VB>(genericOwner, view)")
  )
  fun <VB : ViewBinding> bindWithGeneric(genericOwner: Any, view: View): VB =
    withGenericBindingClass<VB>(genericOwner) { clazz ->
      clazz.getMethod("bind", View::class.java).invoke(null, view) as VB
    }.withLifecycleOwner(genericOwner)

  private fun <VB : ViewBinding> VB.withLifecycleOwner(genericOwner: Any) = apply {
    if (this is ViewDataBinding && genericOwner is ComponentActivity) {
      lifecycleOwner = genericOwner
    } else if (this is ViewDataBinding && genericOwner is Fragment) {
      lifecycleOwner = genericOwner.viewLifecycleOwner
    }
  }

  private fun <VB : ViewBinding> withGenericBindingClass(genericOwner: Any, block: (Class<VB>) -> VB): VB {
    var genericSuperclass = genericOwner.javaClass.genericSuperclass
    var superclass = genericOwner.javaClass.superclass
    while (superclass != null) {
      if (genericSuperclass is ParameterizedType) {
        genericSuperclass.actualTypeArguments.forEach {
          try {
            return block.invoke(it as Class<VB>)
          } catch (e: NoSuchMethodException) {
          } catch (e: ClassCastException) {
          } catch (e: InvocationTargetException) {
            var tagException: Throwable? = e
            while (tagException is InvocationTargetException) {
              tagException = e.cause
            }
            throw tagException ?: IllegalArgumentException("ViewBinding generic was found, but creation failed.")
          }
        }
      }
      genericSuperclass = superclass.genericSuperclass
      superclass = superclass.superclass
    }
    throw IllegalArgumentException("There is no generic of ViewBinding.")
  }
}
