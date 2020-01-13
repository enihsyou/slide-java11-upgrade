/**
 * @author Ryoka Kujo chunxiang.huang@mail.hypers.com
 * @since 2020-01-12
 */
module com.hypers.api {
  // 依赖模组
  requires java.base;
  requires org.jetbrains.annotations;
  requires org.apache.logging.log4j;
  // 导出，对外暴露
  exports com.hypers.api;
  // 限定导出
  exports com.hypers.internal.api to com.hypers.internal.impl;
  // 开放模组，允许反射
  opens com.hypers.internal.impl /* to xxx*/;
  // 可以注册的服务
  uses com.hypers.api.Tool;
  // 通过ServiceLoader可以发现的注册服务
  provides com.hypers.api.Tool with com.hypers.internal.impl.SimpleTool;
}
