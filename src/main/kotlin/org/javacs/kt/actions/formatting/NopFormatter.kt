package org.javacs.kt.actions.formatting

import org.eclipse.lsp4j.FormattingOptions as LspFormattingOptions

object NopFormatter : Formatter {
    override fun format(code: String, options: LspFormattingOptions): String = code
}
