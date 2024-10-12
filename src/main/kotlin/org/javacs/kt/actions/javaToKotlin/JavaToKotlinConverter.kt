package org.javacs.kt.actions.javaToKotlin

import com.intellij.lang.java.JavaLanguage
// import org.jetbrains.kotlin.j2k.JavaToKotlinTranslator
import org.javacs.kt.LOG
import org.javacs.kt.Compiler
import org.javacs.kt.CompilationKind

fun convertJavaToKotlin(javaCode: String, compiler: Compiler): String {
    val psiFactory = compiler.psiFileFactoryFor(CompilationKind.DEFAULT)
    val javaAST = psiFactory.createFileFromText("snippet.java", JavaLanguage.INSTANCE, javaCode)
    LOG.info("Parsed {} to {}", javaCode, javaAST)

	return JavaElementConverter().also(javaAST::accept).translatedKotlinCode ?: run {
        LOG.warn("Could not translate code")
        ""
    }
}
