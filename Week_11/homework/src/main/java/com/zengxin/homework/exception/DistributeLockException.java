package com.zengxin.homework.exception;

/**
 * 锁异常
 */
public class DistributeLockException extends RuntimeException {
    public DistributeLockException() {
    }

    public DistributeLockException(String message) {
        super(message);
    }
}
