package io.github.java_projekt_pk.globals;

import java.util.Random;

public class SystemDText {

    public static final String[] SYSTEMD_OK_MESSAGES = {
        "Reached target Local File Systems.",
        "Started Load Kernel Modules.",
        "Started Apply Kernel Variables.",
        "Created slice User and Session Slice.",
        "Mounted Huge Pages File System.",
        "Mounted POSIX Message Queue File System.",
        "Started Create Static Device Nodes in /dev.",
        "Started Remount Root and Kernel File Systems.",
        "Started File System Check on Root Device.",
        "Started Journal Service.",
        "Started Flush Journal to Persistent Storage.",
        "Started udev Kernel Device Manager.",
        "Started udev Coldplug all Devices.",
        "Started Set console font and keymap.",
        "Started Load/Save Screen Backlight Brightness.",
        "Started RealtimeKit Scheduling Policy Service.",
        "Started Disk Manager.",
        "Started Entropy Daemon using the HAVEGE algorithm.",
        "Reached target Network.",
        "Started Network Service.",
        "Started Network Name Resolution.",
        "Started Network Time Synchronization.",
        "Started WPA Supplicant.",
        "Started Network Manager.",
        "Started Avahi mDNS/DNS-SD Stack.",
        "Started Bluetooth service.",
        "Started OpenSSH server daemon.",
        "Started Firewall Administration Tool.",
        "Started D-Bus System Message Bus.",
        "Started User Login Management.",
        "Started Authorization Manager.",
        "Started Accounts Service.",
        "Started Permit User Sessions.",
        "Started Gnome Display Manager.",
        "Started Regular background program processing daemon.",
        "Started Daily Cleanup of Temporary Directories.",
        "Started System Logging Service.",
        "Started TLP system startup/shutdown.",
        "Started Docker Application Container Engine.",
        "Reached target Graphical Interface."
    };

    public static final String[] SYSTEMD_WARN_MESSAGES = {
        "Dependency failed for Network Manager Wait Online.",
        "Timed out waiting for device dev-sda2.device.",
        "Failed to start Load/Save Screen Backlight Brightness.",
        "Avahi daemon running without IPv6 support.",
        "Bluetooth adapter not found, continuing.",
        "LM-Sensors daemon detected no compatible sensors.",
        "SMART monitoring skipped for removable drive.",
        "PostgreSQL service delayed during startup.",
        "Snap Daemon startup exceeded 10s threshold.",
        "WPA Supplicant configuration file missing optional field.",
        "Entropy pool not fully initialized.",
        "Modem Manager detected no modem devices.",
        "Fingerprint Authentication Daemon unavailable.",
        "Plymouth boot screen terminated unexpectedly.",
        "Failed to restore console font preferences.",
        "Journal file corrupted, rotating logs.",
        "DNS resolver fallback to secondary nameserver.",
        "Docker bridge network using default subnet.",
        "System clock unsynchronized."
    };

    public static final String[] SYSTEMD_ERROR_MESSAGES = {
        "Failed to start Network Configuration.",
        "Failed to mount /boot.",
        "Failed to start OpenSSH server daemon.",
        "Failed to initialize automated crash reporting.",
        "Failed to load kernel module 'vboxdrv'.",
        "Failed to start PostgreSQL RDBMS.",
        "Failed to start Docker Application Container Engine.",
        "Failed to activate swap /swapfile.",
        "Failed to start Firewall Administration Tool.",
        "Failed to start Snap Daemon.",
        "Dependency failed for Graphical Interface.",
        "Failed to start Light Display Manager.",
        "Failed to start Simple Desktop Display Manager.",
        "Failed to connect to D-Bus System Message Bus.",
        "Failed to start User Login Management.",
        "Failed to synchronize system clock.",
        "Failed to detect root filesystem UUID.",
        "Emergency mode activated.",
        "Failed to start Disk Manager.",
        "Kernel panic - not syncing: Fatal exception."
    };

    public static String generateOKMessage() {
        Random r = new Random();
        var idx = Math.abs(r.nextInt()) % SYSTEMD_OK_MESSAGES.length;

        return SYSTEMD_OK_MESSAGES[idx];
    }

    public static String generateWARNMessage() {
        Random r = new Random();
        var idx = Math.abs(r.nextInt()) % SYSTEMD_WARN_MESSAGES.length;

        return SYSTEMD_WARN_MESSAGES[idx];
    }

    public static String generateERRORMessage() {
        Random r = new Random();
        var idx = Math.abs(r.nextInt()) % SYSTEMD_ERROR_MESSAGES.length;

        return SYSTEMD_ERROR_MESSAGES[idx];
    }
}
