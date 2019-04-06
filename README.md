# Injected ViewModel Provider
This is a small lib to use easily use Android's ViewModels with a depedency injection framework like dagger. You obtain a `ViewModel` from a `javax.inject.Provider` instead of a `ViewModelProvider.Factory`.

## Usage

### From Java

#### Download
[![Maven Central](https://img.shields.io/maven-central/v/me.tatarka.injectedvmprovider/injectedvmprovider.svg)](https://search.maven.org/search?q=g:me.tatarka.injectedvmprovider)
[![Sonatype Snapshot](https://img.shields.io/nexus/s/https/oss.sonatype.org/me.tatarka.injectedvmprovider/injectedvmprovider.svg)](https://oss.sonatype.org/content/repositories/snapshots/me/tatarka/injectedvmprovider/)

```groovy
implementation 'me.tatarka.injectedvmprovider:injectedvmprovider-extensions:2.1.0'
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
        MyViewModel vm = InjectedViewModelProviders.of(this).get(vmProvider);
    }
}
```
Note: If you aren't using fragments, you can use `me.tatarka.injectedvmprovider:injectedvmprovider:2.1.0`, and use `new InjectedViewModelProvider(viewModelStoreOwner)` instead.



If you have a factory, you can inject that instead. This is useful for passing in intent arguments to the view model, and/or with [assisted injection](https://github.com/square/AssistedInject).

```java
@Inject
MyViewModel.Factory vmFactory;

@Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    ...
    MyViewModel vm = InjectedViewModelProviders.of(this).get(vmFactory, factory -> factory.create("arg"));
}
```

### From Kotlin

#### Download
```groovy
implementation 'me.tatarka.injectedvmprovider:injectedvmprovider-ktx:2.1.0'
```

#### Usage

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
