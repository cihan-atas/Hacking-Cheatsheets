package com.hackingcheatsheets.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hackingcheatsheets.app.ui.theme.BorderDark
import com.hackingcheatsheets.app.ui.theme.CodeBlockBg
import com.hackingcheatsheets.app.ui.theme.CyberGreen
import com.hackingcheatsheets.app.ui.theme.SurfaceDark
import com.hackingcheatsheets.app.ui.theme.SurfaceVariantDark
import com.hackingcheatsheets.app.ui.theme.TerminalCyan
import com.hackingcheatsheets.app.ui.theme.TextPrimary
import com.hackingcheatsheets.app.ui.theme.TextSecondary

@Composable
fun MarkdownView(
    markdown: String,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        val lines = markdown.lines()
        var inCodeBlock = false
        var codeLang = "bash"
        val codeBuffer = StringBuilder()

        var i = 0
        while (i < lines.size) {
            val line = lines[i]

            // Code block handling
            if (line.trim().startsWith("```")) {
                if (inCodeBlock) {
                    // Close code block
                    CodeBlockView(
                        code = codeBuffer.toString(),
                        language = codeLang
                    )
                    codeBuffer.clear()
                    inCodeBlock = false
                } else {
                    // Start code block
                    codeLang = line.trim().removePrefix("```").trim().ifEmpty { "bash" }
                    inCodeBlock = true
                }
                i++
                continue
            }

            if (inCodeBlock) {
                codeBuffer.appendLine(line)
                i++
                continue
            }

            // Headings
            when {
                line.startsWith("# ") -> {
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = line.removePrefix("# ").trim(),
                        style = MaterialTheme.typography.headlineLarge,
                        color = CyberGreen
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Divider(color = BorderDark, thickness = 1.dp)
                    Spacer(modifier = Modifier.height(8.dp))
                }
                line.startsWith("## ") -> {
                    Spacer(modifier = Modifier.height(14.dp))
                    Text(
                        text = line.removePrefix("## ").trim(),
                        style = MaterialTheme.typography.headlineMedium,
                        color = TerminalCyan
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Divider(color = BorderDark, thickness = 0.5.dp)
                    Spacer(modifier = Modifier.height(6.dp))
                }
                line.startsWith("### ") -> {
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = line.removePrefix("### ").trim(),
                        style = MaterialTheme.typography.titleLarge,
                        color = TextPrimary
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                }
                line.startsWith("#### ") -> {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = line.removePrefix("#### ").trim(),
                        style = MaterialTheme.typography.titleMedium,
                        color = TextSecondary
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                }
                line.trim().startsWith("- ") || line.trim().startsWith("* ") -> {
                    val bulletText = line.trim().substring(2)
                    Row(modifier = Modifier.padding(vertical = 2.dp, horizontal = 4.dp)) {
                        Text(text = "• ", color = CyberGreen, fontWeight = FontWeight.Bold)
                        Text(
                            text = parseFormattedText(bulletText),
                            style = MaterialTheme.typography.bodyMedium,
                            color = TextPrimary
                        )
                    }
                }
                line.trim().startsWith(">") -> {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                            .background(SurfaceVariantDark, RoundedCornerShape(4.dp))
                            .border(1.dp, BorderDark, RoundedCornerShape(4.dp))
                            .padding(8.dp)
                    ) {
                        Text(
                            text = parseFormattedText(line.trim().removePrefix(">").trim()),
                            color = TextSecondary,
                            fontSize = 13.sp,
                            lineHeight = 18.sp
                        )
                    }
                }
                line.trim().startsWith("|") && line.contains("-|-") -> {
                    // Markdown table separator line, ignore
                }
                line.trim().startsWith("|") -> {
                    // Simple table row
                    val cells = line.split("|").filter { it.isNotBlank() }.map { it.trim() }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState())
                            .background(if (i % 2 == 0) SurfaceDark else SurfaceVariantDark)
                            .padding(horizontal = 8.dp, vertical = 6.dp)
                    ) {
                        cells.forEach { cell ->
                            Text(
                                text = parseFormattedText(cell),
                                modifier = Modifier.padding(end = 16.dp),
                                fontSize = 12.sp,
                                color = TextPrimary
                            )
                        }
                    }
                }
                line.isNotBlank() -> {
                    Text(
                        text = parseFormattedText(line),
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(vertical = 3.dp)
                    )
                }
                else -> {
                    Spacer(modifier = Modifier.height(4.dp))
                }
            }
            i++
        }
    }
}

private fun parseFormattedText(raw: String): androidx.compose.ui.text.AnnotatedString {
    return buildAnnotatedString {
        var cursor = 0
        val pattern = reInline.findAll(raw)
        for (match in pattern) {
            val start = match.range.first
            val end = match.range.last + 1
            if (start > cursor) {
                append(raw.substring(cursor, start))
            }
            val token = match.value
            when {
                token.startsWith("**") && token.endsWith("**") -> {
                    withStyle(SpanStyle(fontWeight = FontWeight.Bold, color = TextPrimary)) {
                        append(token.removeSurrounding("**"))
                    }
                }
                token.startsWith("`") && token.endsWith("`") -> {
                    withStyle(
                        SpanStyle(
                            fontFamily = FontFamily.Monospace,
                            background = CodeBlockBg,
                            color = CyberGreen
                        )
                    ) {
                        append(" ${token.removeSurrounding("`")} ")
                    }
                }
                else -> append(token)
            }
            cursor = end
        }
        if (cursor < raw.length) {
            append(raw.substring(cursor))
        }
    }
}

private val reInline = Regex("(\\*\\*.*?\\*\\*|`.*?`)")
