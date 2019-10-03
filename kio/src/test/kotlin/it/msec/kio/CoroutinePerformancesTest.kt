package it.msec.kio

import it.msec.kio.result.get
import it.msec.kio.runtime.Runtime
import it.msec.kio.runtime.RuntimeSuspended
import it.msec.kio.runtime.RuntimeSuspended.unsafeRunSuspended
import kotlinx.coroutines.runBlocking
import org.junit.Ignore
import org.junit.Test

@Ignore
class CoroutinePerformancesTest {

    @Test
    fun `performance comparison when inside coroutine`() = runBlocking {

        val program = loop(32)

        for (i in 1..5) Runtime.unsafeRunSyncAndGet(program)
        for (i in 1..5) unsafeRunSuspended(program, Unit).get()

        val coroutineStart = System.currentTimeMillis()
        unsafeRunSuspended(program, Unit).get()
        val coroutineStop = System.currentTimeMillis()

        val noCoroutineStart = System.currentTimeMillis()
        Runtime.unsafeRunSyncAndGet(program)
        val noCoroutineStop = System.currentTimeMillis()

        println(noCoroutineStop - noCoroutineStart)
        println(coroutineStop - coroutineStart)
    }

    @Test
    fun `performance comparison when outside coroutine`() {

        val program = loop(32)

        for (i in 1..5) Runtime.unsafeRunSyncAndGet(program)
        for (i in 1..5) RuntimeSuspended.unsafeRunSyncAndGet(program)

        val coroutineStart = System.currentTimeMillis()
        RuntimeSuspended.unsafeRunSyncAndGet(program)
        val coroutineStop = System.currentTimeMillis()

        val noCoroutineStart = System.currentTimeMillis()
        Runtime.unsafeRunSyncAndGet(program)
        val noCoroutineStop = System.currentTimeMillis()

        println(noCoroutineStop - noCoroutineStart)
        println(coroutineStop - coroutineStart)
    }


    private fun loop(n: Int): UIO<Int> =
            if (n <= 1) effect { n }
            else loop(n - 1).flatMap { a ->
                loop(n - 2).flatMap { b -> effect { a + b } }
            }
}
