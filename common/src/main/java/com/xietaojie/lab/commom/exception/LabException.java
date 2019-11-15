package com.xietaojie.lab.commom.exception;

/**
 * @author xietaojie1992
 */
public class LabException extends RuntimeException {
    private static final long serialVersionUID = 2389523857892357825L;

    public LabException() {
        super();
    }

    public LabException(String message) {
        super(message);
    }

    public LabException(String message, Throwable cause) {
        super(message, cause);
    }

    public LabException(Throwable cause) {
        super(cause);
    }
}