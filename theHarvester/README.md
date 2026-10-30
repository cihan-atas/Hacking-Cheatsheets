# theHarvester Cheatsheet

[theHarvester](https://github.com/laramies/theHarvester) is one of the most widely used open-source intelligence (OSINT) tools for gathering emails, subdomains, employee names, virtual hosts, open ports, and employee PGP keys from diverse public sources (search engines, PGP key servers, Shodan, Censys, Certspotter, etc.).

---

## Table of Contents

- [Supported Data Sources](#supported-data-sources)
- [Installation & API Key Configuration](#installation--api-key-configuration)
- [Basic Domain Reconnaissance](#basic-domain-reconnaissance)
- [Passive Multi-Source Harvesting](#passive-multi-source-harvesting)
- [Active DNS Verification & Brute-Forcing](#active-dns-verification--brute-forcing)
- [Output Formats (HTML, XML, JSON)](#output-formats-html-xml-json)
- [Pentest Workflow Integration](#pentest-workflow-integration)

---

## Supported Data Sources

theHarvester aggregates intelligence from dozens of passive and active sources:
- **Search Engines (Free / No Key):** Google, Bing, DuckDuckGo, Yahoo, Baidu.
- **Certificate Transparency:** CRT.sh, Certspotter.
- **Code & Threat Repositories:** GitHub, AlienVault OTX, ThreatCrowd, Rapid7, VirusTotal.
- **Reconnaissance & OSINT APIs:** Shodan, Censys, Hunter.io, SecurityTrails, Intelx, FullContact.
- **Network / DNS:** Anubis, HackerTarget, DNSDumpster.

---

## Installation & API Key Configuration

### 1. Installation
```bash
# Debian / Ubuntu / Kali Linux
sudo apt-get update && sudo apt-get install -y theharvester

# From GitHub Source (Latest Version)
git clone https://github.com/laramies/theHarvester.git
cd theHarvester
python3 -m pip install -r requirements/base.txt

# Test binary
theHarvester -h
```

### 2. Configure API Keys (`api-keys.yaml`)
Many sources (Hunter, Shodan, Censys, Intelx, GitHub) require free or paid API keys.
Create or edit `api-keys.yaml` in the configuration path:
```yaml
apikeys:
  hunter:
    key: "YOUR_HUNTER_KEY"
  shodan:
    key: "YOUR_SHODAN_KEY"
  github:
    key: "YOUR_GITHUB_TOKEN"
  securityTrails:
    key: "YOUR_SECURITYTRAILS_KEY"
```

---

## Basic Domain Reconnaissance

### 1. Harvest Emails and Subdomains Using Free Engines
```bash
theHarvester -d example.com -l 500 -b duckduckgo,bing,crtsh
```
- `-d <domain>`: Target domain to recon.
- `-l <limit>`: Number of results to query per engine.
- `-b <sources>`: Comma-separated list of sources (or `all`).

### 2. Query All Configured Sources
```bash
theHarvester -d example.com -l 500 -b all
```

---

## Passive Multi-Source Harvesting

Query specific threat intelligence and certificate transparency databases without sending traffic to the target:

### 1. Certificate Transparency & Threat Intel
```bash
theHarvester -d example.com -b crtsh,certspotter,anubis,hackertarget,otx
```

### 2. Target Specific Source Groups
```bash
# Query GitHub commits and issues for leaked emails and subdomains
theHarvester -d example.com -b github-code

# Query Shodan for discovered IP addresses and open ports
theHarvester -d example.com -b shodan
```

---

## Active DNS Verification & Brute-Forcing

### 1. Resolve Discovered Subdomains (`-r`)
Perform active DNS lookups to verify if collected subdomains are currently live:
```bash
theHarvester -d example.com -b crtsh,bing -r
```

### 2. DNS Brute-Force (`-c`)
Perform wordlist-based DNS subdomain brute-forcing directly within theHarvester:
```bash
theHarvester -d example.com -b duckduckgo -c
```

### 3. Virtual Host Verification (`-v`)
Verify virtual hosts via HTTP requests:
```bash
theHarvester -d example.com -b duckduckgo -v
```

---

## Output Formats (HTML, XML, JSON)

Export results for automated reporting or downstream reconnaissance:

### 1. Export to HTML & XML
```bash
theHarvester -d example.com -b duckduckgo,crtsh -f /tmp/theharvester_results
```
Creates `/tmp/theharvester_results.html` and `/tmp/theharvester_results.xml`.

### 2. Parse Discovered Subdomains with `jq`
If exporting or parsing JSON:
```bash
theHarvester -d example.com -b crtsh,duckduckgo -f recon_out
# Extract clean host list
cat recon_out.xml | grep -oP '(?<=<host>).*?(?=</host>)' | sort -u > live_hosts.txt
```

---

## Pentest Workflow Integration

Chain theHarvester results directly with active vulnerability scanners:

```bash
# 1. Harvest passive subdomains
theHarvester -d target.corp -b crtsh,duckduckgo,bing -f target_recon

# 2. Extract unique domain names
grep -oP '(?<=<host>)[^<]+' target_recon.xml | sort -u > hosts.txt

# 3. Probe for live web services using httpx
cat hosts.txt | httpx -silent -status-code -title > live_web.txt

# 4. Scan live web services with Nuclei
nuclei -l live_web.txt -severity medium,high,critical
```
