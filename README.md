# KImageCheck [![Build Status](https://travis-ci.org/lamba92/KImageCheck.svg?branch=master)](https://travis-ci.org/lamba92/KImageCheck) [![](https://jitpack.io/v/lamba92/kimagecheck.svg)](https://jitpack.io/#lamba92/kimagecheck)

Allows to check if a file is an image, if so check if it's truncated.

Written in Kotlin with ❤️.

Based on [Archimedes Trajano](https://stackoverflow.com/users/242042/archimedes-trajano)'s [answer](https://stackoverflow.com/a/10069478/2331319) on StackOverflow.com

## Usage

If using Kotlin:

```kotlin
val myFile = File(..)
val imageData = myFile.imageData

val myPath = Paths.get(...)
val imageData = myPath.imageData
```

If using Java:
```java
File myFile = new File(..)
ImageData imageData = analyzeImage(myFile)

Path myPath = Paths.get(..)
ImageData imageData = analyzeImage(myPath)
```

## Installing [![](https://jitpack.io/v/lamba92/kimagecheck.svg)](https://jitpack.io/#lamba92/kimagecheck)

Add the [JitPack.io](http://jitpack.io) repository to the project `build.grade`:
```
repositories {
    maven { url 'https://jitpack.io' }
}
```

Then import the latest version in the `build.gradle` of the modules you need:

```
dependencies {
    implementation 'com.github.lamba92:kimagecheck:{latest_version}'
}
```

If using Gradle Kotlin DSL:
```
repositories {
    maven(url = "https://jitpack.io")
}
...
dependencies {
    implementation("com.github.lamba92", "kimagecheck", "{latest_version}")
}
```
For Maven:
```
<repositories>
   <repository>
      <id>jitpack.io</id>
      <url>https://jitpack.io</url>
   </repository>
</repositories>
...
<dependency> 	 
   <groupId>com.github.Lamba92</groupId>
   <artifactId>kimagecheck</artifactId>
   <version>Tag</version>
</dependency>
```
