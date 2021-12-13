# Q&A

### include 的布局怎么使用 ViewBinding？

以下是 ViewBinding 本身的用法，有两种使用情况：

- #### include 的布局不含 merge 标签。

首先要给 include 的布局增加一个 id。

```xml
<include
    android:id="@+id/toolbar"
    layout="@layout/layout_toolar" />
```

这样就可以通过父布局的绑定对象拿到子布局的绑定对象了。

```kotlin
binding.toolbar.tvTitle.text = title
```

- #### include 的布局包含 merge 标签。

这和前面用法有点不一样，不能给 include 标签增加 id。

要用子布局绑定类的 bind() 方法传入父布局的根布局去获取绑定对象。

```kotlin
val mergeBinding = LayoutMergeBinding.bind(binding.root)
```

### 为什么在 Fragment 使用 ViewBinding 时，要在 onDestroyView() 清理绑定对象？

因为 Fragment 的存在时间比其视图长，就是 onDestroyView() 把视图销毁了，Fragment 可能还没走 onDestroy()，此时由于绑定对象还持有着视图，这个本该被释放回收的 View 在 onDestroyView() 后还存在内存中，并未及时的释放，这是不合理的。

那么不释放会导致什么问题呢？其实多数情况下不释放绑定对象也不会报错，个人目前想到的问题是未能及时释放视图的内存，不知道会不会还有其它隐患，后续遇到的话再进行补充。反正在 onDestroyView 释放了总没错，官方也是这么要求的。本库的 Fragment 拓展函数会按照官方要求自动释放绑定对象。

### ViewBinding 和 DataBinding 有什么关系？

两者是一种包含关系，ViewBinding 是 DataBinding 的一部分功能，即 ViewBinding 能做的事，DataBinding 都能做。所以 DataBinding 也能用通过 inflate 方法和 bind 方法创建绑定对象，这样与 ViewBinding 的用法保持一致，不需要用 DataBindingUtil。

两者生成绑定类的机制不一样，ViewBinding 是开启后对模块内每个布局进行生成，在布局里加上 `tools:viewBindingIgnore="true"` 才忽略。而 DataBinding 是只对添加了 layout 标签的布局才生成。利用这个机制，能处理些特殊场景。比如之前有个小伙伴的需求是想在已有的项目的几个页面用 ViewBinding，需要在很多个布局加忽略。这种情况就建议用 DataBinding，因为 DataBinding 只作用于添加了 layout 标签的布局，更符合需求。用 DataBinding 不一定就要用数据绑定，可以只用视图绑定，当作 ViewBinding 来用。

另外用 DataBinding 记得设置 lifecycleOwner。第一次用的时候很容易漏掉，导致数据设了却没有效果，看了很多文章也找不到原因。因为 DataBinding 出得早，当时没有 LiveData，所以大多文章都不会告诉你要设置 lifecycleOwner。明明自己完全照着网上的文章去用了，但就是没效果，很让人怀疑人生，其实是自己漏了重要的一步却无从得知。本库的拓展函数会自动设置，能省去这一步，可以放心地使用 DataBinding。

```kotlin
class MainActivity : AppCompatActivity() {

  private val binding: ActivityMainBinding by binding()
  private val viewModel: MainViewModel by viewModels()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding.vm = viewModel
  }
}
```