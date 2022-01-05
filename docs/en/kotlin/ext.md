# Use extension functions

## Get started

Add dependency, this library provides two usages of using reflection and not using reflection. If you don't want to use reflection, you can replace it with the corresponding comment code.

```gradle
implementation 'com.github.DylanCaiCoding.ViewBindingKTX:viewbinding-ktx:2.0.0'
// implementation 'com.github.DylanCaiCoding.ViewBindingKTX:viewbinding-nonreflection-ktx:2.0.0'
```

>The following usage methods cannot be used for base classes, if you want to use ViewBinding in base classes, please see [the usage of base class](/en/kotlin/baseclass).

### Activity

```kotlin
class MainActivity : AppCompatActivity() {

  private val binding: ActivityMainBinding by binding()
  // private val binding by binding(ActivityMainBinding::inflate)

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding.tvHelloWorld.text = "Hello Android!"
  }
}
```

### Fragment

```kotlin
class HomeFragment : Fragment(R.layout.fragment_home) {

  private val binding: FragmentHomeBinding by binding()
  // private val binding by binding(FragmentHomeBinding::bind)

  private val childBinding: LayoutChildBinding by binding(Method.INFLATE)
  // private val childBinding by binding { LayoutChildBinding.inflate(layoutInflater) }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    binding.container.addView(childBinding.root)
  }
}
```

### Adapter

Use `BindingViewHolder` in the adapter, no need to create a class extends `RecycleView.ViewHolder`.

```kotlin
class TextAdapter : ListAdapter<String, BindingViewHolder<ItemTextBinding>>(DiffCallback()) {

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
    BindingViewHolder<ItemTextBinding>(parent)
    // BindingViewHolder(parent, ItemFooBinding::inflate)
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

If the project has custom ViewHolder such as `BRVAH`, you can get the binding object with ViewHolder.

```kotlin
class FooAdapter : BaseQuickAdapter<Foo, BaseViewHolder>(R.layout.item_foo) {

  override fun convert(holder: BaseViewHolder, item: Foo) {
    holder.getBinding<ItemFooBinding>()
    // holder.getBinding(ItemFooBinding::bind)
      .tvFoo.text = item.value
  }
}
```

### DialogFragment & Dialog

```kotlin
class LoadingDialogFragment(private val text: String? = null) : DialogFragment() {

  override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
    isCancelable = false
    return LoadingDialog(requireContext())
  }

  inner class LoadingDialog(context: Context) : Dialog(context, R.style.DialogTheme) {
    private val binding: DialogLoadingBinding by binding()
    // private val binding by binding(DialogLoadingBinding::inflate)

    override fun onCreate(savedInstanceState: Bundle?) {
      super.onCreate(savedInstanceState)
      window?.setLayout(MATCH_PARENT, WRAP_CONTENT)
      binding.tvMessage.text = "Wait a minute..."
    }
  }
}
```

### Custom View

```kotlin
class CustomView(context: Context, attrs: AttributeSet? = null) : ConstraintLayout(context, attrs) {

  private val binding = inflate<LayoutCustomViewBinding>()
  // private val binding = inflate(LayoutCustomViewBinding::inflate)

  private val childBinding: LayoutChildBinding by binding(attachToParent = false)
  // private val childBinding by binding(LayoutChildBinding::inflate, attachToParent = false)

  init {
    binding.container.addView(childBinding.root)
  }
}
```

### TabLayout

Use TabLayout to customize the bottom navigation bar.

```kotlin
TabLayoutMediator(tabLayout, viewPager2) { tab, position ->
  tab.setCustomView<LayoutBottomTabBinding> {
  // tab.setCustomView(LayoutBottomTabBinding::bind) {
    tvTitle.setText(titleList[position])
    tvTitle.textSize = if (position == 0) 12f else 10f
    ivIcon.setImageResource(iconList[position])
    ivIcon.contentDescription = tvTitle.text.toString()
  }
}.attach()

tabLayout.doOnCustomTabSelected<LayoutBottomTabBinding>(
// tabLayout.doOnCustomTabSelected(LayoutBottomTabBinding::bind,
  onTabSelected = {
    textView.textSize = 12f
  },
  onTabUnselected = {
    textView.textSize = 10f
  })
```

### NavigationView

```kotlin
navigationView.setHeaderView<LayoutNavHeaderBinding> {
// navigationView.setHeaderView(LayoutNavHeaderBinding::bind) {
  tvNickname.text = nickname
}
```

### PopupWindow

```kotlin
private val popupWindow by popupWindow<LayoutPopupBinding> {
// private val popupWindow by popupWindow(LayoutPopupBinding::inflate) {
  btnLike.setOnClickListener { ... }
}
```
