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