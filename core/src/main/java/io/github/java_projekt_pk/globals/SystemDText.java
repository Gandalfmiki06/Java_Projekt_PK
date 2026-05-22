package io.github.java_projekt_pk.globals;

import java.util.Random;

public class SystemDText {
    public static final String[] SYSTEMD_SERVICES = {
        // Core System & Kernel
        "Load Kernel Modules",
        "Apply Kernel Variables",
        "Create Static Device Nodes in /dev",
        "Mount Spruce-like Virtual File Systems",
        "Remount Root and Kernel File Systems",
        "File System Check on Root Device",
        "Initialize Automated Crash Reporting",
        "Create Volatile Files and Directories",
        "Flush Journal to Persistent Storage",
        "Update UTMP about System Boot/Shutdown",

        // Hardware & Device Management
        "udev Kernel Device Manager",
        "udev Coldplug all Devices",
        "Set console font and keymap",
        "Load/Save Screen Backlight Brightness",
        "Manage, Install and Generate Color Profiles",
        "RealtimeKit Scheduling Policy Service",
        "Disk Manager",
        "Entropy Daemon using the HAVEGE algorithm",

        // Networking & Connectivity
        "Network Service",
        "Network Configuration",
        "Network Name Resolution",
        "Network Time Synchronization",
        "WPA Supplicant",
        "Network Manager",
        "Network Manager Wait Online",
        "Avahi mDNS/DNS-SD Stack",
        "Modem Manager",
        "Bluetooth service",
        "OpenSSH server daemon",
        "Firewall Administration Tool",

        // User Space & Security
        "D-Bus System Message Bus",
        "User Login Management",
        "Authorization Manager",
        "Accounts Service",
        "Permit User Sessions",
        "Fingerprint Authentication Daemon",
        "Gnome Display Manager",
        "Light Display Manager",
        "Simple Desktop Display Manager",

        // Background Processes & Maintenance
        "Regular background program processing daemon",
        "Daily Cleanup of Temporary Directories",
        "Daily apt download activities",
        "Daily apt upgrade and clean activities",
        "Self Monitoring and Reporting Technology (SMART) Daemon",
        "System Logging Service",
        "LM-Sensors daemon",
        "TLP system startup/shutdown",

        // Finalization
        "Hold until boot process finishes up",
        "Terminate Plymouth Boot Screen",
        "PostgreSQL RDBMS",
        "Docker Application Container Engine",
        "VirtualBox Linux Kernel Module",
        "Snap Daemon"
    };

    public static String randomText() {
        Random r = new Random();
        var idx = Math.abs(r.nextInt()) % SYSTEMD_SERVICES.length;

        return SYSTEMD_SERVICES[idx];
    }
}
