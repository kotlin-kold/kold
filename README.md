**Kold** is a type-safe validation library for Kotlin.
### What's it for
This library is for validating raw data _before_ constructing an object.

### When to use it
There are a couple of use-cases
* Validating value-objects like `EmailAddress`
* Validating fields of on object

#### Validating value-objects
Imagine you have some values that should follow certain rules to be valid like email address.

We create a wrapper-types for such values, making constructor private and allowing to construct such wrappers only via factory methods which return instance of `Validated`.

```$kotlin
class EmailAddress private constructor(val value: String) {
    companion object {
        fun fromString(email: String): Validated<EmailAddress> {
            val violations = buildList {
                if (email.length > 2048) {
                    add(ValueViolation("value.too.long", "Email address can't be longer than 2048 characters"))
                }

                if (!email.contains("@")) {
                    add(ValueViolation("value.invalid", "Email address is invalid format"))
                }
            }

            return if (violations.isEmpty()) {
                Validated.Valid(EmailAddress(email))
            } else {
                Validated.Invalid(violations)
            }
        }
    }
}
```

#### Validating fields of an object
We want to validate a complex object that contains a few fields, we get data from an API in JSON-like format.
We convert raw data to `KoldData`, there are helper functions for that. Then we validate that data and convert to a desired class.
```kotlin
data class User(
        val name: String,
        val password: String?,
        val age: Int,
        val score: Double?
)

fun validateUser(input: KoldData): Validated<User> =
        input.validationContext {
            combineFields(
                require("name").validateValue { it.string() },
                opt("password").validateOption { it.string() },
                require("age").validateValue { it.long() },
                opt("score").validateOption { it.double() }
            ) { name, password, age, score ->
                User(name, password, age.toInt(), score)
            }
        }
```
More examples can be found in `kold-examples` module.

### KoldData
KoldData is a type safe representation of json-like data. It allows you to store strings, numbers, booleans and also arrays and objects containing fields of such types. 
