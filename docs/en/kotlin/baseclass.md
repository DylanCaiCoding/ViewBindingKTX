# Use in base class

There are two usages of [using reflection](/en/kotlin/baseclass?id=use-reflection) and [not using reflection](/en/kotlin/baseclass?id=don39t-use-reflection).

## Use reflection

Add dependency:

```gradle
implementation 'com.github.DylanCaiCoding.ViewBindingKTX:viewbinding-base:2.0.4'
```

The core steps:

1. Add a generic that extends ViewBinding to the base class.
2. Defining a binding object of the ViewBinding generic in the base class.
3. Initialize the binding object.
4. Replace the code that sets or returns the layout with `binding.root`.

### Activity

Base class:

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

Usage sample:

```kotlin
class MainActivity : BaseBindingActivity<ActivityMainBinding>() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding.tvHelloWorld.text = "Hello Android!"
  }
}
```

### Fragment

Base class:

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

Usage sample:

```kotlin
class HomeFragment: BaseBindingFragment<FragmentHomeBinding>() {

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    binding.tvHelloWorld.text = "Hello Android!"
  }
}
```

### Adapter

The following are samples of two libraries for adapters. If you are using these two libraries, you can copy them.

- #### [BaseRecyclerViewAdapterHelper](https://github.com/CymChad/BaseRecyclerViewAdapterHelper)

Base class: 

```kotlin
abstract class BaseBindingQuickAdapter<T, VB : ViewBinding>(layoutResId: Int = -1) :
  BaseQuickAdapter<T, BaseBindingQuickAdapter.BaseBindingHolder>(layoutResId) {

  override fun onCreateDefViewHolder(parent: ViewGroup, viewType: Int) =
    BaseBindingHolder(ViewBindingUtil.inflateWithGeneric<VB>(this, parent))

  class BaseBindingHolder(private val binding: ViewBinding) : BaseViewHolder(binding.root) {
    constructor(itemView: View) : this(ViewBinding { itemView })

    @Suppress("UNCHECKED_CAST")
    fun <VB : ViewBinding> getViewBinding() = binding as VB
  }
}
```

Usage sample:

```kotlin
class FooAdapter : BaseBindingQuickAdapter<Foo, ItemFooBinding>() {

  override fun convert(holder: BaseBindingHolder<ItemFooBinding>, item: Foo) {
    holder.getViewBinding<ItemFooBinding>().apply {
      tvFoo.text = item.value
    }
  }
}
```

- #### [MultiType](https://github.com/drakeet/MultiType)

Base class:

```kotlin
abstract class BindingViewDelegate<T, VB : ViewBinding> : ItemViewDelegate<T, BindingViewHolder<VB>>() {

  override fun onCreateViewHolder(context: Context, parent: ViewGroup) =
    BindingViewHolder(ViewBindingUtil.inflateWithGeneric<VB>(this, parent))

  class BindingViewHolder<VB : ViewBinding>(val binding: VB) : RecyclerView.ViewHolder(binding.root)
}
```

Usage sample:

```kotlin
class FooViewDelegate : BindingViewDelegate<Foo, ItemFooBinding>() {

  override fun onBindViewHolder(holder: BindingViewHolder<ItemFooBinding>, item: Foo) {
    holder.binding.apply {
      tvFoo.text = item.value
    }
  }
}
```

## Don't use reflection

The core steps:

1. Add a generic that extends ViewBinding to the base class.
2. Define a binding object of the ViewBinding generic in the base class.
3. Add the method of creating a binding object to the constructor.
4. Initialize the binding object.
5. Replace the code that sets or returns the layout with `binding.root`.

### Activity

Base class:

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

Usage sample:

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

Base class:

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

Usage sample:

```kotlin
class HomeFragment: BaseBindingFragment<FragmentHomeBinding>(FragmentHomeBinding::inflate) {

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    binding.tvHelloWorld.text = "Hello Android!"
  }
}
```

### Adapter

The following are samples of two libraries for adapters. If you are using these two libraries, you can copy them.

- #### [BaseRecyclerViewAdapterHelper](https://github.com/CymChad/BaseRecyclerViewAdapterHelper)

Base class:

```kotlin
abstract class BaseBindingQuickAdapter<T, VB : ViewBinding>(
  private val inflate: (LayoutInflater, ViewGroup, Boolean) -> VB,
  layoutResId: Int = -1
) :
  BaseQuickAdapter<T, BaseBindingQuickAdapter.BaseBindingHolder>(layoutResId) {

  override fun onCreateDefViewHolder(parent: ViewGroup, viewType: Int) =
    BaseBindingHolder(inflate(LayoutInflater.from(parent.context), parent, false))

  class BaseBindingHolder(private val binding: ViewBinding) : BaseViewHolder(binding.root) {
    constructor(itemView: View) : this(ViewBinding { itemView })

    @Suppress("UNCHECKED_CAST")
    fun <VB : ViewBinding> getViewBinding() = binding as VB
  }
}
```

Usage sample:

```kotlin
class FooAdapter : BaseBindingQuickAdapter<Foo, ItemFooBinding>(ItemFooBinding::inflate) {

  override fun convert(holder: BaseBindingHolder, item: Foo) {
    holder.getViewBinding<ItemFooBinding>().apply {
      tvFoo.text = item.value
    }
  }
}
```

- #### [MultiType](https://github.com/drakeet/MultiType)

Base class:

```kotlin
abstract class BindingViewDelegate<T, VB : ViewBinding> (
  private val inflate: (LayoutInflater, ViewGroup, Boolean) -> VB
): ItemViewDelegate<T, BindingViewHolder<VB>>() {

  override fun onCreateViewHolder(context: Context, parent: ViewGroup) : BindingViewHolder<VB> =
    BindingViewHolder(inflate(LayoutInflater.from(parent.context), parent, false))

  class BindingViewHolder<VB : ViewBinding>(val binding: VB) : RecyclerView.ViewHolder(binding.root)
}
```

Usage sample:

```kotlin
class FooViewDelegate : BindingViewDelegate<Foo, ItemFooBinding>(ItemFooBinding::inflate) {

  override fun onBindViewHolder(holder: BindingViewHolder<ItemFooBinding>, item: Foo) {
    holder.binding.apply {
      tvFoo.text = item.value
    }
  }
}
```
