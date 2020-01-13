## 字符串性能提升

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

------

## 垃圾收集器

默认使用G1回收器

- 并行的 Full GC
- 相比Java8 G1 和 并行 GC 分别有 16.1% 和 4.5% 的提升

![g1_on_jdk11](g1_on_jdk11.png)

---

### 试验性的ZGC

![zgc-performance](zgc-performance.png)

<https://cr.openjdk.java.net/~pliden/slides/ZGC-FOSDEM-2018.pdf>

------
<!-- .slide: class="center" -->

## 安装包体积大幅下降

🔖 没有JRE 没有32位

最小的HelloWorld可执行程序可以做到40MB
