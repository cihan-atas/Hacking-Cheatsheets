package com.hackingcheatsheets.app.ui.components

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
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

data class BadgeInfo(
    val label: String,
    val value: String,
    val colorName: String
)

sealed class MarkdownBlock {
    data class AsciiArt(val art: String) : MarkdownBlock()
    data class Badges(val badges: List<BadgeInfo>) : MarkdownBlock()
    data class Header(val level: Int, val text: String, val anchor: String) : MarkdownBlock()
    data class Code(val language: String, val code: String) : MarkdownBlock()
    data class Bullet(val text: String, val indentLevel: Int = 0) : MarkdownBlock()
    data class Table(val headers: List<String>, val rows: List<List<String>>) : MarkdownBlock()
    data class Quote(val text: String) : MarkdownBlock()
    data class Paragraph(val text: String) : MarkdownBlock()
    object Divider : MarkdownBlock()
}

fun normalizeAnchor(input: String): String {
    return input
        .replace("İ", "i").replace("I", "i").replace("ı", "i")
        .replace("ç", "c").replace("Ç", "c")
        .replace("ğ", "g").replace("Ğ", "g")
        .replace("ö", "o").replace("Ö", "o")
        .replace("ş", "s").replace("Ş", "s")
        .replace("ü", "u").replace("Ü", "u")
        .lowercase()
        .removePrefix("#")
        .replace(Regex("[^a-z0-9\\s-]"), "")
        .trim()
        .replace(Regex("\\s+"), "-")
        .trim('-')
}

fun parseMarkdown(markdown: String): List<MarkdownBlock> {
    val blocks = mutableListOf<MarkdownBlock>()
    val lines = markdown.lines()
    var i = 0
    var inCode = false
    var codeLang = "bash"
    val codeLines = mutableListOf<String>()

    val shieldRegex = Regex("""shields\.io/badge/([^-]+)-([^-?]+)(?:-([a-zA-Z0-9_]+))?""")
    val altRegex = Regex("""alt="([^"]+)"""")

    while (i < lines.size) {
        val line = lines[i]
        val trimmed = line.trim()

        // Code block start / end
        if (trimmed.startsWith("```")) {
            if (inCode) {
                val codeStr = codeLines.joinToString("\n")
                if (codeStr.any { it in listOf('█', '═', '╔', '╗', '║', '╚', '╝') }) {
                    blocks.add(MarkdownBlock.AsciiArt(codeStr))
                } else {
                    blocks.add(MarkdownBlock.Code(language = codeLang, code = codeStr))
                }
                codeLines.clear()
                inCode = false
            } else {
                inCode = true
                codeLang = trimmed.removePrefix("```").trim().ifEmpty { "bash" }
            }
            i++
            continue
        }

        if (inCode) {
            codeLines.add(line)
            i++
            continue
        }

        // Shields & badges detection (lines with shields.io or <img ...>)
        if (trimmed.contains("shields.io/badge/") || (trimmed.contains("<img") && trimmed.contains("badge"))) {
            val badgeList = mutableListOf<BadgeInfo>()
            while (i < lines.size && (lines[i].contains("shields.io") || lines[i].contains("<img") || lines[i].contains("<p") || lines[i].contains("</p>"))) {
                val currentLine = lines[i]
                for (m in shieldRegex.findAll(currentLine)) {
                    val label = m.groupValues[1].replace("_", " ").replace("%20", " ")
                    val value = m.groupValues[2].replace("_", " ").replace("%20", " ")
                    val col = m.groupValues.getOrNull(3)?.ifBlank { "blue" } ?: "blue"
                    badgeList.add(BadgeInfo(label, value, col))
                }
                if (badgeList.isEmpty()) {
                    for (m in altRegex.findAll(currentLine)) {
                        val altText = m.groupValues[1]
                        if (altText.isNotBlank()) {
                            badgeList.add(BadgeInfo(altText, "", "blue"))
                        }
                    }
                }
                i++
            }
            if (badgeList.isNotEmpty()) {
                blocks.add(MarkdownBlock.Badges(badgeList))
            }
            continue
        }

        // Headings
        if (trimmed.startsWith("# ") || trimmed.startsWith("## ") || trimmed.startsWith("### ") || trimmed.startsWith("#### ")) {
            val level = trimmed.takeWhile { it == '#' }.length
            val hText = trimmed.removePrefix("#".repeat(level)).trim()
            blocks.add(MarkdownBlock.Header(level = level, text = hText, anchor = normalizeAnchor(hText)))
            i++
            continue
        }

        // Bullets
        if (trimmed.startsWith("- ") || trimmed.startsWith("* ")) {
            val indent = (line.length - line.trimStart().length) / 2
            val text = trimmed.substring(2).trim()
            blocks.add(MarkdownBlock.Bullet(text = text, indentLevel = indent))
            i++
            continue
        }

        // Blockquotes
        if (trimmed.startsWith(">")) {
            val quoteLines = mutableListOf<String>()
            while (i < lines.size && lines[i].trim().startsWith(">")) {
                quoteLines.add(lines[i].trim().removePrefix(">").trim())
                i++
            }
            blocks.add(MarkdownBlock.Quote(quoteLines.joinToString(" ")))
            continue
        }

        // Tables
        if (trimmed.startsWith("|")) {
            val tableLines = mutableListOf<String>()
            while (i < lines.size && lines[i].trim().startsWith("|")) {
                tableLines.add(lines[i].trim())
                i++
            }
            if (tableLines.size >= 2) {
                val headers = tableLines[0].split("|").filter { it.isNotBlank() }.map { it.trim() }
                val rows = mutableListOf<List<String>>()
                for (rowIdx in 1 until tableLines.size) {
                    val rLine = tableLines[rowIdx]
                    if (rLine.contains("-|-") || rLine.contains("---")) continue
                    val cells = rLine.split("|").filter { it.isNotBlank() }.map { it.trim() }
                    if (cells.isNotEmpty()) {
                        rows.add(cells)
                    }
                }
                blocks.add(MarkdownBlock.Table(headers = headers, rows = rows))
            }
            continue
        }

        // Divider
        if (trimmed == "---" || trimmed == "***" || trimmed == "___") {
            blocks.add(MarkdownBlock.Divider)
            i++
            continue
        }

        // Normal paragraph
        if (trimmed.isNotBlank()) {
            val clean = trimmed
                .replace(Regex("""<p[^>]*>"""), "")
                .replace("</p>", "")
                .replace("<br>", "\n")
                .replace("<br/>", "\n")
                .replace("<br />", "\n")
                .trim()
            if (clean.isNotBlank()) {
                blocks.add(MarkdownBlock.Paragraph(clean))
            }
        }
        i++
    }

    return blocks
}

@Composable
fun MarkdownBlockView(
    block: MarkdownBlock,
    onLinkClick: (String) -> Unit
) {
    when (block) {
        is MarkdownBlock.AsciiArt -> {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(SurfaceDark)
                    .border(1.dp, BorderDark, RoundedCornerShape(8.dp))
                    .horizontalScroll(rememberScrollState())
                    .padding(10.dp)
            ) {
                Text(
                    text = block.art,
                    fontFamily = FontFamily.Monospace,
                    fontSize = 8.sp,
                    lineHeight = 10.sp,
                    color = CyberGreen
                )
            }
        }

        is MarkdownBlock.Badges -> {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                block.badges.forEach { badge ->
                    BadgeView(badge = badge)
                }
            }
        }

        is MarkdownBlock.Header -> {
            when (block.level) {
                1 -> {
                    Spacer(modifier = Modifier.height(18.dp))
                    Text(
                        text = block.text,
                        style = MaterialTheme.typography.headlineLarge,
                        color = CyberGreen
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    HorizontalDivider(color = BorderDark, thickness = 1.dp)
                    Spacer(modifier = Modifier.height(8.dp))
                }
                2 -> {
                    Spacer(modifier = Modifier.height(14.dp))
                    Text(
                        text = block.text,
                        style = MaterialTheme.typography.headlineMedium,
                        color = TerminalCyan
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    HorizontalDivider(color = BorderDark, thickness = 0.5.dp)
                    Spacer(modifier = Modifier.height(6.dp))
                }
                3 -> {
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = block.text,
                        style = MaterialTheme.typography.titleLarge,
                        color = TextPrimary
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                }
                else -> {
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = block.text,
                        style = MaterialTheme.typography.titleMedium,
                        color = TextSecondary
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                }
            }
        }

        is MarkdownBlock.Code -> {
            CodeBlockView(
                code = block.code,
                language = block.language
            )
        }

        is MarkdownBlock.Bullet -> {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = (block.indentLevel * 16).dp,
                        top = 2.dp,
                        bottom = 2.dp
                    )
            ) {
                Text(
                    text = "• ",
                    color = CyberGreen,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
                ClickableMarkdownText(
                    text = block.text,
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextPrimary,
                    onLinkClick = onLinkClick
                )
            }
        }

        is MarkdownBlock.Quote -> {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp)
                    .clip(RoundedCornerShape(6.dp))
                    .background(SurfaceVariantDark)
                    .border(1.dp, BorderDark, RoundedCornerShape(6.dp))
                    .padding(10.dp)
            ) {
                ClickableMarkdownText(
                    text = block.text,
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextSecondary,
                    onLinkClick = onLinkClick
                )
            }
        }

        is MarkdownBlock.Table -> {
            TableView(headers = block.headers, rows = block.rows, onLinkClick = onLinkClick)
        }

        is MarkdownBlock.Paragraph -> {
            ClickableMarkdownText(
                text = block.text,
                style = MaterialTheme.typography.bodyLarge,
                color = TextPrimary,
                modifier = Modifier.padding(vertical = 3.dp),
                onLinkClick = onLinkClick
            )
        }

        is MarkdownBlock.Divider -> {
            Spacer(modifier = Modifier.height(8.dp))
            HorizontalDivider(color = BorderDark, thickness = 1.dp)
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
fun BadgeView(badge: BadgeInfo) {
    val badgeColor = when (badge.colorName.lowercase()) {
        "blue" -> Color(0xFF2196F3)
        "green" -> Color(0xFF4CAF50)
        "orange" -> Color(0xFFFF9800)
        "red" -> Color(0xFFF44336)
        "purple" -> Color(0xFF9C27B0)
        "yellow" -> Color(0xFFFFEB3B)
        else -> TerminalCyan
    }

    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(6.dp))
            .border(1.dp, BorderDark, RoundedCornerShape(6.dp))
            .background(SurfaceDark),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = badge.label,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = TextSecondary,
            modifier = Modifier
                .background(SurfaceVariantDark)
                .padding(horizontal = 8.dp, vertical = 4.dp)
        )
        if (badge.value.isNotBlank()) {
            Text(
                text = badge.value,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = badgeColor,
                modifier = Modifier
                    .background(SurfaceDark)
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            )
        }
    }
}

@Composable
fun TableView(
    headers: List<String>,
    rows: List<List<String>>,
    onLinkClick: (String) -> Unit
) {
    val context = LocalContext.current
    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clip(RoundedCornerShape(6.dp))
            .border(1.dp, BorderDark, RoundedCornerShape(6.dp))
            .horizontalScroll(rememberScrollState())
    ) {
        // Table Header Row
        Row(
            modifier = Modifier
                .background(SurfaceDark)
                .padding(horizontal = 10.dp, vertical = 8.dp)
        ) {
            headers.forEach { h ->
                Text(
                    text = h.removePrefix("**").removeSuffix("**"),
                    style = MaterialTheme.typography.titleSmall,
                    color = TerminalCyan,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .width(160.dp)
                        .padding(end = 12.dp)
                )
            }
        }
        HorizontalDivider(color = BorderDark, thickness = 1.dp)

        // Table Data Rows
        rows.forEachIndexed { rIdx, row ->
            Row(
                modifier = Modifier
                    .background(if (rIdx % 2 == 0) SurfaceVariantDark else SurfaceDark)
                    .padding(horizontal = 10.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                row.forEach { cell ->
                    val isCode = cell.startsWith("`") && cell.endsWith("`")
                    Box(
                        modifier = Modifier
                            .width(160.dp)
                            .padding(end = 12.dp)
                            .then(
                                if (isCode) {
                                    Modifier.clickable {
                                        val cmd = cell.removeSurrounding("`")
                                        clipboard.setPrimaryClip(ClipData.newPlainText("Command", cmd))
                                        Toast.makeText(context, "Command copied!", Toast.LENGTH_SHORT).show()
                                    }
                                } else Modifier
                            )
                    ) {
                        ClickableMarkdownText(
                            text = cell,
                            style = MaterialTheme.typography.bodySmall,
                            color = if (isCode) CyberGreen else TextPrimary,
                            onLinkClick = onLinkClick
                        )
                    }
                }
            }
            if (rIdx < rows.size - 1) {
                HorizontalDivider(color = BorderDark.copy(alpha = 0.5f), thickness = 0.5.dp)
            }
        }
    }
}

@Composable
fun ClickableMarkdownText(
    text: String,
    style: TextStyle = MaterialTheme.typography.bodyLarge,
    color: Color = TextPrimary,
    modifier: Modifier = Modifier,
    onLinkClick: (String) -> Unit
) {
    val regex = Regex("""(\*\*.*?\*\*|`.*?`|\[.*?\]\(.*?\)|<br\s*/?>|<.*?>)""")
    val annotated = buildAnnotatedString {
        var cursor = 0
        val matches = regex.findAll(text)
        for (m in matches) {
            val start = m.range.first
            val end = m.range.last + 1
            if (start > cursor) {
                append(text.substring(cursor, start))
            }
            val token = m.value
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
                token.startsWith("[") && token.contains("](") && token.endsWith(")") -> {
                    val linkMatch = Regex("""^\[(.*?)\]\((.*?)\)$""").find(token)
                    if (linkMatch != null) {
                        val linkText = linkMatch.groupValues[1]
                        val linkUrl = linkMatch.groupValues[2]
                        pushStringAnnotation(tag = "URL", annotation = linkUrl)
                        withStyle(
                            SpanStyle(
                                color = TerminalCyan,
                                textDecoration = TextDecoration.Underline,
                                fontWeight = FontWeight.SemiBold
                            )
                        ) {
                            append(linkText)
                        }
                        pop()
                    } else {
                        append(token)
                    }
                }
                token.equals("<br>", ignoreCase = true) ||
                token.equals("<br/>", ignoreCase = true) ||
                token.equals("<br />", ignoreCase = true) -> {
                    append("\n")
                }
                token.startsWith("<") && token.endsWith(">") -> {
                    // ignore raw HTML tags
                }
                else -> append(token)
            }
            cursor = end
        }
        if (cursor < text.length) {
            append(text.substring(cursor))
        }
    }

    val hasLinks = annotated.getStringAnnotations(tag = "URL", start = 0, end = annotated.length).isNotEmpty()
    if (hasLinks) {
        ClickableText(
            text = annotated,
            style = style.copy(color = color),
            modifier = modifier,
            onClick = { offset ->
                annotated.getStringAnnotations(tag = "URL", start = offset, end = offset)
                    .firstOrNull()?.let { annotation ->
                        onLinkClick(annotation.item)
                    }
            }
        )
    } else {
        Text(
            text = annotated,
            style = style,
            color = color,
            modifier = modifier
        )
    }
}

@Composable
fun MarkdownView(
    markdown: String,
    modifier: Modifier = Modifier,
    onLinkClick: (String) -> Unit = {}
) {
    val blocks = parseMarkdown(markdown)
    Column(modifier = modifier.fillMaxWidth()) {
        blocks.forEach { block ->
            MarkdownBlockView(block = block, onLinkClick = onLinkClick)
        }
    }
}
