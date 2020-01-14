## 字符串性能提升

压缩字符串

```java
public final class String {
    /**
     * The value is used for character storage.
     */
    private final byte[] value;
    
    /**
     * The identifier of the encoding used to
     * encode the bytes in {@code value}.
     * The supported values in this implementation are
     * LATIN1 / UTF16
     */
    private final byte coder;
```

String: `char[]` => `byte[]` (with encoding)

---

### 性能测试

创建了1千万个字符串，并且合并这些字符串。

    Generated 10000000 strings in 854 ms.
    Created string of length 488895 in 5130 ms.

类似的，如果我们通过 -XX:-CompactStrings 参数禁用 Compact Strings 得出如下输出：

    Generated 10000000 strings in 936 ms.
    Created string of length 488895 in 9727 ms.

<https://reionchan.github.io/2017/09/25/java-9-compact-string>

------

<!-- .slide: class="center" -->
## 虚拟机性能提升

- JEP-143 改善锁争用机制
- JEP-197 代码分段缓存
- JEP-310 应用类数据共享

---

### 垃圾收集器

默认使用G1回收器

![g1_region.png](g1_region.png)

<https://tech.meituan.com/2016/09/23/g1.html>

---

![how_g1_works](how_g1_works.png)

- 更高的并行化
- 可预测的停顿时间
- 更少的内存碎片

---

- JEP-307 并行的 Full GC
- 相比Java8 G1 和 并行 GC 分别有 16.1% 和 4.5% 的提升

![g1_on_jdk11](g1_on_jdk11.png)

---

### 试验性的ZGC

![zgc-performance](zgc-performance.png)

<https://cr.openjdk.java.net/~pliden/slides/ZGC-FOSDEM-2018.pdf>

---

<div class="tweet" data-src="https://twitter.com/0xd33d33/status/1034722011032027136"></div>

---

![gc-benchmarks](gc-benchmarks.png)

不断分配内存空间并且一同释放

<https://ionutbalosin.com/2019/12/jvm-garbage-collectors-benchmarks-report-19-12/>
