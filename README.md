# compose-explode

Explode compose elements on click! Just add `explodeOnClick()` modifier!

<p align="center">
<img src="https://user-images.githubusercontent.com/16976114/133917014-298530c8-82b8-4660-bc89-8b085617f44c.gif"/>
</p>

Inspired from [ExplosionField](https://github.com/tyrantgit/ExplosionField)

## Getting started

1. Go to [library/explodeOnClick.kt](https://github.com/CuriousNikhil/compose-explode/blob/main/library/src/main/java/me/nikhilchaudhari/explode/explodeOnClick.kt)
2. Copy paste the `explodeOnClick.kt` file in your compose project.
3. That's it

p.s. I'm too lazy to release this on maven. Please bear with me I'll do it soon.


## Usage

Just add `explodeOnClick()` modifier on your composable element.

```kotlin
  Text(
    text = "Click me to explode!",
    // Just add this modifier 
    modifier = Modifier.explodeOnClick()
   )
```

### onClick lambda
Lambda will be called when user clicks the element. You can pass `onClick` if you want to perform any action on click.

```kotlin
Modifier.explodeOnClick(color = backColor,
          onClick = {
              Toast
                  .makeText(context, "Toast message", Toast.LENGTH_LONG)
                  .show()
          })
```


### durationMillis
Set the duration of animation, default is 1 sec.

```kotlin
modifier = Modifier.explodeOnClick(durationMillis=1500)
```

### easing
Set easing for the animation, default is `LinearEasing`.

```kotlin
modifier = Modifier.explodeOnClick(easing=LinearEasing)
```

### Color
Set the color of the animation/particles, default is `Color.Black`.

```kotlin
modifier = Modifier.explodeOnClick(color = Color.GREEN)
```

### repeatable
Set if you want to make the animation repeatable again after click.

```kotlin
modifier = Modifier.explodeOnClick(repeatable = true)
```

Which looks like this ..

<p align="center">
<img src="https://user-images.githubusercontent.com/16976114/133917023-8152dbf2-37df-44ae-bce7-f498da9705ef.gif" />
</p>


## Contribution
Please contribute if you feel there's something missing/wrong or anything you feel right. Just raise the PR I would be happy to accept.


## License
Licensed under Apache License, Version 2.0 [here](https://github.com/CuriousNikhil/compose-explode/blob/main/LICENSE)
