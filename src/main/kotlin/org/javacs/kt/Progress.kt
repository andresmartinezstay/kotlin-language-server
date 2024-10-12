package org.javacs.kt

import org.eclipse.lsp4j.ProgressParams
import org.eclipse.lsp4j.WorkDoneProgressBegin
import org.eclipse.lsp4j.WorkDoneProgressCreateParams
import org.eclipse.lsp4j.WorkDoneProgressEnd
import org.eclipse.lsp4j.WorkDoneProgressNotification
import org.eclipse.lsp4j.WorkDoneProgressReport
import org.eclipse.lsp4j.jsonrpc.messages.Either
import org.eclipse.lsp4j.services.LanguageClient
import java.io.Closeable
import java.util.UUID
import java.util.concurrent.CompletableFuture

/** A facility for emitting progress notifications. */
interface Progress : Closeable {
    /**
     * Updates the progress percentage. The
     * value should be in the range [0, 100].
     */
    fun update(message: String? = null, percent: Int? = null)

    object None : Progress {
        override fun update(message: String?, percent: Int?) {}

        override fun close() {}
    }

    interface Factory {
        /**
         * Creates a new progress listener with
         * the given label. The label is intended
         * to be human-readable.
         */
        fun create(label: String): CompletableFuture<Progress>

        object None : Factory {
            override fun create(label: String): CompletableFuture<Progress> = CompletableFuture.completedFuture(Progress.None)
        }
    }
}

class LanguageClientProgress(
    private val label: String,
    private val token: Either<String, Int>,
    private val client: LanguageClient
) : Progress {
    init {
        reportProgress(WorkDoneProgressBegin().also {
            it.title = "Kotlin: $label"
            it.percentage = 0
        })
    }

    override fun update(message: String?, percent: Int?) {
        reportProgress(WorkDoneProgressReport().also {
            it.message = message
            it.percentage = percent
        })
    }

    override fun close() {
        reportProgress(WorkDoneProgressEnd())
    }

    private fun reportProgress(notification: WorkDoneProgressNotification) {
        client.notifyProgress(ProgressParams(token, Either.forLeft(notification)))
    }

    class Factory(private val client: LanguageClient) : Progress.Factory {
        override fun create(label: String): CompletableFuture<Progress> {
            val token = Either.forLeft<String, Int>(UUID.randomUUID().toString())
            return client
                .createProgress(WorkDoneProgressCreateParams().also {
                    it.token = token
                })
                .thenApply { LanguageClientProgress(label, token, client) }
        }
    }
}