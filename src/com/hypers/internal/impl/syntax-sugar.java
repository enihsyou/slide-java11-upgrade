package com.hypers.internal.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.lang.StackWalker.Option;
import java.lang.StackWalker.StackFrame;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.IntPredicate;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@SuppressWarnings({"Convert2Lambda", "Convert2Diamond", "DuplicatedCode", "MultipleVariablesInDeclaration"})
class Diamond {

  interface Function<T, R> extends java.util.function.Function<T, R> {

    R apply(T t);
  }

  public static void main(String[] args) throws NoSuchFieldException {
    var input = "😂你再说";

    java6(input);
    java7(input);
    java8(input);
    java8_old(input);
    java11(input);
    java11_wrong(input);
  }

  private static void java6(String input) {
    Function<String, List<String>> function =
        new Function<String, List<String>>() {
          public List<String> apply(@NotNull String s) {
            List<String> strings = new ArrayList<String>();
            int          length  = s.length(), offset = 0;
            while (offset < length) {
              int codepoint = s.codePointAt(offset);
              strings.add(String.valueOf(Character.toChars(codepoint)));
              offset += Character.charCount(codepoint);
            }
            return strings;
          }
        };

    System.out.println(function.apply(input));
  }

  private static void java7(String input) {
    Function<String, List<String>> function = new Function<String, List<String>>() {
      public List<String> apply(@NotNull String s) {
        List<String> strings = new ArrayList<String>();
        int          length  = s.length(), offset = 0;
        while (offset < length) {
          int codepoint = s.codePointAt(offset);
          strings.add(String.valueOf(Character.toChars(codepoint)));
          offset += Character.charCount(codepoint);
        }
        return strings;
      }
    };

    System.out.println(function.apply(input));
  }

  private static void java8_old(String input) {
    Function<String, List<String>> function = new Function<String, List<String>>() {
      public List<String> apply(@NotNull String s) {
        List<String> strings = new ArrayList<>();
        int          length  = s.length(), offset = 0;

        Function<Integer, Integer> codePointAt      = s::codePointAt;
        Function<Integer, char[]>  codePointTo16Bit = Character::toChars;
        Function<char[], String>   arrayToString    = String::valueOf;
        Function<String, Integer> appendToList = (w) -> {
          strings.add(w);
          return w.codePointAt(0);
        };
        Function<Integer, Integer> moveMetric = Character::charCount;

        while (offset < length) {
          var charOffset = appendToList
              .compose(arrayToString)
              .compose(codePointTo16Bit)
              .compose(codePointAt)
              .apply(offset);
          // var charOffset2 = codePointAt
          //     .andThen(codePointTo16Bit)
          //     .andThen(arrayToString)
          //     .andThen(appendToList)
          //     .apply(offset);
          offset += moveMetric.apply(charOffset);
        }
        return strings;
      }
    };

    System.out.println(function.apply(input));
  }

  private static void java8(String input) {
    Function<String, List<String>> function = s -> s
        .codePoints()
        .mapToObj(Character::toChars)
        .map(String::valueOf)
        .collect(Collectors.toList());

    System.out.println(function.apply(input));
  }

  private static void java11(String input) {
    Function<String, List<String>> function = (@NotNull var s) -> {
      var codePoints = s.codePoints();
      return codePoints
          .mapToObj(Character::toString)
          .collect(Collectors.toList());
    };

    var          var   = 1;
    List<String> apply = function.apply(input);
    System.out.println(apply);

    // var anonymous = new Object() {
    //
    //   void processFile() {
    //     async.openFile(this::secondCallback);
    //   }
    //
    //   void secondCallback(File file) {
    //     // check file properties
    //     async.processFile(this::thirdCallback);
    //   }
    //
    //   void thirdCallback(File file) {
    //     // do on file
    //   }
    // };
    // anonymous.processFile();

    // var name = "Java 11";
    // assert name instanceof String;
    // var chars = name.toCharArray();
    // for (var ch : chars) {
    //   System.out.println(ch);
    // }

    // var number;
    // var someList = null;
    // var isLongString = (String s) -> s.length() > 10;
  }

  private static void java11_wrong(String input) {
    Function<String, List<String>> function = (@NotNull var s) -> s.chars()
        .mapToObj(Character::toString)
        .collect(Collectors.toList());

    System.out.println(function.apply(input));
  }

}

interface Interface {

  default int addEvenNumbers(int... nums) {
    return add(this::isEven, nums);
  }

  default int addOddNumbers(int... nums) {
    return add(this::isOdd, nums);
  }

  private boolean isEven(int n) { return n % 2 == 0; }

  private boolean isOdd(int n)  { return !isEven(n); }

  private static int add(IntPredicate predicate, int... nums) {
    return IntStream.of(nums).filter(predicate).sum();
  }
}

class TryWithResource {

  public static void main(String[] args) throws Exception {
    java6();
    java7();
    java9();

    List.of("a", "b", "c");
    Set.of("a", "b", "c");
    Map.of("a", 1, "b", 2, "c", 3);
    List.copyOf(new ArrayList<>());
  }

  private static void java6() throws IOException {
    Closeable closeable = null;
    try {
      closeable = new StringReader("something");
      // use resource
    } finally {
      if (closeable != null) {
        closeable.close();
      }
    }
  }

  private static void java7() throws Exception {
    try (AutoCloseable closeable = new StringReader("something")) {
      // use resource
    }
  }

  @Deprecated(forRemoval = true)
  private static void java9() throws Exception {
    AutoCloseable closeable = new StringReader("something");
    try (closeable) {
      // use resource
    }
  }
}

class ExternalProcess {

  public static void main(String[] args) throws IOException, InterruptedException {
    filterProcess();
    processInfo();
  }

  public static void filterProcess() {
    Optional<String> currUser = ProcessHandle.current().info().user();

    Comparator<ProcessHandle> parentComparator = (p1, p2) -> {
      long pid1 = p1.parent().map(ProcessHandle::pid).orElse(-1L);
      long pid2 = p2.parent().map(ProcessHandle::pid).orElse(-1L);
      return Long.compare(pid1, pid2);
    };
    Consumer<ProcessHandle> showProcess = ExternalProcess::showProcess;
    ProcessHandle.allProcesses()
        .filter(p -> p.info().user().equals(currUser))
        .sorted(parentComparator)
        .peek(showProcess)
        .forEach(p -> {
          System.out.println(p.children().count());
          System.out.println(p.descendants().count());
        });
  }

  static void showProcess(ProcessHandle ph) {
    ProcessHandle.Info info = ph.info();
    System.out.printf("pid: %d, user: %s, cmd: %s%n",
                      ph.pid(), info.user().orElse("none"), info.command().orElse("none"));
  }


  public static void processInfo() throws IOException {
    ProcessBuilder     pb   = new ProcessBuilder("neofetch");
    String             na   = "<not available>";
    Process            p    = pb.start();
    ProcessHandle.Info info = p.info();

    Optional<String>   command     = info.command();
    Optional<String>   commandLine = info.commandLine();
    Optional<Instant>  startTime   = info.startInstant();
    Optional<String>   user        = info.user();
    Optional<Duration> cpuDuration = info.totalCpuDuration();
    Optional<String[]> arguments   = info.arguments();
  }
}

class Optionals {

  private static class Config {

    @Nullable
    private String redirect;

    private String getRedirect() {
      return redirect;
    }
  }

  public static void main(String[] args) {
    ifPresentOrElse();
  }

  private static void ifPresentOrElse() {
    var config = new Config();

    String redirect = config.getRedirect();
    if (Boolean.parseBoolean(redirect)) {
      System.out.println("redirect to backend");
    } else {
      System.out.println("nothing get redirected");
    }

    Optional.ofNullable(config.getRedirect())
        .map(Boolean::valueOf)
        .ifPresentOrElse(
            e -> System.out.println("redirect to backend"),
            () -> System.out.println("nothing get redirected"));
  }

  private static void or() {
    var config       = new Config();
    var secondConfig = new Config();

    String configStr = config.getRedirect();
    if (configStr == null) {
      configStr = secondConfig.getRedirect();
    }
    if (Boolean.parseBoolean(configStr)) {
      System.out.println("redirect to backend");
    } else {
      System.out.println("nothing get redirected");
    }

    Optional.<String>empty()
        .or(() -> Optional.ofNullable(config.getRedirect()))
        .or(() -> Optional.ofNullable(secondConfig.getRedirect()))
        .map(Boolean::valueOf)
        .ifPresentOrElse(
            e -> System.out.println("redirect to backend"),
            () -> System.out.println("nothing get redirected"));
  }
}

class StackExample {

  void method1() {
    method2();
  }

  private void method2() {
    Runnable runnable = this::method3;
    runnable.run();
  }

  private void method3() {
    Class<?> callerClass = StackWalker.getInstance(Option.RETAIN_CLASS_REFERENCE)
        .getCallerClass();
    Logger logger = LogManager.getLogger(callerClass);

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
  }

  public static void main(String[] args) {
    new StackExample().method1();
  }
}


interface ClosableSupplier {

  static <T extends Closeable & Appendable>
  T appendTo(T supplier) throws IOException {
    try (supplier) {
      supplier.append('m');
    }
    return supplier;
  }

  static void myExample() throws IOException {
    var example1 = appendTo((Closeable & Appendable) new StringWriter());
    var example2 = appendTo(new StringWriter());
    example1.close();
    example2.close();
  }
}
