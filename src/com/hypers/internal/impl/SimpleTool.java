package com.hypers.internal.impl;

import com.hypers.api.Tool;
import com.hypers.internal.api.BaseTool;

import java.util.ServiceLoader;

/**
 * @author Ryoka Kujo chunxiang.huang@mail.hypers.com
 * @since 2020-01-13
 */
public class SimpleTool extends BaseTool {

  @Override
  public void someMethod() {

  }

  public static void main(String[] args) {
    ServiceLoader<Tool> loader = ServiceLoader.load(Tool.class);
    for (Tool tool : loader) {
      tool.someMethod();
    }
  }
}
