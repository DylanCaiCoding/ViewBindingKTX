# 封装到基类

下面会带着大家用最少的代码改造自己的基类使其用上 ViewBinding。因为大家的基类封装方式各式各样，所以下面会讲改造步骤和核心的改造代码，主要都是把 binding 对象封装在基类里替换掉原来的布局 id。

提供了[使用反射](/kotlin/baseclass?id=使用反射)和[不使用反射](/kotlin/baseclass?id=不使用反射)的两种封装思路，后者需要在构造函数多传一个方法参数，使用的代码会更长。而且代码阅读性也更差，绝大多数人不知道为什么要这么来写。大家可以对比两者的用法后进行选择。个人更推荐用反射的方式封装改造，多一次反射的性能损耗可以忽略不计。

## 使用反射

添加依赖：

```gradle
implementation 'com.github.DylanCaiCoding.ViewBindingKTX:viewbinding-base:2.0.5'
```

改造的核心步骤：

1. 在基类增加一个继承 ViewBinding 的泛型；
2. 在基类里定义一个类型是 ViewBinding 泛型的 binding 对象；
3. 用工具类方法初始化泛型的 binding；
4. 用 binding.root 替代原来设置或返回布局的代码；

### Activity

Activity 基类的核心改造代码：

```kotlin
abstract class BaseBindingActivity<VB : ViewBinding> : AppCompatActivity() {

  lateinit var binding: VB

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = ViewBindingUtil.inflateWithGeneric(this, layoutInflater)
    setContentView(binding.root)
  }
}
```

Activity 基类改造后的使用示例：

```kotlin
class MainActivity : BaseBindingActivity<ActivityMainBinding>() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding.tvHelloWorld.text = "Hello Android!"
  }
}
```

### Fragment

Fragment 基类的核心改造代码：

```kotlin
abstract class BaseBindingFragment<VB : ViewBinding> : Fragment() {

  private var _binding: VB? = null
  val binding:VB get() = _binding!!

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
    _binding = ViewBindingUtil.inflateWithGeneric(this, inflater, container, false)
    return binding.root
  }

  override fun onDestroyView() {
    super.onDestroyView()
    _binding = null
  }
}
```

Fragment 基类改造后的使用示例：

```kotlin
class HomeFragment: BaseBindingFragment<FragmentHomeBinding>() {

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    binding.tvHelloWorld.text = "Hello Android!"
  }
}
```

### Adapter

下面是第三方适配器库 [MultiType](https://github.com/drakeet/MultiType) 的封装示例，如果是自己封装的适配器基类，可参考下面的封装思路进行改造。

封装基类代码：

```kotlin
abstract class BindingViewDelegate<T, VB : ViewBinding> : ItemViewDelegate<T, BindingViewHolder<VB>>() {

  override fun onCreateViewHolder(context: Context, parent: ViewGroup) =
    BindingViewHolder(ViewBindingUtil.inflateWithGeneric<VB>(this, parent))
  
  override fun onBindViewHolder(holder: BindingViewHolder<VB>, item: T) {
    onBindViewHolder(holder.binding, item, holder.adapterPosition)
  }

  abstract fun onBindViewHolder(holder: VB, item: T, position: Int)

  class BindingViewHolder<VB : ViewBinding>(val binding: VB) : RecyclerView.ViewHolder(binding.root)
}
```

封装后的使用示例：

```kotlin
class FooViewDelegate : BindingViewDelegate<Foo, ItemFooBinding>() {

  override fun onBindViewHolder(binding: ItemFooBinding, item: Foo, position: Int) {
    binding.tvFoo.text = item.value
  }
}
```

## 不使用反射


改造的核心步骤：

1. 在基类增加一个继承 ViewBinding 的泛型；
2. 在基类里定义一个类型是 ViewBinding 泛型的 binding 对象；
3. 在构造函数传入创建 binding 对象的方法，并初始化泛型的 binding；
4. 用 binding.root 替代原来设置或返回布局的代码；

### Activity

Activity 基类的核心改造代码：

```kotlin
abstract class BaseBindingActivity<VB : ViewBinding>(
  private val inflate: (LayoutInflater) -> VB
) : AppCompatActivity() {

  lateinit var binding: VB

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = inflate(layoutInflater)
    setContentView(binding.root)
  }
}
```

Activity 基类改造后的使用示例：

```kotlin
class MainActivity : 
  BaseBindingActivity<ActivityMainBinding>(ActivityMainBinding::inflate) {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding.tvHelloWorld.text = "Hello Android!"
  }
}
```

### Fragment

Fragment 基类的核心改造代码：

```kotlin
abstract class BaseBindingFragment<VB : ViewBinding>(
  private val inflate: (LayoutInflater, ViewGroup?, Boolean) -> VB
) : Fragment() {

  private var _binding: VB? = null
  val binding: VB get() = _binding!!

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
    _binding = inflate(inflater, container, false)
    return binding.root
  }

  override fun onDestroyView() {
    super.onDestroyView()
    _binding = null
  }
}
```

Fragment 基类改造后的使用示例：

```kotlin
class HomeFragment: BaseBindingFragment<FragmentHomeBinding>(FragmentHomeBinding::inflate) {

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    binding.tvHelloWorld.text = "Hello Android!"
  }
}
```

### Adapter

下面是第三方适配器库 [MultiType](https://github.com/drakeet/MultiType) 的封装示例，如果是自己封装的适配器基类，可参考下面的封装思路进行改造。

封装基类代码：

```kotlin
abstract class BindingViewDelegate<T, VB : ViewBinding> (
  private val inflate: (LayoutInflater, ViewGroup, Boolean) -> VB
): ItemViewDelegate<T, BindingViewHolder<VB>>() {

  override fun onCreateViewHolder(context: Context, parent: ViewGroup) : BindingViewHolder<VB> =
    BindingViewHolder(inflate(LayoutInflater.from(parent.context), parent, false))
    
  override fun onBindViewHolder(holder: BindingViewHolder<VB>, item: T) {
    onBindViewHolder(holder.binding, item, holder.adapterPosition)
  }

  abstract fun onBindViewHolder(holder: VB, item: T, position: Int)

  class BindingViewHolder<VB : ViewBinding>(val binding: VB) : RecyclerView.ViewHolder(binding.root)
}
```

封装后的使用示例：

```kotlin
class FooViewDelegate : BindingViewDelegate<Foo, ItemFooBinding>(ItemFooBinding::inflate) {

  override fun onBindViewHolder(binding: ItemFooBinding, item: Foo, position: Int) {
    binding.tvFoo.text = item.value
  }
}
```
