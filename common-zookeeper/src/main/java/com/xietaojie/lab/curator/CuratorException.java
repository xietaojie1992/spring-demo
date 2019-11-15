package com.xietaojie.lab.curator;

/**
 * Curator 异常类
 */
public class CuratorException extends RuntimeException {

    private static final long serialVersionUID = 7573468048337611215L;

    public CuratorException() {
        super();
    }

    public CuratorException(String message) {
        super(message);
    }

    public CuratorException(String message, Throwable cause) {
        super(message, cause);
    }

    public CuratorException(Throwable cause) {
        super(cause);
    }
}