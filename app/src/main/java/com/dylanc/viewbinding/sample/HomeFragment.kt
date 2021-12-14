@file:Suppress("unused")

package com.dylanc.viewbinding.sample

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.dylanc.viewbinding.base.simpleListAdapter
import com.dylanc.viewbinding.nonreflection.binding
import com.dylanc.viewbinding.sample.databinding.FragmentHomeBinding
import com.dylanc.viewbinding.sample.databinding.ItemFooBinding
import com.dylanc.viewbinding.sample.item.Foo
import com.dylanc.viewbinding.sample.item.FooDiffCallback

/**
 * @author Dylan Cai
 */
class HomeFragment : Fragment(R.layout.fragment_home) {

  private val binding by binding(FragmentHomeBinding::bind)

  private val list = listOf(Foo("item 1"), Foo("item 2"), Foo("item 3"))

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    binding.recyclerView.adapter = adapter
    adapter.submitList(list)
    adapter.doOnItemClick { item, _ ->
      Toast.makeText(requireContext(), item.value, Toast.LENGTH_SHORT).show()
    }
    adapter.doOnItemLongClick { item, _ ->
      Toast.makeText(requireContext(), "long click ${item.value}", Toast.LENGTH_SHORT).show()
    }
  }

  private val adapter = simpleListAdapter<Foo, ItemFooBinding>(FooDiffCallback()) { item ->
    tvFoo.text = item.value
  }
}