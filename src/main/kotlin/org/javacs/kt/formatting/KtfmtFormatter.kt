package org.javacs.kt.formatting

import org.javacs.kt.KtfmtConfiguration
import com.facebook.ktfmt.format.Formatter as Ktfmt
import com.facebook.ktfmt.format.FormattingOptions as KtfmtOptions
import org.eclipse.lsp4j.FormattingOptions as LspFormattingOptions

class KtfmtFormatter(private val config: KtfmtConfiguration) : Formatter {
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

