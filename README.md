# fable

**fable** is a library designed for creating server-side skills. It provides a simple skill API, along with a skill
registry.

> **Note:** More detailed information will be added later.

## Gradle Setup

### Groovy

```groovy
repositories {
    maven { url 'https://api.modrinth.com/maven' }
}

dependencies {
    modImplementation 'maven.modrinth:fable-api:FABLE_VERSION'
}
```

### Kotlin DSL

```kotlin
repositories {
    maven("https://api.modrinth.com/maven")
}

dependencies {
    modImplementation("maven.modrinth:fable-api:FABLE_VERSION")
}
```