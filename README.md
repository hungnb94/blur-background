# Protect screen content
[![](https://jitpack.io/v/hungnb94/blur-background.svg)](https://jitpack.io/#hungnb94/blur-background)

Hide app content in [Android Recent Apps](https://developer.android.com/guide/components/activities/recents)


## Installation

**Step 1.** Add the JitPack repository to your build.gradle file

```groovy
allprojects {
    repositories {
        ...
        maven { url = uri('https://jitpack.io') }
    }
}
```

**Step 2.** Add the dependency

```groovy
dependencies {
    implementation 'com.github.hungnb94:blur-background:[LATEST_VERSION]'
}
```


## Usage

```kotlin
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Should call before set content view
        ScreenshotProtector.protect(this)
        setContentView(R.layout.activity_main)
    }

}
```
