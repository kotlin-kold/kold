package com.paralainer.kold.context

import com.paralainer.kold.validated.Validated
import com.paralainer.kold.validated.ValidatedField
import com.paralainer.kold.validated.Violation

fun <R, A> validateFields(v1: ValidatedField<A>, combine: (A) -> R): Validated<R> =
    collectViolations(v1).ifValid { combine(v1.v()) }

fun <R, A, B> validateFields(v1: ValidatedField<A>, v2: ValidatedField<B>, combine: (A, B) -> R): Validated<R> =
    collectViolations(v1, v2).ifValid { combine(v1.v(), v2.v()) }

fun <R, A, B, C> validateFields(
    v1: ValidatedField<A>,
    v2: ValidatedField<B>,
    v3: ValidatedField<C>,
    combine: (A, B, C) -> R
): Validated<R> =
    collectViolations(v1, v2, v3).ifValid { combine(v1.v(), v2.v(), v3.v()) }

fun <R, A, B, C, D> validateFields(
    v1: ValidatedField<A>,
    v2: ValidatedField<B>,
    v3: ValidatedField<C>,
    v4: ValidatedField<D>,
    combine: (A, B, C, D) -> R
): Validated<R> =
    collectViolations(v1, v2, v3, v4).ifValid { combine(v1.v(), v2.v(), v3.v(), v4.v()) }

fun <R, A, B, C, D, E> validateFields(
    v1: ValidatedField<A>,
    v2: ValidatedField<B>,
    v3: ValidatedField<C>,
    v4: ValidatedField<D>,
    v5: ValidatedField<E>,
    combine: (A, B, C, D, E) -> R
): Validated<R> =
    collectViolations(v1, v2, v3, v4, v5).ifValid { combine(v1.v(), v2.v(), v3.v(), v4.v(), v5.v()) }

fun <R, A, B, C, D, E, F> validateFields(
    v1: ValidatedField<A>,
    v2: ValidatedField<B>,
    v3: ValidatedField<C>,
    v4: ValidatedField<D>,
    v5: ValidatedField<E>,
    v6: ValidatedField<F>,
    combine: (A, B, C, D, E, F) -> R
): Validated<R> =
    collectViolations(v1, v2, v3, v4, v5, v6).ifValid { combine(v1.v(), v2.v(), v3.v(), v4.v(), v5.v(), v6.v()) }

fun <R, A, B, C, D, E, F, G> validateFields(
    v1: ValidatedField<A>,
    v2: ValidatedField<B>,
    v3: ValidatedField<C>,
    v4: ValidatedField<D>,
    v5: ValidatedField<E>,
    v6: ValidatedField<F>,
    v7: ValidatedField<G>,
    combine: (A, B, C, D, E, F, G) -> R
): Validated<R> =
    collectViolations(v1, v2, v3, v4, v5, v6, v7).ifValid { combine(v1.v(), v2.v(), v3.v(), v4.v(), v5.v(), v6.v(), v7.v()) }


private fun collectViolations(vararg validations: ValidatedField<*>): List<Violation> =
    validations.mapNotNull {
        when (val validated = it.value) {
            is Validated.Valid -> null
            is Validated.Invalid -> validated.violations
        }
    }.flatten()

private fun <R> List<Violation>.ifValid(block: () -> R): Validated<R> =
    if (this.isEmpty())
        Validated.Valid(block())
    else
        Validated.Invalid(this)

private fun <T> ValidatedField<T>.v(): T = (this.value as Validated.Valid<T>).value
