# Use extension functions

## Get started

Add dependency, this library provides two usages of using reflection and not using reflection. If you don't want to use reflection, you can replace it with the corresponding comment code.

<!-- tabs:start -->

#### **Reflection**

```groovy
implementation 'com.github.DylanCaiCoding.ViewBindingKTX:viewbinding-ktx:2.1.0'
```

#### **Nonreflection**

```groovy
implementation 'com.github.DylanCaiCoding.ViewBindingKTX:viewbinding-nonreflection-ktx:2.1.0'
```

<!-- tabs:end -->

>The following usage methods cannot be used for base classes, if you want to use ViewBinding in base classes, please see [the usage of base class](/en/kotlin/baseclass).

### Activity

<!-- tabs:start -->

#### **Reflection**

```kotlin
class MainActivity : AppCompatActivity() {

  private val binding: ActivityMainBinding by binding()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding.tvHelloWorld.text = "Hello Android!"
  }
}
```

#### **Nonreflection**

```kotlin
class MainActivity : AppCompatActivity() {

  private val binding by binding(ActivityMainBinding::inflate)

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding.tvHelloWorld.text = "Hello Android!"
  }
}
```

<!-- tabs:end -->

If the binding object is not used in the `onCreate()` method, you need to call `setContentView(binding.root)` to set the layout.

### Fragment

The constructor needs the layout ID.

<!-- tabs:start -->

#### **Reflection**

```kotlin
class HomeFragment : Fragment(R.layout.fragment_home) {

  private val binding: FragmentHomeBinding by binding()
  private val childBinding: LayoutChildBinding by binding(Method.INFLATE)

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    binding.container.addView(childBinding.root)
  }
}
```

#### **Nonreflection**

```kotlin
class HomeFragment : Fragment(R.layout.fragment_home) {

  private val binding by binding(FragmentHomeBinding::bind)
  private val childBinding by binding { LayoutChildBinding.inflate(layoutInflater) }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    binding.container.addView(childBinding.root)
  }
}
```

<!-- tabs:end -->

### Adapter

Use `BindingViewHolder` in the adapter, no need to create a class extends `RecycleView.ViewHolder`.

<!-- tabs:start -->

#### **Reflection**

```kotlin
class TextAdapter : ListAdapter<String, BindingViewHolder<ItemTextBinding>>(DiffCallback()) {

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
    BindingViewHolder<ItemTextBinding>(parent)
      .withBinding {
        root.setOnClickListener { ... }
        tvText.setOnClickListener { ... }
      }

  override fun onBindViewHolder(holder: BindingViewHolder<ItemTextBinding>, position: Int) {
    holder.binding.tvText.text = currentList[position]
  }

  class DiffCallback : DiffUtil.ItemCallback<String>() {
    override fun areItemsTheSame(oldItem: String, newItem: String) = oldItem == newItem
    override fun areContentsTheSame(oldItem: String, newItem: String) = oldItem == newItem
  }
}
```

#### **Nonreflection**

```kotlin
class TextAdapter : ListAdapter<String, BindingViewHolder<ItemTextBinding>>(DiffCallback()) {

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
    BindingViewHolder(parent, ItemFooBinding::inflate)
      .withBinding {
        root.setOnClickListener { ... }
        tvText.setOnClickListener { ... }
      }

  override fun onBindViewHolder(holder: BindingViewHolder<ItemTextBinding>, position: Int) {
    holder.binding.tvText.text = currentList[position]
  }

  class DiffCallback : DiffUtil.ItemCallback<String>() {
    override fun areItemsTheSame(oldItem: String, newItem: String) = oldItem == newItem
    override fun areContentsTheSame(oldItem: String, newItem: String) = oldItem == newItem
  }
}
```

<!-- tabs:end -->


If the project has custom ViewHolder such as `BRVAH`, you can get the binding object with ViewHolder.

<!-- tabs:start -->

#### **Reflection**

```kotlin
class FooAdapter : BaseQuickAdapter<Foo, BaseViewHolder>(R.layout.item_foo) {

  override fun convert(holder: BaseViewHolder, item: Foo) {
    holder.getBinding<ItemFooBinding>().tvFoo.text = item.value
  }
}
```

#### **Nonreflection**

```kotlin
class FooAdapter : BaseQuickAdapter<Foo, BaseViewHolder>(R.layout.item_foo) {

  override fun convert(holder: BaseViewHolder, item: Foo) {
    holder.getBinding(ItemFooBinding::bind).tvFoo.text = item.value
  }
}
```

<!-- tabs:end -->

### DialogFragment & Dialog

<!-- tabs:start -->

#### **Reflection**

```kotlin
class LoadingDialogFragment(private val text: String? = null) : DialogFragment() {

  override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
    isCancelable = false
    return LoadingDialog(requireContext())
  }

  inner class LoadingDialog(context: Context) : Dialog(context, R.style.DialogTheme) {
    private val binding: DialogLoadingBinding by binding()

    override fun onCreate(savedInstanceState: Bundle?) {
      super.onCreate(savedInstanceState)
      window?.setLayout(MATCH_PARENT, WRAP_CONTENT)
      binding.tvMessage.text = "Wait a minute..."
    }
  }
}
```

#### **Nonreflection**

```kotlin
class LoadingDialogFragment(private val text: String? = null) : DialogFragment() {

  override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
    isCancelable = false
    return LoadingDialog(requireContext())
  }

  inner class LoadingDialog(context: Context) : Dialog(context, R.style.DialogTheme) {
    private val binding by binding(DialogLoadingBinding::inflate)

    override fun onCreate(savedInstanceState: Bundle?) {
      super.onCreate(savedInstanceState)
      window?.setLayout(MATCH_PARENT, WRAP_CONTENT)
      binding.tvMessage.text = "Wait a minute..."
    }
  }
}
```

<!-- tabs:end -->

### Custom View

<!-- tabs:start -->

#### **Reflection**

```kotlin
class CustomView(context: Context, attrs: AttributeSet? = null) : ConstraintLayout(context, attrs) {

  private val binding = inflate<LayoutCustomViewBinding>()
  private val childBinding: LayoutChildBinding by binding(attachToParent = false)

  init {
    binding.container.addView(childBinding.root)
  }
}
```

#### **Nonreflection**

```kotlin
class CustomView(context: Context, attrs: AttributeSet? = null) : ConstraintLayout(context, attrs) {

  private val binding = inflate(LayoutCustomViewBinding::inflate)
  private val childBinding by binding(LayoutChildBinding::inflate, attachToParent = false)

  init {
    binding.container.addView(childBinding.root)
  }
}
```

<!-- tabs:end -->

### TabLayout

Use TabLayout to customize the bottom navigation bar.

<!-- tabs:start -->

#### **Reflection**

```kotlin
TabLayoutMediator(tabLayout, viewPager2) { tab, position ->
  tab.setCustomView<LayoutBottomTabBinding> {
    tvTitle.setText(titleList[position])
    tvTitle.textSize = if (position == 0) 12f else 10f
    ivIcon.setImageResource(iconList[position])
    ivIcon.contentDescription = tvTitle.text.toString()
  }
}.attach()

tabLayout.doOnCustomTabSelected<LayoutBottomTabBinding>(
  onTabSelected = {
    tvTitle.textSize = 12f
  },
  onTabUnselected = {
    tvTitle.textSize = 10f
  })
```

#### **Nonreflection**

```kotlin
TabLayoutMediator(tabLayout, viewPager2) { tab, position ->
  tab.setCustomView(LayoutBottomTabBinding::bind) {
    tvTitle.setText(titleList[position])
    tvTitle.textSize = if (position == 0) 12f else 10f
    ivIcon.setImageResource(iconList[position])
    ivIcon.contentDescription = tvTitle.text.toString()
  }
}.attach()

tabLayout.doOnCustomTabSelected(LayoutBottomTabBinding::bind,
  onTabSelected = {
    tvTitle.textSize = 12f
  },
  onTabUnselected = {
    tvTitle.textSize = 10f
  })
```

<!-- tabs:end -->

### NavigationView

<!-- tabs:start -->

#### **Reflection**

```kotlin
navigationView.updateHeaderView<LayoutNavHeaderBinding> {
  tvNickname.text = nickname
}
```

#### **Nonreflection**

```kotlin
navigationView.updateHeaderView(LayoutNavHeaderBinding::bind) {
  tvNickname.text = nickname
}
```

<!-- tabs:end -->

### PopupWindow

<!-- tabs:start -->

#### **Reflection**

```kotlin
private val popupWindow by popupWindow<LayoutPopupBinding> {
  btnLike.setOnClickListener { ... }
}
```

#### **Nonreflection**

```kotlin
private val popupWindow by popupWindow(LayoutPopupBinding::inflate) {
  btnLike.setOnClickListener { ... }
}
```

<!-- tabs:end -->