package com.xietaojie.lab.commom.exception;

/**
 * @author xietaojie1992
 */
public class LabException extends RuntimeException {
    private static final long serialVersionUID = 2389523857892357825L;

    private String exceptionCode = "UNKNOWN_EXCEPTION";

    private String exceptionMsg = "no exception message";

    public LabException() {
        super();
    }

    public LabException(String exceptionCode) {
        super(exceptionCode);
        this.exceptionCode = exceptionCode;
    }

    public LabException(String exceptionCode, String exceptionMsg) {
        super(exceptionCode);
        this.exceptionCode = exceptionCode;
        this.exceptionMsg = exceptionMsg;
    }

    public LabException(Throwable cause) {
        super(cause);
    }

    public LabException(String exceptionCode, Throwable cause) {
        super(exceptionCode, cause);
        this.exceptionCode = exceptionCode;
    }

    public LabException(String exceptionCode, String exceptionMsg, Throwable cause) {
        super(exceptionCode, cause);
        this.exceptionCode = exceptionCode;
        this.exceptionMsg = exceptionMsg;
    }

    public String getExceptionCode() {
        return exceptionCode;
    }

    public void setExceptionCode(String exceptionCode) {
        this.exceptionCode = exceptionCode;
    }

    public String getExceptionMsg() {
        return exceptionMsg;
    }

    public void setExceptionMsg(String exceptionMsg) {
        this.exceptionMsg = exceptionMsg;
    }

}