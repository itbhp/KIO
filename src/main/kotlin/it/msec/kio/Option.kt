package it.msec.kio

import it.msec.kio.utils.nullable.then

object Empty

fun <A> empty(): Option<A> = failure(Empty)

fun <A> Option<A>.filter(f: (A) -> Boolean): Option<A> =
        flatMap { a -> if (f(a)) just(a) else failure(Empty) }

fun <A> A?.toOption(): Option<A> = then(::just) ?: empty()

fun <E, A> KIO<Any, E, A>.toOption(): Option<A> = mapError { Empty }
