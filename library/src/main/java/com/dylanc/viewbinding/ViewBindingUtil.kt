@file:Suppress("unused")
@file:JvmName("ViewBindingUtil")

package com.dylanc.viewbinding

import android.app.Activity
import android.app.Dialog
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import java.lang.reflect.ParameterizedType


/**
 * @author Dylan Cai
 */

inline fun <reified VB : ViewBinding> Fragment.lazyInflate(): Lazy<VB> = lazy { bindingOf(layoutInflater) }

inline fun <reified VB : ViewBinding> Activity.lazyInflate(): Lazy<VB> = lazy { bindingOf(layoutInflater) }

inline fun <reified VB : ViewBinding> Dialog.lazyInflate(): Lazy<VB> = lazy { bindingOf(layoutInflater) }

@JvmName("inflate")
@Suppress("UNCHECKED_CAST")
fun <VB : ViewBinding> bindingOf(clazz: Class<VB>, layoutInflater: LayoutInflater) =
  clazz.getMethod("inflate", LayoutInflater::class.java).invoke(null, layoutInflater) as VB

inline fun <reified VB : ViewBinding> bindingOf(layoutInflater: LayoutInflater) =
  bindingOf(VB::class.java, layoutInflater)

@JvmName("inflate")
@Suppress("UNCHECKED_CAST")
fun <VB : ViewBinding> bindingOf(clazz: Class<VB>, layoutInflater: LayoutInflater, parent: ViewGroup, attachToParent: Boolean) =
  clazz.getMethod("inflate", LayoutInflater::class.java, ViewGroup::class.java, Boolean::class.java)
    .invoke(null, layoutInflater, parent, attachToParent) as VB

inline fun <reified VB : ViewBinding> bindingOf(layoutInflater: LayoutInflater, parent: ViewGroup, attachToParent: Boolean) =
  bindingOf(VB::class.java, layoutInflater, parent, attachToParent)

@JvmName("inflateWithGeneric")
fun <VB : ViewBinding> Any.genericBindingOf(layoutInflater: LayoutInflater): VB =
  bindingOf(findBindingGenericClass(), layoutInflater)

@JvmName("inflateWithGeneric")
fun <VB : ViewBinding> Any.genericBindingOf(layoutInflater: LayoutInflater, parent: ViewGroup, attachToParent: Boolean): VB =
  bindingOf(findBindingGenericClass(), layoutInflater, parent, attachToParent)

@Suppress("UNCHECKED_CAST")
private fun <VB : ViewBinding> Any.findBindingGenericClass(): Class<VB> {
  val type = javaClass.genericSuperclass
  if (type is ParameterizedType) {
    type.actualTypeArguments.forEach {
      try {
        return it as Class<VB>
      } catch (e: Exception) {
      }
    }
  }
  throw IllegalArgumentException("There is no generic of ViewBinding.")
}
