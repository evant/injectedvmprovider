# Injected ViewModel Provider
This is a small lib to use easily use Android's ViewModels with a depedency injection framework like dagger. You obtain a `ViewModel` from a `javax.inject.Provider` instead of a `ViewModelProvider.Factory`.

## Usage

### From Java

#### Download
[![Maven Central](https://img.shields.io/maven-central/v/me.tatarka.injectedvmprovider/injectedvmprovider.svg)](https://search.maven.org/search?q=g:me.tatarka.injectedvmprovider)
[![Sonatype Snapshot](https://img.shields.io/nexus/s/https/oss.sonatype.org/me.tatarka.injectedvmprovider/injectedvmprovider.svg)](https://oss.sonatype.org/content/repositories/snapshots/me/tatarka/injectedvmprovider/)

```groovy
implementation 'me.tatarka.injectedvmprovider:injectedvmprovider:2.2.0'
```

#### Usage

Set up your ViewModel

```java
public class MyViewModel extends ViewModel {
    private final MyDependency source;

    @Inject
    public MainViewModel(MyDependency source) {
        this.source = source;
    }
}
```

Inject your ViewModel provider into the desired Fragment or Activity

```java
public class MyActivity extends AppCompatActivity {

    @Inject
    Provider<MyViewModel> vmProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ...
        MyViewModel vm = InjectedViewModelProvider.of(this).get(vmProvider);
    }
}
```

If you have a factory, you can inject that instead. This is useful for passing in intent arguments to the view model, and/or with [assisted injection](https://github.com/square/AssistedInject).
For the key, you can either pass the factory instance or the view model class.

```java
class MyActivity extends ComponentActivity {
    @Inject
    MyViewModel.Factory vmFactory;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyViewModel vm = InjectedViewModelProvider.of(this).get(vmFactory, factory -> factory.create("arg"));
        MyViewModel vm2 = InjectedViewModelProvider.of(this).get(MyViweModel.class, () -> vmFactory.create("arg"));
    }
} 
```

### From Kotlin

#### Download

```groovy
implementation 'me.tatarka.injectedvmprovider:injectedvmprovider-ktx:2.2.0'
```

#### Usage

Set up your ViewModel

```kotlin
class MyViewModel @Inject constructor(val source: MyDependency) {
}
```

Use the `viewModel` delegate to obtain a view model from an injected provider.

```kotlin
class MyFragment @Inject constructor(val vmProvider: Provider<MyViweModel>) {
    val vm by viewModel(vmProvider)
}
```

If you have field injection or are using a factory, you pass a lambda to view model instead.  

```kotlin
class MyActivity: ComponentActivity {
    @Inject
    latinit var vmProvider: Provider<MyViewModel>
    @Inject
    latinit var vmFactory: MyViewModel.Factory
    
    val vm by viewModel { vmProvider.get() }
    val vm2 by viewModel { vmFactory.create("arg") }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        inject(this)
    }
}
```

ViewModel
```kotlin
class MainViewModel @Inject constructor(val dependency: Dependency): ViewModel()
```

Injection
```kotlin
class KotlinDaggerMainActivity : AppCompatActivity() {

    @Inject
    lateinit var vmProvider: Provider<MainViewModel>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ...
        val vm = injectedViewModelProvider.get(vmProvider)
    }
}
```
