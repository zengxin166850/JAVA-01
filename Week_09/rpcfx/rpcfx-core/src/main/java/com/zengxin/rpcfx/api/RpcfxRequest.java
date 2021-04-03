package com.zengxin.rpcfx.api;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RpcfxRequest {
  private String serviceClass;
  private String method;
  private Object[] params;
}