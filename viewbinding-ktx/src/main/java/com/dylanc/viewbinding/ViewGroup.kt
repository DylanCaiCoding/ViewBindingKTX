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
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import kotlin.LazyThreadSafetyMode.*

inline fun <reified VB : ViewBinding> ViewGroup.inflate() =
  inflateBinding<VB>(LayoutInflater.from(context), this, true)

inline fun <reified VB : ViewBinding> ViewGroup.binding(attachToParent: Boolean = false) = lazy(NONE) {
  inflateBinding<VB>(LayoutInflater.from(context), if (attachToParent) this else null, attachToParent)
}
