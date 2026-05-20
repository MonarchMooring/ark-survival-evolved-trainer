package com.arktrainer;

import com.sun.jna.Native;
import com.sun.jna.platform.win32.Tlhelp32;
import com.sun.jna.platform.win32.Kernel32;
import com.sun.jna.platform.win32.WinNT;
import com.sun.jna.ptr.IntByReference;

/**
 * Utility class to find the process ID of ARK: Survival Evolved by its executable name.
 */
public class ProcessFinder {

    private static final String ARK_EXECUTABLE = "ShooterGame.exe";

    /**
     * Finds the process ID of the ARK game by iterating through running processes.
     *
     * @return The process ID, or -1 if not found.
     */
    public static int findArkProcessId() {
        Kernel32 kernel32 = Kernel32.INSTANCE;
        WinNT.HANDLE snapshot = kernel32.CreateToolhelp32Snapshot(Tlhelp32.TH32CS_SNAPPROCESS, 0);
        if (snapshot == null || WinNT.INVALID_HANDLE_VALUE.equals(snapshot)) {
            System.err.println("Failed to create process snapshot. Error: " + Native.getLastError());
            return -1;
        }

        Tlhelp32.PROCESSENTRY32.ByReference processEntry = new Tlhelp32.PROCESSENTRY32.ByReference();
        processEntry.dwSize = new Tlhelp32.PROCESSENTRY32().dwSize;

        boolean found = false;
        int processId = -1;
        if (kernel32.Process32First(snapshot, processEntry)) {
            do {
                String exeFile = Native.toString(processEntry.szExeFile);
                if (ARK_EXECUTABLE.equalsIgnoreCase(exeFile)) {
                    processId = processEntry.th32ProcessID;
                    found = true;
                    break;
                }
            } while (kernel32.Process32Next(snapshot, processEntry));
        }
        kernel32.CloseHandle(snapshot);
        return processId;
    }

    /**
     * Alternative method using process name pattern matching for robustness.
     *
     * @return The process ID, or -1 if not found.
     */
    public static int findArkProcessIdByName() {
        // Fallback: could use WMI or other methods; here just wraps findArkProcessId
        return findArkProcessId();
    }
}
