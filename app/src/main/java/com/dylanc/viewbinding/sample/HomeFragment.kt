@file:Suppress("unused")

package com.dylanc.viewbinding.sample

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.dylanc.viewbinding.base.simpleStringListAdapter
import com.dylanc.viewbinding.nonreflection.binding
import com.dylanc.viewbinding.sample.databinding.FragmentHomeBinding
import com.dylanc.viewbinding.sample.databinding.ItemFooBinding

/**
 * @author Dylan Cai
 */
class HomeFragment : Fragment(R.layout.fragment_home) {

  private val binding by binding(FragmentHomeBinding::bind)

  private val list = listOf("item 1", "item 2", "item 3")

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    binding.recyclerView.adapter = adapter
    adapter.submitList(list)
    adapter.doOnItemClick { item, _ ->
      Toast.makeText(requireContext(), item, Toast.LENGTH_SHORT).show()
    }
    adapter.doOnItemLongClick { item, _ ->
      Toast.makeText(requireContext(), "long click $item", Toast.LENGTH_SHORT).show()
    }
  }

  private val adapter = simpleStringListAdapter<ItemFooBinding> {
    textView.text = it
  }
}