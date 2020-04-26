package com.github.kold.examples

import com.github.kold.validated.Validated
import com.github.kold.validated.ValueViolation
import com.github.kold.validated.valid


class EmailAddress private constructor(val value: String) {
    companion object {
        @OptIn(ExperimentalStdlibApi::class)
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