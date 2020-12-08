package com.github.kold.context

import com.github.kold.validated.*

fun <R, A> combineFields(v1: ValidatedField<A>, combine: (A) -> R): Validated<R> =
    collectViolations(v1).ifValid { combine(v1.v()) }

fun <R, A, B> combineFields(v1: ValidatedField<A>, v2: ValidatedField<B>, combine: (A, B) -> R): Validated<R> =
    collectViolations(v1, v2).ifValid { combine(v1.v(), v2.v()) }

fun <R, A, B, C> combineFields(
    v1: ValidatedField<A>,
    v2: ValidatedField<B>,
    v3: ValidatedField<C>,
    combine: (A, B, C) -> R
): Validated<R> =
    collectViolations(v1, v2, v3).ifValid { combine(v1.v(), v2.v(), v3.v()) }

fun <R, A, B, C, D> combineFields(
    v1: ValidatedField<A>,
    v2: ValidatedField<B>,
    v3: ValidatedField<C>,
    v4: ValidatedField<D>,
    combine: (A, B, C, D) -> R
): Validated<R> =
    collectViolations(v1, v2, v3, v4).ifValid { combine(v1.v(), v2.v(), v3.v(), v4.v()) }

fun <R, A, B, C, D, E> combineFields(
    v1: ValidatedField<A>,
    v2: ValidatedField<B>,
    v3: ValidatedField<C>,
    v4: ValidatedField<D>,
    v5: ValidatedField<E>,
    combine: (A, B, C, D, E) -> R
): Validated<R> =
    collectViolations(v1, v2, v3, v4, v5).ifValid { combine(v1.v(), v2.v(), v3.v(), v4.v(), v5.v()) }

fun <R, A, B, C, D, E, F> combineFields(
    v1: ValidatedField<A>,
    v2: ValidatedField<B>,
    v3: ValidatedField<C>,
    v4: ValidatedField<D>,
    v5: ValidatedField<E>,
    v6: ValidatedField<F>,
    combine: (A, B, C, D, E, F) -> R
): Validated<R> =
    collectViolations(v1, v2, v3, v4, v5, v6).ifValid { combine(v1.v(), v2.v(), v3.v(), v4.v(), v5.v(), v6.v()) }

fun <R, A, B, C, D, E, F, G> combineFields(
    v1: ValidatedField<A>,
    v2: ValidatedField<B>,
    v3: ValidatedField<C>,
    v4: ValidatedField<D>,
    v5: ValidatedField<E>,
    v6: ValidatedField<F>,
    v7: ValidatedField<G>,
    combine: (A, B, C, D, E, F, G) -> R
): Validated<R> =
    collectViolations(v1, v2, v3, v4, v5, v6, v7).ifValid {
        combine(
            v1.v(),
            v2.v(),
            v3.v(),
            v4.v(),
            v5.v(),
            v6.v(),
            v7.v()
        )
    }

fun <R, A, B, C, D, E, F, G, H> combineFields(
    v1: ValidatedField<A>,
    v2: ValidatedField<B>,
    v3: ValidatedField<C>,
    v4: ValidatedField<D>,
    v5: ValidatedField<E>,
    v6: ValidatedField<F>,
    v7: ValidatedField<G>,
    v8: ValidatedField<H>,
    combine: (A, B, C, D, E, F, G, H) -> R
): Validated<R> =
    collectViolations(v1, v2, v3, v4, v5, v6, v7, v8).ifValid {
        combine(
            v1.v(),
            v2.v(),
            v3.v(),
            v4.v(),
            v5.v(),
            v6.v(),
            v7.v(),
            v8.v()
        )
    }

fun <R, A, B, C, D, E, F, G, H, I> combineFields(
    v1: ValidatedField<A>,
    v2: ValidatedField<B>,
    v3: ValidatedField<C>,
    v4: ValidatedField<D>,
    v5: ValidatedField<E>,
    v6: ValidatedField<F>,
    v7: ValidatedField<G>,
    v8: ValidatedField<H>,
    v9: ValidatedField<I>,
    combine: (A, B, C, D, E, F, G, H, I) -> R
): Validated<R> =
    collectViolations(v1, v2, v3, v4, v5, v6, v7, v8, v9).ifValid {
        combine(
            v1.v(),
            v2.v(),
            v3.v(),
            v4.v(),
            v5.v(),
            v6.v(),
            v7.v(),
            v8.v(),
            v9.v()
        )
    }

fun <R, A, B, C, D, E, F, G, H, I, J> combineFields(
    v1: ValidatedField<A>,
    v2: ValidatedField<B>,
    v3: ValidatedField<C>,
    v4: ValidatedField<D>,
    v5: ValidatedField<E>,
    v6: ValidatedField<F>,
    v7: ValidatedField<G>,
    v8: ValidatedField<H>,
    v9: ValidatedField<I>,
    v10: ValidatedField<J>,
    combine: (A, B, C, D, E, F, G, H, I, J) -> R
): Validated<R> =
    collectViolations(v1, v2, v3, v4, v5, v6, v7, v8, v9, v10).ifValid {
        combine(
            v1.v(),
            v2.v(),
            v3.v(),
            v4.v(),
            v5.v(),
            v6.v(),
            v7.v(),
            v8.v(),
            v9.v(),
            v10.v()
        )
    }

fun <R, A, B, C, D, E, F, G, H, I, J, K> combineFields(
    v1: ValidatedField<A>,
    v2: ValidatedField<B>,
    v3: ValidatedField<C>,
    v4: ValidatedField<D>,
    v5: ValidatedField<E>,
    v6: ValidatedField<F>,
    v7: ValidatedField<G>,
    v8: ValidatedField<H>,
    v9: ValidatedField<I>,
    v10: ValidatedField<J>,
    v11: ValidatedField<K>,
    combine: (A, B, C, D, E, F, G, H, I, J, K) -> R
): Validated<R> =
    collectViolations(v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11).ifValid {
        combine(
            v1.v(),
            v2.v(),
            v3.v(),
            v4.v(),
            v5.v(),
            v6.v(),
            v7.v(),
            v8.v(),
            v9.v(),
            v10.v(),
            v11.v()
        )
    }

fun <R, A, B, C, D, E, F, G, H, I, J, K, L> combineFields(
    v1: ValidatedField<A>,
    v2: ValidatedField<B>,
    v3: ValidatedField<C>,
    v4: ValidatedField<D>,
    v5: ValidatedField<E>,
    v6: ValidatedField<F>,
    v7: ValidatedField<G>,
    v8: ValidatedField<H>,
    v9: ValidatedField<I>,
    v10: ValidatedField<J>,
    v11: ValidatedField<K>,
    v12: ValidatedField<L>,
    combine: (A, B, C, D, E, F, G, H, I, J, K, L) -> R
): Validated<R> =
    collectViolations(v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12).ifValid {
        combine(
            v1.v(),
            v2.v(),
            v3.v(),
            v4.v(),
            v5.v(),
            v6.v(),
            v7.v(),
            v8.v(),
            v9.v(),
            v10.v(),
            v11.v(),
            v12.v()
        )
    }

fun <R, A, B, C, D, E, F, G, H, I, J, K, L, M> combineFields(
    v1: ValidatedField<A>,
    v2: ValidatedField<B>,
    v3: ValidatedField<C>,
    v4: ValidatedField<D>,
    v5: ValidatedField<E>,
    v6: ValidatedField<F>,
    v7: ValidatedField<G>,
    v8: ValidatedField<H>,
    v9: ValidatedField<I>,
    v10: ValidatedField<J>,
    v11: ValidatedField<K>,
    v12: ValidatedField<L>,
    v13: ValidatedField<M>,
    combine: (A, B, C, D, E, F, G, H, I, J, K, L, M) -> R
): Validated<R> =
    collectViolations(v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13).ifValid {
        combine(
            v1.v(),
            v2.v(),
            v3.v(),
            v4.v(),
            v5.v(),
            v6.v(),
            v7.v(),
            v8.v(),
            v9.v(),
            v10.v(),
            v11.v(),
            v12.v(),
            v13.v()
        )
    }

fun <R, A, B, C, D, E, F, G, H, I, J, K, L, M, N> combineFields(
    v1: ValidatedField<A>,
    v2: ValidatedField<B>,
    v3: ValidatedField<C>,
    v4: ValidatedField<D>,
    v5: ValidatedField<E>,
    v6: ValidatedField<F>,
    v7: ValidatedField<G>,
    v8: ValidatedField<H>,
    v9: ValidatedField<I>,
    v10: ValidatedField<J>,
    v11: ValidatedField<K>,
    v12: ValidatedField<L>,
    v13: ValidatedField<M>,
    v14: ValidatedField<N>,
    combine: (A, B, C, D, E, F, G, H, I, J, K, L, M, N) -> R
): Validated<R> =
    collectViolations(v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14).ifValid {
        combine(
            v1.v(),
            v2.v(),
            v3.v(),
            v4.v(),
            v5.v(),
            v6.v(),
            v7.v(),
            v8.v(),
            v9.v(),
            v10.v(),
            v11.v(),
            v12.v(),
            v13.v(),
            v14.v()
        )
    }

fun <R, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O> combineFields(
    v1: ValidatedField<A>,
    v2: ValidatedField<B>,
    v3: ValidatedField<C>,
    v4: ValidatedField<D>,
    v5: ValidatedField<E>,
    v6: ValidatedField<F>,
    v7: ValidatedField<G>,
    v8: ValidatedField<H>,
    v9: ValidatedField<I>,
    v10: ValidatedField<J>,
    v11: ValidatedField<K>,
    v12: ValidatedField<L>,
    v13: ValidatedField<M>,
    v14: ValidatedField<N>,
    v15: ValidatedField<O>,
    combine: (A, B, C, D, E, F, G, H, I, J, K, L, M, N, O) -> R
): Validated<R> =
    collectViolations(v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14, v15).ifValid {
        combine(
            v1.v(),
            v2.v(),
            v3.v(),
            v4.v(),
            v5.v(),
            v6.v(),
            v7.v(),
            v8.v(),
            v9.v(),
            v10.v(),
            v11.v(),
            v12.v(),
            v13.v(),
            v14.v(),
            v15.v()
        )
    }

fun <R, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P> combineFields(
    v1: ValidatedField<A>,
    v2: ValidatedField<B>,
    v3: ValidatedField<C>,
    v4: ValidatedField<D>,
    v5: ValidatedField<E>,
    v6: ValidatedField<F>,
    v7: ValidatedField<G>,
    v8: ValidatedField<H>,
    v9: ValidatedField<I>,
    v10: ValidatedField<J>,
    v11: ValidatedField<K>,
    v12: ValidatedField<L>,
    v13: ValidatedField<M>,
    v14: ValidatedField<N>,
    v15: ValidatedField<O>,
    v16: ValidatedField<P>,
    combine: (A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P) -> R
): Validated<R> =
    collectViolations(v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14, v15, v16).ifValid {
        combine(
            v1.v(),
            v2.v(),
            v3.v(),
            v4.v(),
            v5.v(),
            v6.v(),
            v7.v(),
            v8.v(),
            v9.v(),
            v10.v(),
            v11.v(),
            v12.v(),
            v13.v(),
            v14.v(),
            v15.v(),
            v16.v()
        )
    }

fun <R, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q> combineFields(
    v1: ValidatedField<A>,
    v2: ValidatedField<B>,
    v3: ValidatedField<C>,
    v4: ValidatedField<D>,
    v5: ValidatedField<E>,
    v6: ValidatedField<F>,
    v7: ValidatedField<G>,
    v8: ValidatedField<H>,
    v9: ValidatedField<I>,
    v10: ValidatedField<J>,
    v11: ValidatedField<K>,
    v12: ValidatedField<L>,
    v13: ValidatedField<M>,
    v14: ValidatedField<N>,
    v15: ValidatedField<O>,
    v16: ValidatedField<P>,
    v17: ValidatedField<Q>,
    combine: (A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q) -> R
): Validated<R> =
    collectViolations(v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14, v15, v16, v17).ifValid {
        combine(
            v1.v(),
            v2.v(),
            v3.v(),
            v4.v(),
            v5.v(),
            v6.v(),
            v7.v(),
            v8.v(),
            v9.v(),
            v10.v(),
            v11.v(),
            v12.v(),
            v13.v(),
            v14.v(),
            v15.v(),
            v16.v(),
            v17.v()
        )
    }

fun <RE, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R> combineFields(
    v1: ValidatedField<A>,
    v2: ValidatedField<B>,
    v3: ValidatedField<C>,
    v4: ValidatedField<D>,
    v5: ValidatedField<E>,
    v6: ValidatedField<F>,
    v7: ValidatedField<G>,
    v8: ValidatedField<H>,
    v9: ValidatedField<I>,
    v10: ValidatedField<J>,
    v11: ValidatedField<K>,
    v12: ValidatedField<L>,
    v13: ValidatedField<M>,
    v14: ValidatedField<N>,
    v15: ValidatedField<O>,
    v16: ValidatedField<P>,
    v17: ValidatedField<Q>,
    v18: ValidatedField<R>,
    combine: (A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R) -> RE
): Validated<RE> =
    collectViolations(v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14, v15, v16, v17, v18).ifValid {
        combine(
            v1.v(),
            v2.v(),
            v3.v(),
            v4.v(),
            v5.v(),
            v6.v(),
            v7.v(),
            v8.v(),
            v9.v(),
            v10.v(),
            v11.v(),
            v12.v(),
            v13.v(),
            v14.v(),
            v15.v(),
            v16.v(),
            v17.v(),
            v18.v()
        )
    }

fun <RE, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S> combineFields(
    v1: ValidatedField<A>,
    v2: ValidatedField<B>,
    v3: ValidatedField<C>,
    v4: ValidatedField<D>,
    v5: ValidatedField<E>,
    v6: ValidatedField<F>,
    v7: ValidatedField<G>,
    v8: ValidatedField<H>,
    v9: ValidatedField<I>,
    v10: ValidatedField<J>,
    v11: ValidatedField<K>,
    v12: ValidatedField<L>,
    v13: ValidatedField<M>,
    v14: ValidatedField<N>,
    v15: ValidatedField<O>,
    v16: ValidatedField<P>,
    v17: ValidatedField<Q>,
    v18: ValidatedField<R>,
    v19: ValidatedField<S>,
    combine: (A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S) -> RE
): Validated<RE> =
    collectViolations(v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14, v15, v16, v17, v18, v19).ifValid {
        combine(
            v1.v(),
            v2.v(),
            v3.v(),
            v4.v(),
            v5.v(),
            v6.v(),
            v7.v(),
            v8.v(),
            v9.v(),
            v10.v(),
            v11.v(),
            v12.v(),
            v13.v(),
            v14.v(),
            v15.v(),
            v16.v(),
            v17.v(),
            v18.v(),
            v19.v()
        )
    }

fun <RE, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T> combineFields(
    v1: ValidatedField<A>,
    v2: ValidatedField<B>,
    v3: ValidatedField<C>,
    v4: ValidatedField<D>,
    v5: ValidatedField<E>,
    v6: ValidatedField<F>,
    v7: ValidatedField<G>,
    v8: ValidatedField<H>,
    v9: ValidatedField<I>,
    v10: ValidatedField<J>,
    v11: ValidatedField<K>,
    v12: ValidatedField<L>,
    v13: ValidatedField<M>,
    v14: ValidatedField<N>,
    v15: ValidatedField<O>,
    v16: ValidatedField<P>,
    v17: ValidatedField<Q>,
    v18: ValidatedField<R>,
    v19: ValidatedField<S>,
    v20: ValidatedField<T>,
    combine: (A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T) -> RE
): Validated<RE> =
    collectViolations(v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14, v15, v16, v17, v18, v19, v20).ifValid {
        combine(
            v1.v(),
            v2.v(),
            v3.v(),
            v4.v(),
            v5.v(),
            v6.v(),
            v7.v(),
            v8.v(),
            v9.v(),
            v10.v(),
            v11.v(),
            v12.v(),
            v13.v(),
            v14.v(),
            v15.v(),
            v16.v(),
            v17.v(),
            v18.v(),
            v19.v(),
            v20.v()
        )
    }


private fun collectViolations(vararg validations: ValidatedField<*>): List<FieldViolation> =
    validations.mapNotNull { field ->
        field.fold(
            onInvalid = { it.violation },
            onValid = { null }
        )
    }

private fun <R> List<Violation>.ifValid(block: () -> R): Validated<R> =
    if (this.isEmpty())
        Validated.Valid(block())
    else
        Validated.Invalid(this)

private fun <T> ValidatedField<T>.v(): T = (this as ValidField<T>).value
