# Use in base class

There are two usages of [using reflection](/en/java/baseclass?id=use-reflection) and [not using reflection](/en/java/baseclass?id=don39t-use-reflection).

## Use reflection

Add dependency:

```gradle
implementation 'com.github.DylanCaiCoding.ViewBindingKTX:viewbinding-base:1.2.6'
```

The core steps:

1. Add a generic that extends ViewBinding to the base class.
2. Defining a binding object of the ViewBinding generic in the base class.
3. Initialize the binding object.
4. Replace the code that sets or returns the layout with `binding.getRoot()`.

### Activity

Base class:

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

Usage sample:

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

Base class:

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

Usage sample:

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

The following are samples of two libraries for adapters. If you are using these two libraries, you can copy them.

- #### [BaseRecyclerViewAdapterHelper](https://github.com/CymChad/BaseRecyclerViewAdapterHelper)

Base class:

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

Usage sample:

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

Base class:

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

Usage sample:

```java
class FooViewDelegate extends BindingViewDelegate<Foo, ItemFooBinding> {

  @Override
  public void onBindViewHolder(@NotNull BindingViewHolder<ItemFooBinding> holder, Foo item) {
    holder.getBinding().tvFoo.setText(item.getValue());
  }
}
```

## Don't use reflection

The core steps:

1. Add a generic that extends ViewBinding to the base class.
2. Define a binding object of the ViewBinding generic in the base class.
3. Add an abstract method of creating a binding object.
4. Initialize the binding object.
5. Replace the code that sets or returns the layout with `binding.getRoot()`.

### Activity

Base class:

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

Usage sample:

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

Base class:

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

Usage sample:

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

The following are samples of two libraries for adapters. If you are using these two libraries, you can copy them.

- #### [BaseRecyclerViewAdapterHelper](https://github.com/CymChad/BaseRecyclerViewAdapterHelper)

Base class:

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

Usage sample:

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

Base class:

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

Usage sample:

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