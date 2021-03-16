package com.zengxin.rpcfx.api;

import lombok.Data;

@Data
public class RpcfxRequest {
  private String serviceClass;
  private String method;
  private Object[] params;
}