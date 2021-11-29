# 封装到基类

因为 Java 没有 Kotlin 的特性，所以只推荐封装在基类里使用。

下面会带着大家用最少的代码改造自己的基类使其用上 ViewBinding。因为大家的基类封装方式各式各样，所以会讲改造步骤和核心的改造代码，主要都是把 binding 对象封装在基类里替换掉原来的布局 id。

提供了[使用反射](/java/baseclass?id=使用反射)和[不使用反射](/java/baseclass?id=不使用反射)的两种封装思路，后者需要每次重写个方法并手动创建 binding 对象，相对会麻烦一点。大家可以对比两者的用法后进行选择。个人更推荐用反射的方式封装改造，多一次反射的性能损耗可以忽略不计。

## 使用反射

添加依赖：

```gradle
implementation 'com.github.DylanCaiCoding.ViewBindingKTX:viewbinding-base:1.2.6'
```

改造的核心步骤：

1. 在基类增加一个继承 ViewBinding 的泛型；
2. 在基类里定义一个类型是 ViewBinding 泛型的 binding 对象；
3. 用工具类方法初始化泛型的 binding；
4. 用 binding.root 替代原来设置或返回布局的代码；

### Activity

Activity 基类的核心改造代码：

```java
public abstract class BaseBindingActivity<VB extends ViewBinding> extends AppCompatActivity {

  private VB binding;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    binding = ViewBindingUtil.inflateWithGeneric(this, getLayoutInflater());
    setContentView(binding.getRoot());
  }

  public VB getBinding() {
    return binding;
  }
}
```

Activity 基类改造后的使用示例：

```java
public class MainActivity extends BaseBindingActivity<ActivityMainBinding>{

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    getBinding().getTvHelloWord().setText("Hello Android!");
  }
}
```

### Fragment

Fragment 基类的核心改造代码：

```java
public abstract class BaseBindingFragment<VB extends ViewBinding> extends Fragment {

  private VB binding;

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    binding = ViewBindingUtil.inflateWithGeneric(this, getLayoutInflater(), container, false);
    return binding.getRoot();
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    binding = null;
  }

  public VB getBinding() {
    return binding;
  }
}
```

Fragment 基类改造后的使用示例：

```java
class HomeFragment extends BaseBindingFragment<FragmentHomeBinding> {

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    getBinding().tvHelloWorld.setText("Hello Android!");
  }
}
```

### Adapter

下面提供两个适配器开源库的封装改造示例，如果你有在用这两个库，可以直接拷贝去用。如果是自己封装的适配器基类，可参考下面封装的思路进行改造。

- #### [BaseRecyclerViewAdapterHelper](https://github.com/CymChad/BaseRecyclerViewAdapterHelper)

封装基类代码：

```java
public abstract class BaseBindingQuickAdapter<T, VB extends ViewBinding>
    extends BaseQuickAdapter<T, BaseBindingQuickAdapter.BaseBindingHolder> {

  public BaseBindingQuickAdapter() {
    this(-1);
  }

  public BaseBindingQuickAdapter(@LayoutRes int layoutResId) {
    super(layoutResId);
  }

  @NotNull
  @Override
  protected BaseBindingHolder onCreateDefViewHolder(@NotNull ViewGroup parent, int viewType) {
    VB viewBinding = ViewBindingUtil.inflateWithGeneric(this, parent);
    return new BaseBindingHolder(viewBinding);
  }

  public static class BaseBindingHolder extends BaseViewHolder {

    private final ViewBinding binding;

    public BaseBindingHolder(@NotNull View view) {
      this(() -> view);
    }

    public BaseBindingHolder(@NotNull ViewBinding binding) {
      super(binding.getRoot());
      this.binding = binding;
    }

    @NonNull
    @SuppressWarnings("unchecked")
    public <VB extends ViewBinding> VB getViewBinding() {
      return (VB) binding;
    }
  }
}
```

封装后的使用示例：

```java
class FooAdapter extends BaseBindingQuickAdapter<Foo, ItemFooBinding> {

  @Override
  public void convert(@NotNull BindingViewHolder<ItemFooBinding> holder, Foo item) {
    ItemFooBinding binding = holder.getViewBinding();
    binding.tvFoo.setText(item.getValue());
  }
}
```

- #### [MultiType](https://github.com/drakeet/MultiType)

封装基类代码：

```java
public abstract class BindingViewDelegate<T, VB extends ViewBinding> extends
    ItemViewDelegate<T, BindingViewDelegate.BindingViewHolder<VB>> {
  @NotNull
  @Override
  public BindingViewHolder<VB> onCreateViewHolder(@NotNull Context context, @NotNull ViewGroup parent) {
    return new BindingViewHolder<>(ViewBindingUtil.inflateWithGeneric(this, parent));
  }

  public static class BindingViewHolder<VB extends ViewBinding> extends RecyclerView.ViewHolder {

    private final VB binding;

    public BindingViewHolder(@NonNull VB binding) {
      super(binding.getRoot());
      this.binding = binding;
    }

    @NonNull
    public VB getBinding() {
      return binding;
    }
  }
}
```

封装后的使用示例：

```java
class FooViewDelegate extends BindingViewDelegate<Foo, ItemFooBinding> {

  @Override
  public void onBindViewHolder(@NotNull BindingViewHolder<ItemFooBinding> holder, Foo item) {
    holder.getBinding().tvFoo.setText(item.getValue());
  }
}
```

## 不使用反射

改造的核心步骤：

1. 在基类增加一个继承 ViewBinding 的泛型；
2. 在基类里定义一个类型是 ViewBinding 泛型的 binding 对象；
3. 添加创建绑定对象的抽象方法，并使用该方法初始化 binding 对象；
4. 用 binding.root 替代原来设置或返回布局的代码；

### Activity

Activity 基类的核心改造代码：

```java
public abstract class BaseBindingActivity<VB extends ViewBinding> extends AppCompatActivity {

  private VB binding;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    binding = onCreateViewBinding(getLayoutInflater());
    setContentView(binding.getRoot());
  }

  protected abstract VB onCreateViewBinding(@NonNull LayoutInflater layoutInflater);

  public VB getBinding() {
    return binding;
  }
}
```

Activity 基类改造后的使用示例：

```java
public class MainActivity extends BaseBindingActivity<ActivityMainBinding> {
  @Override
  protected ActivityMainBinding onCreateViewBinding(@NonNull LayoutInflater layoutInflater) {
    return ActivityMainBinding.inflate(layoutInflater);
  }
    
  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    getBinding().getTvHelloWord().setText("Hello Android!");
  }
}
```

### Fragment

Fragment 基类的核心改造代码：

```java
public abstract class BaseBindingFragment<VB extends ViewBinding> extends Fragment {

  private VB binding;

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    binding = onCreateViewBinding(inflater, container);
    return binding.getRoot();
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    binding = null;
  }

  public VB getBinding() {
    return binding;
  }

  protected abstract VB onCreateViewBinding(@NonNull LayoutInflater inflater, @Nullable ViewGroup parent);
}
```

Fragment 基类改造后的使用示例：

```java
public class HomeFragment extends BaseBindingFragment<FragmentHomeBinding> {
  @Override
  protected FragmentHomeBinding onCreateViewBinding(@NonNull LayoutInflater inflater, @Nullable ViewGroup parent) {
    return FragmentHomeBinding.inflate(inflater, parent, false);
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    getBinding().getTvHelloWord().setText("Hello Android!");
  }
}
```

### Adapter

下面提供两个适配器开源库的封装改造示例，如果你有在用这两个库，可以直接拷贝去用。如果是自己封装的适配器基类，可参考下面封装的思路进行改造。

- #### [BaseRecyclerViewAdapterHelper](https://github.com/CymChad/BaseRecyclerViewAdapterHelper)

封装基类代码：

```java
public abstract class BaseBindingQuickAdapter<T, VB extends ViewBinding>
    extends BaseQuickAdapter<T, BaseBindingQuickAdapter.BaseBindingHolder> {

  public BaseBindingQuickAdapter() {
    this(-1);
  }

  public BaseBindingQuickAdapter(@LayoutRes int layoutResId) {
    super(-layoutResId);
  }

  @NotNull
  @Override
  protected BaseBindingHolder onCreateDefViewHolder(@NotNull ViewGroup parent, int viewType) {
    return new BaseBindingHolder(onCreateViewBinding(LayoutInflater.from(parent.getContext()), parent));
  }

  protected abstract VB onCreateViewBinding(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent);

  public static class BaseBindingHolder extends BaseViewHolder {

    private final ViewBinding binding;

    public BaseBindingHolder(@NotNull View view) {
      this(() -> view);
    }

    public BaseBindingHolder(@NotNull ViewBinding binding) {
      super(binding.getRoot());
      this.binding = binding;
    }

    @NonNull
    @SuppressWarnings("unchecked")
    public <VB extends ViewBinding> VB getViewBinding() {
      return (VB) binding;
    }
  }
}
```

封装后的使用示例：

```java
public class FooAdapter extends BaseBindingQuickAdapter<Foo, ItemFooBinding> {
  @Override
  protected ItemFooBinding onCreateViewBinding(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
    return ItemFooBinding.inflate(inflater, parent, false);
  }

  @Override
  protected void convert(@NotNull BaseBindingHolder holder, Foo item) {
    ItemFooBinding binding = holder.getViewBinding();
    binding.tvFoo.setText(item.getValue());
  }
}
```

- #### [MultiType](https://github.com/drakeet/MultiType)

封装基类代码：

```java
public abstract class BindingViewDelegate<T, VB extends ViewBinding> extends
    ItemViewDelegate<T, BindingViewDelegate.BindingViewHolder<VB>> {
  @NotNull
  @Override
  public BindingViewHolder<VB> onCreateViewHolder(@NotNull Context context, @NotNull ViewGroup parent) {
    return new BindingViewHolder<>(onCreateViewBinding(LayoutInflater.from(parent.getContext()), parent));
  }

  protected abstract VB onCreateViewBinding(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent);

  public static class BindingViewHolder<VB extends ViewBinding> extends RecyclerView.ViewHolder {

    private final VB binding;

    public BindingViewHolder(@NonNull VB binding) {
      super(binding.getRoot());
      this.binding = binding;
    }

    @NonNull
    public VB getBinding() {
      return binding;
    }
  }
}
```

封装后的使用示例：

```java
public class FooViewDelegate extends BindingViewDelegate<Foo, ItemFooBinding> {
  @Override
  protected ItemFooBinding onCreateViewBinding(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
    return ItemFooBinding.inflate(inflater, parent, false);
  }

  @Override
  public void onBindViewHolder(@NotNull BindingViewHolder<ItemFooBinding> holder, Foo foo) {
    holder.getBinding().tvFoo.setText(item.getValue());
  }
}
```