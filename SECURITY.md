# Security Policy


# Overview

client-side firewall and mobile honeypot engineered to protect Android devices from rogue, intrusive, or malicious hotspot hosts.When connected to untrusted public Wi-Fi or shared mobile hotspots, the app conducts **Active Traffic Analysis** to detect real-time network reconnaissance (SYN, UDP, and full TCP port scans), host-level intrusion attempts, and link-layer attacks like ARP spoofing/poisoning. It responds by dropping malicious packets, isolating device ports, generating incident logs, and redirecting attackers into a deceptive, sandbox honeypot environment that feeds them artificial data.
---
## Supported Versions & Devices
We actively maintain and issue security updates for the following operating environments:

| Device Type | Operating System Version | Support Status |
| :--- | :--- | :--- |
| **Android Mobile** | Android 8.0 (API 26) – Android 16 | :white_check_mark: Supported |
| **Android Tablet** | Android 8.0 (API 26) – Android 16 | :white_check_mark: Supported |
| **Legacy Devices** | Android < 8.0 | :x: Unsupported |

---
## Security Model & Technical Scope
When evaluating potential vulnerabilities within this project, please consider the core defensive mechanisms built into the application:
### 1. Active Traffic Analysis & Packet Filtering
* **Real-Time Detection:** The engine continuously inspects incoming frame headers to distinguish legitimate local network communication from hostile probes.
* **Automated Isolation:** Upon detecting rogue scanning behavior or ARP poisoning, the app drops suspicious incoming traffic, logs the origin IP, and isolates local device ports.
* **Assumed Operating State:** Vulnerabilities where a specifically crafted packet can crash the background monitoring engine or cause a Denial of Service (DoS) of the active packet filter are strictly **in scope**.
### 2. The Deceptive Honeypot Engine
* **Deceptive Sandbox:** The app deliberately responds to hostile probes with crafted, non-real vulnerabilities and synthetic responses to distract the attacker and capture their digital footprint.
* **Data Discarding:** Extracting fabricated or fake system parameters from the honeypot sandbox is **intended functionality, not a flaw**.
* **Sandbox Escapes:** Any vulnerability that allows an attacker to break out of the decoy honeypot layer to reach actual system files, true device specifications, or internal device memory is considered a **critical security flaw**.
---
## In-Scope vs. Out-of-Scope Vulnerabilities
### In-Scope Vulnerabilities
* **Honeypot Sandbox Escapes:** Bypassing the decoy layer to access genuine device parameters, real system files, or sensitive app data.
* **Packet Inspection Crashes:** Malformed network packets (SYN/UDP/TCP or ARP frames) causing thread crashes, memory leaks, or unhandled exceptions in the detection engine.
* **Filter Bypasses:** Circumvention mechanisms that allow a rogue hotspot host to execute port scans or ARP poisoning without triggering the alert or isolation engines.
* **Privilege Escalation:** Flaws that allow unauthenticated local network traffic to execute arbitrary code within the application sandbox.
### Out-of-Scope Issues
* Receiving fake/artificial data when probing the honeypot traps (this is by design).
* Vulnerabilities requiring prior root access to the host Android device.
* Denial-of-Service attacks relying on physical wireless jammer hardware or total Wi-Fi signal destruction.
* Attacks targeting OS-level system components outside the scope of the app's permissions.
---
