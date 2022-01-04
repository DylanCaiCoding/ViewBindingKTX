# Use in base class

There are two usages of [using reflection](/en/java/baseclass?id=use-reflection) and [not using reflection](/en/java/baseclass?id=don39t-use-reflection).

## Use reflection

Add dependency:

```gradle
implementation 'com.github.DylanCaiCoding.ViewBindingKTX:viewbinding-base:2.0.0'
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

The following is the sample of [MultiType](https://github.com/drakeet/MultiType) library. If you are using the library, you can copy it.

Base class:

```java
public abstract class BindingViewDelegate<T, VB extends ViewBinding> extends
    ItemViewDelegate<T, BindingViewDelegate.BindingViewHolder<VB>> {
  @NotNull
  @Override
  public BindingViewHolder<VB> onCreateViewHolder(@NotNull Context context, @NotNull ViewGroup parent) {
    return new BindingViewHolder<>(ViewBindingUtil.inflateWithGeneric(this, parent));
  }

  @Override
  public void onBindViewHolder(@NonNull BindingViewHolder<VB> holder, T t) {
    onBindViewHolder(holder.getBinding(), t, holder.getAdapterPosition());
  }

  protected abstract void onBindViewHolder(VB binding, T item, int position);

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
  protected void onBindViewHolder(@NotNull ItemFooBinding binding, Foo foo, int position) {
    binding.tvFoo.setText(item.getValue());
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

The following is the sample of [MultiType](https://github.com/drakeet/MultiType) library. If you are using the library, you can copy it.

Base class:

```java
public abstract class BindingViewDelegate<T, VB extends ViewBinding> extends
    ItemViewDelegate<T, BindingViewDelegate.BindingViewHolder<VB>> {
  @NotNull
  @Override
  public BindingViewHolder<VB> onCreateViewHolder(@NotNull Context context, @NotNull ViewGroup parent) {
    return new BindingViewHolder<>(onCreateViewBinding(LayoutInflater.from(parent.getContext()), parent));
  }

  @Override
  public void onBindViewHolder(@NonNull BindingViewHolder<VB> holder, T t) {
    onBindViewHolder(holder.getBinding(), t, holder.getAdapterPosition());
  }

  protected abstract VB onCreateViewBinding(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent);

  protected abstract void onBindViewHolder(VB binding, T item, int position);

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
  protected void onBindViewHolder(@NotNull ItemFooBinding binding, Foo foo, int position) {
    binding.tvFoo.setText(item.getValue());
  }
}
```