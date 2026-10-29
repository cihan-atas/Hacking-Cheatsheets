package com.hackingcheatsheets.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Terminal
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hackingcheatsheets.app.data.AppLanguage
import com.hackingcheatsheets.app.data.Cheatsheet
import com.hackingcheatsheets.app.ui.theme.BackgroundDark
import com.hackingcheatsheets.app.ui.theme.BorderDark
import com.hackingcheatsheets.app.ui.theme.CyberGreen
import com.hackingcheatsheets.app.ui.theme.ExploitRed
import com.hackingcheatsheets.app.ui.theme.SurfaceDark
import com.hackingcheatsheets.app.ui.theme.SurfaceVariantDark
import com.hackingcheatsheets.app.ui.theme.TerminalCyan
import com.hackingcheatsheets.app.ui.theme.TextMuted
import com.hackingcheatsheets.app.ui.theme.TextPrimary
import com.hackingcheatsheets.app.ui.theme.TextSecondary
import com.hackingcheatsheets.app.viewmodel.CheatsheetViewModel

@Composable
fun HomeScreen(
    viewModel: CheatsheetViewModel,
    onNavigateToDetail: (String) -> Unit,
    onNavigateToSearch: () -> Unit,
    onNavigateToCategories: (String?) -> Unit
) {
    val language by viewModel.currentLanguage.collectAsState()
    val allCheatsheets by viewModel.allCheatsheets.collectAsState()
    val categories by viewModel.categories.collectAsState()

    val featuredIds = listOf(
        "ffuf", "ligolo-ng", "nmap", "certipy", "netexec",
        "peass-ng", "chisel", "metasploit", "sqlmap",
        "burp-suite", "feroxbuster", "jwt-attacks", "hydra"
    )
    val featuredTools = allCheatsheets.filter { it.id in featuredIds }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundDark)
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(top = 16.dp, bottom = 80.dp)
    ) {
        // Top Header
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(CyberGreen),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Terminal,
                            contentDescription = "Hacker Icon",
                            tint = BackgroundDark,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = "HACKING CHEATSHEETS",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                        Text(
                            text = if (language == AppLanguage.TR) "138 ARAÇ • %100 ÇEVRİMDIŞI" else "138 TOOLS • 100% OFFLINE",
                            fontSize = 11.sp,
                            fontFamily = FontFamily.Monospace,
                            color = CyberGreen
                        )
                    }
                }

                // Language Toggle Badge
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(16.dp))
                        .background(SurfaceVariantDark)
                        .border(1.dp, BorderDark, RoundedCornerShape(16.dp))
                        .clickable { viewModel.toggleLanguage() }
                        .padding(horizontal = 10.dp, vertical = 6.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Language,
                            contentDescription = "Language",
                            tint = TerminalCyan,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = if (language == AppLanguage.TR) "🇹🇷 TR" else "🇬🇧 EN",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Search Bar Button
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(SurfaceDark)
                    .border(1.dp, BorderDark, RoundedCornerShape(12.dp))
                    .clickable { onNavigateToSearch() }
                    .padding(horizontal = 14.dp, vertical = 12.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search",
                        tint = TextMuted,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = if (language == AppLanguage.TR) "Araç, komut, parametre veya CVE ara..." else "Search tools, commands, flags, CVEs...",
                        color = TextMuted,
                        fontSize = 14.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // MITRE ATT&CK Phase Horizontal Selector
            Text(
                text = if (language == AppLanguage.TR) "🎯 SALDIRI AŞAMALARI (KILL CHAIN)" else "🎯 ATTACK KILL CHAIN",
                style = MaterialTheme.typography.labelLarge,
                color = TerminalCyan
            )
            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                val phases = if (language == AppLanguage.TR) listOf(
                    "01 Başlangıç Erişimi" to "attack-methodology-01-initial-access",
                    "02 Numaralandırma" to "attack-methodology-02-enumeration",
                    "03 Yetki Yükseltme" to "attack-methodology-03-privilege-escalation",
                    "04 Yanal Hareket" to "attack-methodology-04-lateral-movement",
                    "05 Kalıcılık" to "attack-methodology-05-persistence",
                    "06 Savunma Atlama" to "attack-methodology-06-defense-evasion",
                    "07 Amaç Eylemleri" to "attack-methodology-07-actions-objectives"
                ) else listOf(
                    "01 Initial Access" to "attack-methodology-01-initial-access",
                    "02 Enumeration" to "attack-methodology-02-enumeration",
                    "03 PrivEsc" to "attack-methodology-03-privilege-escalation",
                    "04 Lateral Movement" to "attack-methodology-04-lateral-movement",
                    "05 Persistence" to "attack-methodology-05-persistence",
                    "06 Defense Evasion" to "attack-methodology-06-defense-evasion",
                    "07 Actions Objectives" to "attack-methodology-07-actions-objectives"
                )

                phases.forEach { (label, id) ->
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(SurfaceVariantDark)
                            .border(1.dp, BorderDark, RoundedCornerShape(8.dp))
                            .clickable { onNavigateToDetail(id) }
                            .padding(horizontal = 12.dp, vertical = 8.dp)
                    ) {
                        Text(
                            text = label,
                            color = TextPrimary,
                            fontSize = 12.sp,
                            fontFamily = FontFamily.Monospace
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Featured High-Impact Tools Header
            Text(
                text = if (language == AppLanguage.TR) "⚡ POPÜLER & KRİTİK ARAÇLAR" else "⚡ FEATURED & ESSENTIAL TOOLS",
                style = MaterialTheme.typography.labelLarge,
                color = CyberGreen
            )
            Spacer(modifier = Modifier.height(8.dp))
        }

        // Featured Tools List
        items(featuredTools) { tool ->
            CheatsheetCardItem(
                tool = tool,
                language = language,
                onClick = { onNavigateToDetail(tool.id) }
            )
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
fun CheatsheetCardItem(
    tool: Cheatsheet,
    language: AppLanguage,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = SurfaceDark),
        shape = RoundedCornerShape(10.dp),
        border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(BorderDark))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = if (language == AppLanguage.TR) tool.titleTr else tool.title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                }
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = tool.category.uppercase(),
                    fontSize = 11.sp,
                    fontFamily = FontFamily.Monospace,
                    color = TerminalCyan
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = (if (language == AppLanguage.TR) tool.descriptionTr else tool.description) ?: "",
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 2,
                    color = TextSecondary
                )
            }
            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = "Open",
                tint = TextMuted,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}
