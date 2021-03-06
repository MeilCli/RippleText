# RippleText
Ripple effect for clickable text on android view and composable function

|AndroidView|ComposableFunction|
|:--:|:--:|
|<video src="https://user-images.githubusercontent.com/2821921/177032841-807f6f1c-a91b-44f1-9ca2-1bf502da1c3a.mp4" />|<video src="https://user-images.githubusercontent.com/2821921/176992580-090c9d8e-3762-4923-8c4c-7d870c164896.mp4" />|

## Installation
RippleText is published to MavenCentral.

### Android View
```groovy
dependencies {
    // you can select artifact by your text view kind
    implementation "net.meilcli.rippletext:core:0.0.1"
    implementation "net.meilcli.rippletext:appcompat:0.0.1"
    implementation "net.meilcli.rippletext:emoji:0.0.1"
    implementation "net.meilcli.rippletext:emoji-appcompat:0.0.1"
    implementation "net.meilcli.rippletext:emoji2-views:0.0.1"
}
```

### Composable function
```groovy
dependencies {
    implementation "net.meilcli.rippletext:compose:0.0.1"
}
```

## Usage
### Android View
Usage is easily, replace your text view at xml
- TextView -> `net.meilcli.rippletext.RippleTextView`
  - RippleTextView is for usage of OS implementation text view, so recommend to use RippleAppCompatTextView
- AppCompatTextView -> `net.meilcli.rippletext.appcompat.RippleAppCompatTextView`
- EmojiTextView -> `net.meilcli.rippletext.emoji.RippleEmojiTextView`
- EmojiAppCompatTextView -> `net.meilcli.rippletext.emoji.appcompat.RippleEmojiAppCompatTextView`
- EmojiTextView(emoji2) -> `net.meilcli.rippletext.emoji2.views.RippleEmojiTextView`

#### Customization
*Change ripple drawable*
- Use `app:textRipple=""` at xml
- Use `setRippleDrawable(drawableCreator: () -> Drawable)` function
- Use `setRippleDrawable(@DrawableRes drawableResource: Int)` function

#### Integrate to your custom view
Basic logic of RippleText is provided by `net.meilcli.rippletext.core.RippleTextPresenter`.

So, you can integrated to your custom view using RippleTextPresenter.

```kt
// in your custom text view
private val presenter = RippleTextPresenter(this, this::createDrawableState)

init {
    presenter.initialize()
}

private fun createDrawableState(vararg additionalState: Int): IntArray {
    val baseState = onCreateDrawableState(additionalState.size)
    return mergeDrawableStates(baseState, additionalState)
}

override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
    super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    presenter.onMeasure()
}

override fun jumpDrawablesToCurrentState() {
    super.jumpDrawablesToCurrentState()
    presenter.jumpDrawablesToCurrentState()
}

override fun verifyDrawable(who: Drawable): Boolean {
    return super.verifyDrawable(who) || presenter.verifyDrawable(who)
}

override fun draw(canvas: Canvas?) {
    if (canvas != null) {
        presenter.draw(canvas)
    }
    super.draw(canvas)
}

@SuppressLint("ClickableViewAccessibility")
override fun onTouchEvent(event: MotionEvent?): Boolean {
    if (event == null) {
        return super.onTouchEvent(event)
    }
    return presenter.onTouchEvent(event) || super.onTouchEvent(event)
}
```

### Composable function
Sample code:
```kt
@Composable
private fun Sample() {
    val text = buildAnnotatedString {
        val addClickableText = {
            val index = pushStringAnnotation(tag = "click", annotation = "clickable")
            pushStyle(SpanStyle(color = Color.Blue))
            append("This text is clickable and effective.")
            pop(index)
        }
        val addNonClickableText = {
            append("This text is not clickable.")
        }
        for (i in 0 until 15) {
            for (j in 0..i) {
                addNonClickableText()
            }
            append(' ')
            addClickableText()
            append('\n')
        }
    }
    RippleText(
        text = text,
        modifier = Modifier.padding(16.dp),
        style = MaterialTheme.typography.body1,
        onClick = {
            val annotations = text.getStringAnnotations("click", start = it, end = it)
            for (annotation in annotations) {
                val clickedText = text.substring(annotation.start, annotation.end)
                showToast("Clicked: $clickedText, ${annotation.start}~${annotation.end}")
            }
        }
    )
}
```

## License
MIT License

### Usings
- [Kotlin Coroutines](https://github.com/Kotlin/kotlinx.coroutines)
- [AndroidX](https://github.com/androidx/androidx)
- Material Components
  - https://github.com/material-components/material-components-android
  - https://github.com/material-components/material-components-android-compose-theme-adapter
