# Yersinia Cheatsheet

[Yersinia](https://github.com/tomac/yersinia) is a powerful Layer-2 (Data Link layer) network penetration testing framework designed to analyze and exploit weaknesses in Layer-2 protocols such as STP (Spanning Tree Protocol), CDP (Cisco Discovery Protocol), DTP (Dynamic Trunking Protocol), DHCP, HSRP, and 802.1Q VLAN trunking.

---

## Table of Contents

- [Supported Protocols & Modes](#supported-protocols--modes)
- [Installation](#installation)
- [Interactive Curses Mode (`-I`)](#interactive-curses-mode--i)
- [Spanning Tree Protocol (STP) Attacks](#spanning-tree-protocol-stp-attacks)
- [Cisco Discovery Protocol (CDP) Attacks](#cisco-discovery-protocol-cdp-attacks)
- [Dynamic Trunking Protocol (DTP) & VLAN Hopping](#dynamic-trunking-protocol-dtp--vlan-hopping)
- [DHCP Rogue & Starvation Attacks](#dhcp-rogue--starvation-attacks)
- [HSRP & VRRP Hijacking](#hsrp--vrrp-hijacking)
- [Mitigation & Hardening](#mitigation--hardening)

---

## Supported Protocols & Modes

Yersinia targets protocols operating below IP:
- **STP:** Spanning Tree Protocol (Root bridge role takeover, topology change notifications).
- **CDP:** Cisco Discovery Protocol (Device flooding, fake neighbor injection).
- **DTP:** Dynamic Trunking Protocol (Negotiating trunk links to access all VLANs).
- **DHCP:** Dynamic Host Configuration Protocol (Starvation & rogue DHCP servers).
- **HSRP:** Hot Standby Router Protocol (Active gateway takeover).
- **802.1Q / 802.1X:** Dot1Q VLAN hopping and EAPOL exhaustion.
- **VTP:** VLAN Trunking Protocol (VLAN database deletion / overwriting).

Execution modes:
- **`-I` (Interactive Curses):** Full-screen terminal UI for selecting protocols, configuring attacks, and monitoring live packets.
- **Non-interactive CLI:** Direct execution for automated scripts.

---

## Installation

```bash
# Debian / Ubuntu / Kali Linux
sudo apt-get update && sudo apt-get install -y yersinia

# Arch Linux / BlackArch
sudo pacman -S yersinia

# Run in terminal
yersinia -h
```

---

## Interactive Curses Mode (`-I`)

The most practical and safe way to use Yersinia in the terminal without GUI:
```bash
sudo yersinia -I
```
- **`h`**: Display help and keybindings.
- **`g`**: Switch protocols (STP, CDP, DTP, DHCP, HSRP, etc.).
- **`x`**: Execute attack menu for the current protocol.
- **`c`**: Cancel running attack.
- **`l`**: Toggle packet logging.
- **`q`**: Exit Yersinia.

---

## Spanning Tree Protocol (STP) Attacks

STP elects a root bridge with the lowest Bridge ID (Priority + MAC). Yersinia can inject fake BPDUs (Bridge Protocol Data Units) to claim root status and redirect traffic through the attacker host.

### 1. Claim Root Role (BPDU Injection)
Send BPDUs with Priority `0` and a lower MAC address to become the STP Root Bridge:
```bash
sudo yersinia stp -attack 1 -i eth0
```

### 2. Claim Root Role with Topology Change (TCN)
Force all switches to flush CAM tables and re-converge through attacker:
```bash
sudo yersinia stp -attack 2 -i eth0
```

### 3. STP DoS Attack (Flood Config BPDUs)
Crash or saturate switch CPU with massive conflicting BPDU frames:
```bash
sudo yersinia stp -attack 0 -i eth0
```

---

## Cisco Discovery Protocol (CDP) Attacks

CDP shares device names, models, capabilities, and software versions unauthenticated across Cisco devices.

### 1. Flood Fake CDP Neighbors
Exhaust switch memory and create phantom devices in `show cdp neighbors`:
```bash
sudo yersinia cdp -attack 0 -i eth0
```

### 2. Capture and Inspect Legitimate CDP Packets
Run interactive curses mode (`yersinia -I`), press `g`, select `CDP`, and observe switch hostname, management IP, hardware model, and native VLAN ID.

---

## Dynamic Trunking Protocol (DTP) & VLAN Hopping

DTP automatically negotiates trunking links if switch ports are configured in `dynamic desirable` or `dynamic auto` mode.

### 1. Negotiate Trunking Link (DTP Desirable Packet)
Trick the switch port into turning into an 802.1Q trunk:
```bash
sudo yersinia dtp -attack 0 -i eth0
```
Once the trunk is negotiated, capture frames from all VLANs or tag packets into isolated subnets using Linux 802.1Q interfaces:
```bash
# Add VLAN subinterface for newly accessed VLAN (e.g. VLAN 20)
sudo ip link add link eth0 name eth0.20 type vlan id 20
sudo ip link set dev eth0.20 up
sudo dhclient eth0.20
```

---

## DHCP Rogue & Starvation Attacks

### 1. DHCP Starvation Attack (Exhaust IP Pool)
Send thousands of DHCP DISCOVER requests with random MAC addresses to consume all available IP addresses in the pool:
```bash
sudo yersinia dhcp -attack 1 -i eth0
```

### 2. Set Up Rogue DHCP Server
Deploy a rogue DHCP server with custom DNS and gateway:
```bash
sudo yersinia dhcp -attack 2 -i eth0
```

---

## HSRP & VRRP Hijacking

HSRP allows multiple routers to present a single virtual default gateway.

### 1. Claim Active Router Priority
Inject HSRP HELLO packets with priority 255 to take over the default gateway IP:
```bash
sudo yersinia hsrp -attack 0 -i eth0
```

---

## Mitigation & Hardening

| Protocol | Attack Vector | Cisco / Standard Mitigation |
|---|---|---|
| **STP** | Fake Root Bridge Injection | `spanning-tree bpduguard enable` on access ports; `spanning-tree rootguard` on inter-switch links. |
| **DTP** | Rogue Trunk Negotiation | `switchport mode access` & `switchport nonegotiate` on all user-facing access ports. |
| **CDP** | Flooding / Info Disclosure | `no cdp run` (globally) or `no cdp enable` on untrusted interface ports. |
| **DHCP** | Starvation & Rogue Server | Enable `ip dhcp snooping`, limit rate per port, configure trusted uplink ports. |
| **HSRP** | Priority Hijacking | Configure MD5 authentication with secret keys (`standby 1 authentication md5 key-string <SECRET>`). |
