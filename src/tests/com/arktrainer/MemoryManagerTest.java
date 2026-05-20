package com.arktrainer;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for MemoryManager class.
 * These tests are designed to run without an actual ARK process;
 * they test the logic of memory operations using mock-like assumptions.
 */
public class MemoryManagerTest {

    @Test
    public void testReadIntThrowsOnInvalidProcess() {
        // Using an invalid process ID should cause failure
        MemoryManager manager = new MemoryManager(99999);
        assertThrows(IllegalStateException.class, () -> {
            manager.readInt(0x12345678L);
        });
        manager.close();
    }

    @Test
    public void testWriteIntThrowsOnInvalidProcess() {
        MemoryManager manager = new MemoryManager(99999);
        assertThrows(IllegalStateException.class, () -> {
            manager.writeInt(0x12345678L, 100);
        });
        manager.close();
    }

    @Test
    public void testReadFloatThrowsOnInvalidProcess() {
        MemoryManager manager = new MemoryManager(99999);
        assertThrows(IllegalStateException.class, () -> {
            manager.readFloat(0x12345678L);
        });
        manager.close();
    }

    @Test
    public void testWriteFloatThrowsOnInvalidProcess() {
        MemoryManager manager = new MemoryManager(99999);
        assertThrows(IllegalStateException.class, () -> {
            manager.writeFloat(0x12345678L, 3.14f);
        });
        manager.close();
    }

    @Test
    public void testCloseDoesNotThrow() {
        MemoryManager manager = new MemoryManager(99999);
        assertDoesNotThrow(manager::close);
    }
}
