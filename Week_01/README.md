学习笔记

### 类加载器
1. 启动类加载器 `bootstrapClassLoader`，加载 `jdk/lib` 核心类
2. 扩展类加载器 `extClassLoader`，加载 `jdk/lib/ext` 扩展包下的类
3. 应用类加载器 `ApplicationClassLoader` ，加载应用程序中自行编写的类

对于任意一个类，都必须由它的类加载器 `classloader` 以及类本身来确定它在 `JVM` 虚拟机中的唯一性。
类加载器不同时，即使加载的同一个 `class` 文件，也会被当作两个不同的类：
```
public class ClassLoaderTest {

    public static void main(String[] args) throws Exception {
        ClassLoader classLoader = new ClassLoader() {
            @Override
            public Class<?> loadClass(String name) throws ClassNotFoundException {
                try {
                    String fileName = name.substring(name.lastIndexOf('.')+1)+".class";
                    InputStream inputStream = ClassLoaderTest.class.getResourceAsStream(fileName);
                    if(inputStream == null){
                        return super.loadClass(name);
                    }
                    byte[] b = new byte[inputStream.available()];
                    inputStream.read(b);
                    return defineClass(name,b,0,b.length);
                } catch (IOException e) {
                    throw new  ClassNotFoundException();
                }
            }
        };
        Object o = classLoader.loadClass("demo.java0104.ClassLoaderTest").newInstance();
        System.out.println(o.getClass());
        System.out.println(o instanceof demo.java0104.ClassLoaderTest);
    }
}
-----------------------------------
class test.ClassLoaderTest
false
```

**双亲委派机制**

当一个类收到类加载请求时，它首先不会自己去尝试加载这个类，而是把这个请求委派给父类加载器去完成。该机制可以保证同一个类不会被多次加载，保证唯一性。

### GC
垃圾回收，通常是指**堆和方法区**的垃圾回收策略，**堆(回收对象)** 占主要空间，**方法区（回收废弃常量和不再使用的类型）** 回收的空间和效率相对来说更低。

**当一个对象被判断为 `GC roots` 引用不可达时，会被标记为可回收，由相应的 `GC` 回收算法进行具体的回收**。
- `serial`、`serial Old` 串行收集器，适用小内存的应用，只能使用单核资源， `GC` 时一定会 `STW` ，直到结束。
- `parNew` 新生代并行收集， `parNew` 是启用 `CMS` 时，默认的新生代收集器。
- `paralell`、 `paralell Old` 吞吐量优先的并行策略。
- `CMS` 老年代收集器，短停顿优先
- `G1` 在实现思想有区别，不再明确区分新生代、老年代，统一采用 `region` 来命名，按块回收。
- 新版 `JDK` 的低延迟收集器 `ZGC` 、 `shenandoah` 。 号称最大停顿不超过 `10ms` ，适用于超大内存的垃圾回收， `20G+`


### JVM参数
- `Xmx` 的默认值为机器内存的 `1/4`
- `java8` 的默认收集器是 `paralell` 并行收集
- 默认情况下 `jvm` 会开启自适应 `AdapterSizePolicy` (生产环境推荐开启)，动态的调整年轻代各区域大小。参数 `-XX:-UseAdaptiveSizePolicy` 可以关闭，`-XX:+UseAdaptiveSizePolicy` 表示开启
- 未指定 `Xmn` 参数时，`eden：from:to` 的大小不会一开始就达到最大值，比例可能也不是准确的 `8：1：1`
```
CMS GC的 默认GC进程数是怎么来的？
区分young区的parnew gc线程数和old区的cms线程数，分别为以下两参数：
-XX:ParallelGCThreads=m
-XX:ConcGCThreads=n 

其中ParallelGCThreads 参数的默认值是：
CPU核心数 <= 8，则为 ParallelGCThreads=CPU核心数
CPU核心数 > 8，则为 ParallelGCThreads = CPU核心数 * 5/8 + 3 向下取整
16核的情况下，ParallelGCThreads = 13
32核的情况下，ParallelGCThreads = 23
64核的情况下，ParallelGCThreads = 43
72核的情况下，ParallelGCThreads = 48

ConcGCThreads的默认值则为：
ConcGCThreads = (ParallelGCThreads + 3)/4 向下去整。
ParallelGCThreads = 1~4时，ConcGCThreads = 1
ParallelGCThreads = 5~8时，ConcGCThreads = 2
ParallelGCThreads = 13~16时，ConcGCThreads = 4
```
