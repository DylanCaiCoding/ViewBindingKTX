# 使用扩展函数

## 开始使用

添加依赖，本库提供了使用反射和不使用反射的用法，如果希望不使用反射，可换成对应的注释代码。

```gradle
implementation 'com.github.DylanCaiCoding.ViewBindingKTX:viewbinding-ktx:2.0.2'
// implementation 'com.github.DylanCaiCoding.ViewBindingKTX:viewbinding-nonreflection-ktx:2.0.2'
```

个人推荐使用反射的用法，多一次反射的性能损耗可忽略不计，代码可读性会好很多，与 ViewModel 的用法更加统一。

>下面的使用方式不能用于基类，想封装到基类请移步到 [封装到基类](/cn/kotlin/baseclass) 的用法

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

如果在 `onCreate()` 方法没有使用 binding 对象，需要手动调用 `setContentView(binding.root)` 设置布局。

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

在 Adapter 里使用 `BindingViewHolder`，无需再写个类继承 `RecycleView.ViewHolder`。

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

如果项目中的适配器像 `BRVAH` 那样封装了自定义的 ViewHolder，那么可以直接通过 ViewHolder 获取 binding 对象。

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

### 自定义 View

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

以下是 TabLayout + ViewPager2 实现自定义底部导航栏的示例。

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
    tvTitle.textSize = 12f
  },
  onTabUnselected = {
    tvTitle.textSize = 10f
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