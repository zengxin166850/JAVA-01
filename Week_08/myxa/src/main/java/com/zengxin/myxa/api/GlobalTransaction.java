package com.zengxin.myxa.api;

import com.zengxin.myxa.exception.TransactionException;

public interface GlobalTransaction {
    void begin() throws TransactionException;

    void begin(int timeout) throws TransactionException;

    void begin(int timeout, String name) throws TransactionException;

    void commit() throws TransactionException;

    void rollback() throws TransactionException;

    /**
     *
     */
    String getXid();
}
