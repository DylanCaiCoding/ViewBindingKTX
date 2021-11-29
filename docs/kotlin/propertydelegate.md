# 使用属性委托

## 开始使用

添加依赖，本库提供了使用反射和不使用反射的用法，如果希望不使用反射，可换成代码下方对应的注释代码。

```gradle
implementation 'com.github.DylanCaiCoding.ViewBindingKTX:viewbinding-ktx:1.2.6'
// implementation 'com.github.DylanCaiCoding.ViewBindingKTX:viewbinding-nonreflection-ktx:1.2.6'
```

个人推荐使用反射的用法，多一次反射的性能损耗可忽略不计，代码可读性会好很多，与 ViewModel 的用法更加统一。

>下面的使用方式不能用于基类，想封装到基类请移步到 [封装到基类](/kotlin/baseclass) 的用法

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

如果需要在销毁 binding 对象前做其它释放操作，请实现 BindingLifecycleOwner 接口，重写 onDestroyViewBinding() 方法。

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
      // 如果使用了多个绑定对象，需要判断是对应的类型才做进行释放操作
    }
  }
}
```

### Adapter

下面是结合官方 ListAdapter 的使用示例，无需再写个类继承 `RecycleView.ViewHolder`，直接用 `BindingViewHolder` 进行替代，可快速创建和获取 binding 对象。

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

如果项目中的适配器封装了 ViewHolder 基类，可参考[兼容 BaseRecyclerViewAdapterHelper](/kotlin/brvah) 的方式另行适配。

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

以下是 TabLayout + ViewPager2 实现底部导航栏的示例。

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
      // ps: 修改选中颜色或图标建议用 selector 实现，如需要改字体大小、风格等才监听标签选中事件。
    }
  }

  override fun onTabUnselected(tab: TabLayout.Tab) {
  }

  override fun onTabReselected(tab: TabLayout.Tab) { 
  }
})
```

设置自定义布局后需要修改自定义布局样式，不要再调用 setCustomView()，而是调用 bindCustomView()。

### NavigationView

以下是 Navigation 设置头部布局的控件的示例。

```kotlin
navigationView.setHeaderView<LayoutNavHeaderBinding> {
// navigationView.setHeaderView(LayoutNavHeaderBinding::bind) {
  tvNickname.text = nickname
}
```