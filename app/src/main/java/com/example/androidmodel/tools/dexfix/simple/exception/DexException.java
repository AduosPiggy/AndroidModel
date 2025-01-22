package com.example.androidmodel.tools.dexfix.simple.exception;

/**
 * @author kfflso
 * @data 2025-01-22 20:02
 * @plus:
 */

/**
 * Thrown when there's a format problem reading, writing, or generally
 * processing a dex file.
 */
public class DexException extends ExceptionWithContext {
    public DexException(String message) {
        super(message);
    }

    public DexException(Throwable cause) {
        super(cause);
    }
}
