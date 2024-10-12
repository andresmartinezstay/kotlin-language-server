package org.javacs.kt.actions.formatting

import org.javacs.kt.Configuration
import com.facebook.ktfmt.format.Formatter as Ktfmt
import com.facebook.ktfmt.format.FormattingOptions as KtfmtOptions
import org.eclipse.lsp4j.FormattingOptions as LspFormattingOptions

class KtfmtFormatter(private val config: Configuration.Formatting.Ktfmt) : Formatter {
    override fun format(
        code: String,
        options: LspFormattingOptions,
    ): String {
        return Ktfmt.format(KtfmtOptions(
            maxWidth = config.maxWidth,
            blockIndent = options.tabSize.takeUnless { it == 0 } ?: config.indent,
            continuationIndent = config.continuationIndent,
            removeUnusedImports = config.removeUnusedImports,
        ), code)
    }
}

