# 使用扩展函数

使用了 Kotlin 委托的特性，不支持 Java 语法，使用 Java 的小伙伴请查看[封装到基类](/zh/baseclass)的文档（以下用法不能用于基类，需要换一种方式）。

## Gradle

添加依赖，提供了使用反射和不使用反射的用法，注意依赖是不一样的。个人推荐使用反射的用法，多一次反射的性能损耗可忽略不计，代码可读性会好很多，与 ViewModel 的用法更加统一。

<!-- tabs:start -->

#### **反射**

```groovy
implementation 'com.github.DylanCaiCoding.ViewBindingKTX:viewbinding-ktx:2.1.0'
```

#### **无反射**

```groovy
implementation 'com.github.DylanCaiCoding.ViewBindingKTX:viewbinding-nonreflection-ktx:2.1.0'
```

<!-- tabs:end -->

## Activity

<!-- tabs:start -->

#### **反射**

```kotlin
class MainActivity : AppCompatActivity() {

  private val binding: ActivityMainBinding by binding()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding.tvHelloWorld.text = "Hello Android!"
  }
}
```

#### **无反射**

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

如果在 `onCreate()` 方法没有使用 binding 对象，需要手动调用 `setContentView(binding.root)` 设置布局。

## Fragment

构造函数需要传入布局 id。

<!-- tabs:start -->

#### **反射**

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

#### **无反射**

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

## Adapter

在 Adapter 里使用 `BindingViewHolder`，无需再写个类继承 `RecycleView.ViewHolder`。

<!-- tabs:start -->

#### **反射**

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

#### **无反射**

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

如果项目中的适配器像 `BRVAH` 那样封装了自定义的 ViewHolder，那么可以直接通过 ViewHolder 获取 binding 对象。

<!-- tabs:start -->

#### **反射**

```kotlin
class FooAdapter : BaseQuickAdapter<Foo, BaseViewHolder>(R.layout.item_foo) {

  override fun convert(holder: BaseViewHolder, item: Foo) {
    holder.getBinding<ItemFooBinding>().tvFoo.text = item.value
  }
}
```

#### **无反射**

```kotlin
class FooAdapter : BaseQuickAdapter<Foo, BaseViewHolder>(R.layout.item_foo) {

  override fun convert(holder: BaseViewHolder, item: Foo) {
    holder.getBinding(ItemFooBinding::bind).tvFoo.text = item.value
  }
}
```

<!-- tabs:end -->

## DialogFragment & Dialog

<!-- tabs:start -->

#### **反射**

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

#### **无反射**

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

## 自定义 View

<!-- tabs:start -->

#### **反射**

```kotlin
class CustomView(context: Context, attrs: AttributeSet? = null) : ConstraintLayout(context, attrs) {

  private val binding = inflate<LayoutCustomViewBinding>()
  private val childBinding: LayoutChildBinding by binding(attachToParent = false)

  init {
    binding.container.addView(childBinding.root)
  }
}
```

#### **无反射**

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

## TabLayout

以下是 TabLayout + ViewPager2 实现自定义底部导航栏的示例。

<!-- tabs:start -->

#### **反射**

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

#### **无反射**

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

## NavigationView

<!-- tabs:start -->

#### **反射**

```kotlin
navigationView.updateHeaderView<LayoutNavHeaderBinding> {
  tvNickname.text = nickname
}
```

#### **无反射**

```kotlin
navigationView.updateHeaderView(LayoutNavHeaderBinding::bind) {
  tvNickname.text = nickname
}
```

<!-- tabs:end -->

## PopupWindow

<!-- tabs:start -->

#### **反射**

```kotlin
private val popupWindow by popupWindow<LayoutPopupBinding> {
  btnLike.setOnClickListener { ... }
}
```

#### **无反射**

```kotlin
private val popupWindow by popupWindow(LayoutPopupBinding::inflate) {
  btnLike.setOnClickListener { ... }
}
```

<!-- tabs:end -->