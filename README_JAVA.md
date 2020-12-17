# ViewBindingKtx

[![Download](https://api.bintray.com/packages/dylancai/maven/viewbinding-ktx/images/download.svg)](https://bintray.com/dylancai/maven/viewbinding-ktx/_latestVersion) [![](https://img.shields.io/badge/License-Apache--2.0-green.svg)](https://github.com/DylanCaiCoding/ViewBindingKtx/blob/master/LICENSE)

ViewBinding 相对于 Kotlin synthetics、ButterKnife、findViewById，能减少 id 写错或类型写错导致的异常，官方和 JakeWharton 都推荐使用。但是 ViewBinding 直接使用会有点繁琐，所以 [ViewBindingKtx](https://github.com/DylanCaiCoding/ViewBindingKtx) 就诞生了，**能够用最少的代码来使用 ViewBinding**。

[Kotlin](https://github.com/DylanCaiCoding/ViewBindingKtx) | Java

在 module 的 build.gradle 添加以下代码：

```
android {
    viewBinding {
        enabled = true
    }
}

dependencies {
    implementation 'com.dylanc:viewbinding-ktx:1.0.0'
}
```

这里是让大家用最少的代码改造自己的基类，从而使用上 ViewBinding。主要是把 binding 对象封装在基类里替换掉布局。

因为大家的基类封装方式各式各样，所以讲下核心的改造步骤：

1. 增加一个继承 ViewBinding 的泛型；
2. 在类里定义一个类型是 ViewBinding 泛型的 binding 对象；
3. 用工具类方法初始化泛型的 binding；
4. 用 binding.root 替代原来设置或返回布局的代码；

Activity 基类改造的核心代码：

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
class MainActivity extends BaseBindingActivity<ActivityMainBinding> {

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    getBinding().tvHelloWorld.setText("Hello Android!");
  }
}
```

Fragment 基类改造的核心代码：

```java
public abstract class BaseBindingFragment<VB extends ViewBinding> extends Fragment {

  private VB binding;

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    binding = ViewBindingUtil.inflateWithGeneric(this, getLayoutInflater());
    return binding.getRoot();
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

Dialog 基类改造的核心代码：

```java
public abstract class BaseBindingDialog<VB extends ViewBinding> extends Dialog {

  private VB binding;

  public BaseBindingDialog(@NonNull Context context) {
    this(context, 0);
  }

  public BaseBindingDialog(@NonNull Context context, int themeResId) {
    super(context, themeResId);
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    binding = ViewBindingUtil.inflateWithGeneric(this, getLayoutInflater());
    setContentView(binding.getRoot());
  }

  public VB getBinding() {
    return binding;
  }
}
```

Dialog 基类改造后的使用示例：

```java
class LoadingDialog extends BaseBindingDialog<DialogLoadingBinding> {
    
  public LoadingDialog(@NonNull Context context) {
    super(context);
  }

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    getBinding().tvHelloWorld.setText("Hello Android!");
  }
}
```

Adapter 基类的改造步骤：

1. 增加一个继承 ViewBinding 的泛型；
2. 将原来的 ViewHolder 改成 BindingViewHolder；
3. 删除原有的用布局创建 ViewHolder 相关代码，改成一个用泛型创建 BindingViewHolder 的方法；

用列表库 [Drakeet/MultiType]() 为例子，ViewDelegate 可以当成 Adapter 来看。下面是核心的代码：

```java
public abstract class BindingViewDelegate<T, VB extends ViewBinding> extends ItemViewDelegate<T, BindingViewHolder<VB>> {
  @NotNull
  @Override
  public BindingViewHolder<VB> onCreateViewHolder(@NotNull Context context, @NotNull ViewGroup parent) {
    return BindingViewHolder.createWithGeneric(this, parent);
  }
}
```

Adapter 基类改造后的使用示例：

```java
class FooViewDelegate extends BindingViewDelegate<Foo, ItemFooBinding> {

  @Override
  public void onBindViewHolder(@NotNull BindingViewHolder<ItemFooBinding> holder, Foo item) {
    holder.getBinding().tvFoo.setText(item.getValue());
  }
}
```

## 混淆

```
-keepclassmembers class * implements androidx.viewbinding.ViewBinding {
  public static ** inflate(...);
}
```

## License

```
Copyright (C) 2020. Dylan Cai

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
