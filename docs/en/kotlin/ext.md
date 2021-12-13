# Use extension functions

## Get started

Add dependency, this library provides two usages of using reflection and not using reflection. If you don't want to use reflection, you can replace it with the corresponding comment code.

```gradle
implementation 'com.github.DylanCaiCoding.ViewBindingKTX:viewbinding-ktx:1.2.6'
// implementation 'com.github.DylanCaiCoding.ViewBindingKTX:viewbinding-nonreflection-ktx:1.2.6'
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

If you need to do some releases before destroying the binding object, implement the `BindingLifecycleOwner` interface and override the `onDestroyViewBinding()` method.

```kotlin
class HomeFragment : Fragment(R.layout.fragment_home), BindingLifecycleOwner {

  private val binding: FragmentHomeBinding by binding()
  // private val binding by binding(FragmentHomeBinding::bind)

  private val childBinding: LayoutChildBinding by binding(Method.INFLATE)
  // private val childBinding by binding { LayoutChildBinding.inflate(layoutInflater) }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    binding.container.addView(childBinding.root)
  }

  override fun onDestroyViewBinding(destroyingBinding: ViewBinding) {
    if(destroyingBinding is FragmentHomeBinding) {
      // Multiple binding objects need to determine the type
    }
  }
}
```

### Adapter

Use `BindingViewHolder` in the adapter, no need to create a class extends `RecycleView.ViewHolder`.

```kotlin
class TextAdapter : ListAdapter<String, BindingViewHolder<ItemTextBinding>>(DiffCallback()) {

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
    BindingViewHolder<ItemFooBinding>(parent)
    // BindingViewHolder(parent, ItemFooBinding::inflate)

  override fun onBindViewHolder(holder: BindingViewHolder<ItemTextBinding>, position: Int) {
    holder.binding.apply {
      tvText.text = currentList[position]
    }
  }

  class DiffCallback : DiffUtil.ItemCallback<String>() {
    override fun areItemsTheSame(oldItem: String, newItem: String) = oldItem == newItem
    override fun areContentsTheSame(oldItem: String, newItem: String) = oldItem == newItem
  }
}
```

If the project has custom ViewHolder, please refer to [the usage of BaseRecyclerViewAdapterHelper](/en/kotlin/brvah).

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
    ivIcon.setImageResource(iconList[position])
  }
}.attach()

tabLayout.addOnTabSelectedListener(object :TabLayout.OnTabSelectedListener{
  override fun onTabSelected(tab: TabLayout.Tab) {
    tab.bindCustomView<LayoutBottomTabBinding> { 
    // tab.bindCustomView(LayoutBottomTabBinding::bind) {
      
    }
  }

  override fun onTabUnselected(tab: TabLayout.Tab) {
  }

  override fun onTabReselected(tab: TabLayout.Tab) { 
  }
})
```

### NavigationView

```kotlin
navigationView.setHeaderView<LayoutNavHeaderBinding> {
// navigationView.setHeaderView(LayoutNavHeaderBinding::bind) {
  tvNickname.text = nickname
}
```