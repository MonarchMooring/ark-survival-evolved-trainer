package com.arktrainer;

import com.sun.jna.Native;
import com.sun.jna.platform.win32.Kernel32;
import com.sun.jna.platform.win32.WinNT;
import com.sun.jna.ptr.IntByReference;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * MemoryManager handles reading and writing process memory for the ARK trainer.
 * Uses JNA to interact with Windows kernel32.dll for memory operations.
 */
public class MemoryManager {

    private final Kernel32 kernel32;
    private final int processId;
    private WinNT.HANDLE processHandle;

    /**
     * Constructs a MemoryManager for a given process ID.
     *
     * @param processId The process ID of the ARK game process.
     */
    public MemoryManager(int processId) {
        this.kernel32 = Kernel32.INSTANCE;
        this.processId = processId;
        this.processHandle = openProcess();
    }

    /**
     * Opens the process with necessary permissions (VM_READ | VM_WRITE | VM_OPERATION).
     *
     * @return Handle to the process, or null if failed.
     */
    private WinNT.HANDLE openProcess() {
        int desiredAccess = WinNT.PROCESS_VM_READ | WinNT.PROCESS_VM_WRITE | WinNT.PROCESS_VM_OPERATION;
        WinNT.HANDLE handle = kernel32.OpenProcess(desiredAccess, false, processId);
        if (handle == null) {
            System.err.println("Failed to open process. Error code: " + Native.getLastError());
        }
        return handle;
    }

    /**
     * Reads an integer from a memory address.
     *
     * @param address The memory address to read from.
     * @return The integer value at that address.
     * @throws IllegalStateException if reading fails.
     */
    public int readInt(long address) {
        ByteBuffer buffer = ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN);
        IntByReference bytesRead = new IntByReference();
        boolean success = kernel32.ReadProcessMemory(processHandle, address, buffer, 4, bytesRead);
        if (!success || bytesRead.getValue() != 4) {
            throw new IllegalStateException("Failed to read memory at address: " + Long.toHexString(address));
        }
        return buffer.getInt(0);
    }

    /**
     * Writes an integer to a memory address.
     *
     * @param address The memory address to write to.
     * @param value   The integer value to write.
     * @throws IllegalStateException if writing fails.
     */
    public void writeInt(long address, int value) {
        ByteBuffer buffer = ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN);
        buffer.putInt(value);
        buffer.flip();
        IntByReference bytesWritten = new IntByReference();
        boolean success = kernel32.WriteProcessMemory(processHandle, address, buffer, 4, bytesWritten);
        if (!success || bytesWritten.getValue() != 4) {
            throw new IllegalStateException("Failed to write memory at address: " + Long.toHexString(address));
        }
    }

    /**
     * Reads a float from a memory address.
     *
     * @param address The memory address to read from.
     * @return The float value at that address.
     */
    public float readFloat(long address) {
        int raw = readInt(address);
        return Float.intBitsToFloat(raw);
    }

    /**
     * Writes a float to a memory address.
     *
     * @param address The memory address to write to.
     * @param value   The float value to write.
     */
    public void writeFloat(long address, float value) {
        writeInt(address, Float.floatToIntBits(value));
    }

    /**
     * Closes the process handle to release resources.
     */
    public void close() {
        if (processHandle != null) {
            kernel32.CloseHandle(processHandle);
            processHandle = null;
        }
    }

    @Override
    protected void finalize() throws Throwable {
        close();
        super.finalize();
    }
}
