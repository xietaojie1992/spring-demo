package com.xietaojie.lab.aop.exception;

/**
 * @author xietaojie1992
 */
public class ParamCheckException extends RuntimeException {

    private final String parameterName;
    private final String parameterType;

    public ParamCheckException(String parameterName, String parameterType) {
        super("");
        this.parameterName = parameterName;
        this.parameterType = parameterType;
    }

    @Override
    public String getMessage() {
        return "Required " + this.parameterType + " parameter \'" + this.parameterName + "\' must be not null !";
    }

    public final String getParameterName() {
        return this.parameterName;
    }

    public final String getParameterType() {
        return this.parameterType;
    }

}
