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

package com.dylanc.viewbinding.sample

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.fragment.app.Fragment
import com.dylanc.viewbinding.binding
import com.dylanc.viewbinding.sample.databinding.FragmentCustomViewBinding
import com.dylanc.viewbinding.sample.widget.LoadingDialogFragment

class CustomViewFragment : Fragment(R.layout.fragment_custom_view) {

  private val binding: FragmentCustomViewBinding by binding()
  private val loadingDialog by lazy { LoadingDialogFragment() }
  private val handler = Handler(Looper.getMainLooper())

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    binding.customView.setOnClickListener {
      loadingDialog.show(childFragmentManager, "loading")
      handler.postDelayed({
        loadingDialog.dismiss()
      }, 2000)
    }
  }
}