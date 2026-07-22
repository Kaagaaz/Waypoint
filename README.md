# Waypoint
Overview -

This app is designed to protect connected users from rogue, intrusive, or malicious hotspot providers. When you connect to public Wi-Fi or a shared mobile hotspot, you usually trust the network host with your device’s visibility—leaving you exposed to local attacks. Our app acts as an active, client-side firewall that detects and instantly blocks network reconnaissance, unauthorized port scans, and host-level intrusion attempts in real time, keeping your private data safe on any untrusted network.

Why You Need It -

When you connect to someone else's hotspot, the host has a privileged position on the local network. A malicious host can quietly probe your device to find open ports, identify running services, and execute Man-In-The-Middle (MITM) attacks often without your knowledge. Standard antivirus and VPNs don't always stop local host-to-client port scanning; our app fills that exact gap.

How It Works -

1. Active Traffic Analysis -
The app runs silently in the background, constantly inspecting incoming network packets on your device's interface to distinguish normal network traffic from suspicious host activities.

2. Instant Scan & Reconnaissance Detection -
The moment the hotspot host initiates a port scan (like SYN, UDP, or full TCP scans) or attempts ARP spoofing/poisoning, the app flags the source IP and pinpoints the exact attack vector.

3. Automated Threat Blocking & Isolation -
Once detected, the app immediately drops the malicious packets, blocks further probes from the host, and isolates your device’s ports—stopping the attacker before they can discover open entry points or exploit vulnerabilities.

4. Real-Time Alerts & Incident Logs -
Receive immediate notifications explaining what the hotspot host attempted to do, complete with detailed logs of blocked ports and hostile network activity.
