### 被移除的组件

- Applets, Browser Plugin
- JavaFX 🗃
- java.corba
- java.transaction 🗃
- java.activation 🗃
- java.xml.bind 🗃
- java.xml.ws 🗃
- java.xml.ws.annotation 🗃

---

### Illegal Reflective Access

```
WARNING: An illegal reflective access operation has occurred
WARNING: Illegal reflective access by $PERPETRATOR to $VICTIM
WARNING: Please consider reporting this to the maintainers of Diamond
WARNING: Use --illegal-access=warn to enable warnings of further illegal reflective access operations
WARNING: All illegal access operations will be denied in a future release
```

- `--illegal-access`
- `--add-opens`
- `--add-exports`

---
<!-- .slide: class="center" -->

### `--illegal-access`

- `permit`:
  未来可能会移除。仅在第一次反射调用内部api的时候报警
- `warn`:
  每次次反射调用内部api的时候报警
- `debug`:
  在warn的基础上，加上堆栈输出
- `deny`:
  拒绝所有非法反射访问内部api

---
<!-- .slide: class="center" -->

### `--add-opens` `--add-exports`

在编译期或运行期添加

    --add-exports <source-module>/<package>=<target-module-list>
    // 将模块中的包导出到所有或其他模块
    --add-opens <source-module>/<package>=<target-module-list>
    // 使模块里面的包对其他模块开放

---
<!-- .slide: class="center" -->

### 版本要求

- IntelliJ IDEA 2018.2
- Spring Boot 2.1.0.M2
- Spring Framework 5.1
- Hibernate 5.4
- maven-compiler-plugin 3.7.0
- Gradle 5.0
- Cloudera 6.3
- Hadoop 3.3.0-SNAPSHOT

---
<!-- .slide: class="center" -->

📌不一定使用模块化开发
