package com.zengxin.myxa.api;

import com.zengxin.myxa.exception.TransactionException;

public class DefaultGlobalTransaction implements GlobalTransaction{
    @Override
    public void begin() throws TransactionException {

    }

    @Override
    public void begin(int timeout) throws TransactionException {

    }

    @Override
    public void begin(int timeout, String name) throws TransactionException {

    }

    @Override
    public void commit() throws TransactionException {

    }

    @Override
    public void rollback() throws TransactionException {

    }

    @Override
    public String getXid() {
        return null;
    }
}
