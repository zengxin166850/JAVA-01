package com.zengxin.rpcfx.serialize.impl;

import com.zengxin.rpcfx.api.RpcfxResponse;
import com.zengxin.rpcfx.serialize.Serializer;

import java.nio.ByteBuffer;

public class RpcfxResponseSerializer implements Serializer<RpcfxResponse> {
    @Override
    public int size(RpcfxResponse entry) {
        return 0;
    }

    @Override
    public void serialize(RpcfxResponse entry, byte[] bytes, int offset, int length) {
        ByteBuffer byteBuffer = ByteBuffer.wrap(bytes,offset,length);
    }

    @Override
    public RpcfxResponse parse(byte[] bytes, int offset, int length) {
        return null;
    }

    @Override
    public byte type() {
        return 0;
    }

    @Override
    public Class<RpcfxResponse> getSerializeClass() {
        return null;
    }
}
