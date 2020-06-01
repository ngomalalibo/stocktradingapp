package com.ngomalalibo.stocktradingapp.exception;

import com.google.common.collect.ComputationException;

import javax.annotation.Nullable;

public class InsufficientCaseException extends ComputationException
{
    public InsufficientCaseException(@Nullable Throwable cause)
    {
        super(cause);
    }
}
