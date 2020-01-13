## 操控外部程序 - 进程API

- `java.lang.Process` 类代表由Java程序创建的本地进程
- 能获得标准输入输出流并以此操控进程
- 由 `Runtime.getRuntime().exec()` 启动进程
- Java 7 提供了 `java.lang.ProcessBuilder` \
  用于更好构建进程

- ⚠️ 没有简单的方式获得进程信息
- ⚠️ 没有简单的方式获得进程列表

---

###  `java.lang.ProcessHandle`

```java
Optional<String> currUser = ProcessHandle.current().info().user();

Comparator<ProcessHandle> parentComparator = (p1, p2) -> {
  long pid1 = p1.parent().map(ProcessHandle::pid).orElse(-1L);
  long pid2 = p2.parent().map(ProcessHandle::pid).orElse(-1L);
  return Long.compare(pid1, pid2);
};
Consumer<ProcessHandle> showProcess = ExternalProcess::showProcess;

ProcessHandle.allProcesses()
    .filter(p1 -> p1.info().user().equals(currUser))
    .sorted(parentComparator)
    .peek(showProcess)
    .forEach(p -> {
      System.out.println(p.children().count());
      System.out.println(p.descendants().count());
    });
```
<!-- .element: class="stretch" -->

---

###  `java.lang.ProcessHandle.Info`

```java
ProcessBuilder     pb   = new ProcessBuilder("neofetch");
ProcessHandle.Info info = pb.start().info();

Optional<String>   command     = info.command();
Optional<String>   commandLine = info.commandLine();
Optional<Instant>  startTime   = info.startInstant();
Optional<String>   user        = info.user();
Optional<Duration> cpuDuration = info.totalCpuDuration();
Optional<String[]> arguments   = info.arguments();
```

------

## 访问线程栈

- 栈是LIFO结构
- 保存了函数调用的上下文
- 每个线程自有一条栈
- `StackWalker` API可以高效地访问栈

```java
Class<?> callerClass = StackWalker
    .getInstance(Option.RETAIN_CLASS_REFERENCE)
    .getCallerClass();
Logger logger = LogManager.getLogger(callerClass);

// java.lang.Throwable.getStackTrace()
```

---

只允许从 com.hypers 包调用该方法

```java
StackWalker.getInstance(Option.RETAIN_CLASS_REFERENCE)
    .walk(stacks -> {
      Predicate<StackFrame> hypersPackage = it -> it
          .getDeclaringClass().getPackageName()
          .startsWith("com.hypers");
      boolean eligible = stacks
          .dropWhile(hypersPackage)
          .findAny().isEmpty();
      if (!eligible) {
        throw new IllegalCallerException();
      }
      return eligible;
    });
```

------
<!-- .slide: class="center" -->

## 还有更多

- HTTP Client API
- `String.strip()` `String.repeat()`
- `Stream.takeWhile()` `Stream.dropWhile()`
- `InputStream.transferTo(OutputStream)`
- 运行.java文件
- JShell
- ...
