package com.zengxin.rpcfx.serialize.impl;

import com.zengxin.rpcfx.api.RpcfxRequest;
import com.zengxin.rpcfx.serialize.SerializeException;
import com.zengxin.rpcfx.serialize.Serializer;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public class RpcfxRequestSerializer implements Serializer<RpcfxRequest> {
    @Override
    public int size(RpcfxRequest entry) {
        return 0;
    }

    @Override
    public void serialize(RpcfxRequest entry, byte[] bytes, int offset, int length) {
        ByteBuffer byteBuffer = ByteBuffer.wrap(bytes, offset, length);
        byte[] serviceClassBytes = entry.getServiceClass().getBytes(StandardCharsets.UTF_8);
        byteBuffer.putInt(serviceClassBytes.length);
        byteBuffer.put(serviceClassBytes);

        byte[] methodBytes = entry.getMethod().getBytes(StandardCharsets.UTF_8);
        byteBuffer.putInt(methodBytes.length);
        byteBuffer.put(methodBytes);
        //自定义Object的序列化，反射获取属性
        try (ByteArrayOutputStream byteArrayStream = new ByteArrayOutputStream();
             ObjectOutputStream outputStream = new ObjectOutputStream(byteArrayStream)) {
            outputStream.writeObject(entry.getParams());
            outputStream.flush();
            final byte[] paramsBytes = byteArrayStream.toByteArray();
            byteBuffer.putInt(paramsBytes.length);
            byteBuffer.put(paramsBytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public RpcfxRequest parse(byte[] bytes, int offset, int length) {
        ByteBuffer buffer = ByteBuffer.wrap(bytes, offset, length);
        int len = buffer.getInt();
        byte[] tmpBytes = new byte[len];
        buffer.get(tmpBytes);
        String serviceClass = new String(tmpBytes, StandardCharsets.UTF_8);

        len = buffer.getInt();
        tmpBytes = new byte[len];
        buffer.get(tmpBytes);
        String method = new String(tmpBytes, StandardCharsets.UTF_8);

        len = buffer.getInt();
        tmpBytes = new byte[len];
        buffer.get(tmpBytes);
        try (ByteArrayInputStream byteArrayStream = new ByteArrayInputStream(tmpBytes);
             ObjectInputStream outputStream = new ObjectInputStream(byteArrayStream)) {
            Object[] params = (Object[]) outputStream.readObject();
            return new RpcfxRequest(serviceClass, method, params);
        } catch (IOException | ClassNotFoundException e) {
            throw new SerializeException("serialize error!");
        }
    }

    @Override
    public byte type() {
        return SerializeType.RPC_FX_REQUEST_SERIALIZE;
    }

    @Override
    public Class<RpcfxRequest> getSerializeClass() {
        return RpcfxRequest.class;
    }

}
