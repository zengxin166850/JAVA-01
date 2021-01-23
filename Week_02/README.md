### GC日志分析及调优

**jvm 如何判断对象是否可以回收**：

- 引用计数法，每引用一次计数器加一，失效时减一，问题是无法解决循环依赖；

- 可达性分析，以 `GC roots` 为起点，根据引用关系向下查找，无法与 `GC Roots` 连接的对象被初步判断为“可回收”。

> java中的引用分为强引用、软引用、弱引用、虚引用四种，引用强度依次递减。

**`GC` 相关的常见概念：**

- “分代假说” 思想，绝大部分对象的生命周期都是朝生夕灭，经过越多次垃圾收集过程的对象就越难以消灭，在这个假设下，`JVM` 将堆内存分为年轻代和老年代两个部分；

- `young gc` (`minorGC `) ，年轻代发生的 ` GC`； 
- `full gc` 清理年轻代+老年代（ `majorGC` ），会发生 `STW` ；
- `STW` 在清理堆内存时所发生的全局暂停事件，在  `STW` 的过程中，应用程序不会进行工作，需要等到暂停结束才恢复， `GC` 收集器的发展方向也是在向着减小 `STW` 的时间。

**JDK8的 `GC`收集器组合**：

- `serial + serialOld`，配置参数为 `-XX:+UseSerialGC` ；
- `parNew + CMS`，配置参数为 `-XX:+UseConcMarkSweepGC`，默认的年轻代即 `ParNew`；
- `parallel`组合，配置参数为 `-XX:+UseParallelGC` ，默认情况下 `parallel`会开启自适应参数 `-XX:+UseAdaptiveSizePolicy`，自动调节分代的大小；
- `G1`，配置参数为 `-XX:+UseG1GC` ，不强调分代概念，采用统一的回收策略，垃圾优先，将堆内存划分为 `region` 按区域进行回收。

> G1 的跨 region 引用是如何解决的？
>
> 跨 region 引用和跨代引用问题类似，为了避免扫描全堆的 GC Roots ，采用了记忆集保存引用关系，G1 为每个 region 都保存了一个RS （Remember Set），类似于哈希结构，key存储着 “我引用了谁”，对应的 value 为“谁引用了我” 的双向卡表关系。这也导致G1收集器本身所需占用的内存高于其他收集器。

并发的可达性分析问题：可以借助黑色、灰色、白色的"三色标记"分析过程，如果 `GC` 在标记的同时并发的修改了引用关系，就可能会导致 “对象消失”问题（即应该为黑色的被标记为了白色），该问题可以使用增量更新（记录新增的引用，结束后扫描这部分）和原始快照（`SATB` 记录删除的引用，结束后扫描这部分）来解决，`G1` 收集器使用的就是原始快照方式。

![](https://note.youdao.com/yws/api/group/102464226/file/909661154?method=download&inline=true&version=1&shareToken=97250695C9C44462BE5CF02A0E05E43E)

**如何打印 `GC` 日志**：

- `-XX:+PrintGCDetails` 打印  `GC` 信息在控制台；
- `-Xloggc:gc.demo.log` 输出 `GC` 信息到文件中；
- `-XX:+PrintGCDateStamps`  打印具体的时间信息。

如下所示分别为一次 `young gc` 和 `full gc` 的日志信息：

```
..........

2021-01-22T17:36:58.345+0800: [GC (Allocation Failure) [PSYoungGen: 157384K->40575K(232960K)] 737874K->657395K(932352K), 0.0255286 secs] [Times: user=0.11 sys=0.00, real=0.03 secs] 
2021-01-22T17:36:58.371+0800: [Full GC (Ergonomics) [PSYoungGen: 40575K->0K(232960K)] [ParOldGen: 616819K->353250K(699392K)] 657395K->353250K(932352K), [Metaspace: 3559K->3559K(1056768K)], 0.0788716 secs] [Times: user=0.16 sys=0.00, real=0.08 secs] 

```

`Allocation Failure` 表示发生此次 `GC`的原因， `157384K->40575K(232960K)`为回收前年轻代 `157M`，回收后`40M`，年轻代的总量为 `232M`，本次 `gc` 耗时 `0.02s`，跟在后面的为整个堆的回收情况。

**线程堆栈分析：**

可以使用 `jstack -l`, `jcmd` , `jconsole` 等工具将堆栈日志导出到文件中查看，重点关注 `Blocked` ，`deadLock`，`Waiting on condition`，`Waiting on monitor entry` 这几个状态。

下图为测试环境中数据库死锁后， 应用启动时卡在中间无响应 ，采用 `jstack`工具 `dump` 出来的堆栈日志，（`0x00000003d7365978` 这个锁一直未释放）:



![image](https://note.youdao.com/yws/api/group/102464226/file/906765438?method=download&inline=true&version=1&shareToken=97250695C9C44462BE5CF02A0E05E43E)


![image](https://note.youdao.com/yws/api/group/102464226/file/906764520?method=download&inline=true&version=1&shareToken=97250695C9C44462BE5CF02A0E05E43E)

**GC测试总结：**

- 1.通常情况下`youngc` 次数多余 `fullgc` 次数，当堆内存设置的很小时，内存不足以分配（`Allocation failure`）而导致的  `GC` 会很频繁，内存越不足，`GC` 越频繁，当回收赶不上对象新增的效率时，就会发生 `OOM`；

- 2.小内存下，串行化收集 `SerialGC` 的效果意外的很不错，`parallel` 的效果较差；
- 3.并行参与GC的线程数 `-XX:ConcGCThreads` ，会极大地影响parallel、CMS、G1 的效率；
- 4.`CMS`、`G1` 的 `GC` 日志中都有很明显的阶段信息，如 `initial Mark`、`concurrent-mark`、`final mark` 等，除开初始标记和最终标记阶段需要 `STW` 外，其余阶段与应用程序是并行运行的；

- 5.堆内存设置的过大时，虽然 `GC` 的次数更少，但是每次 `GC` 的时间也会更长，`STW`  的时间也更长，需要合理设置才行；
- 6.`parallel` 在未设置 `Xms` 参数时，分代的大小会随 `GC` 进行调整，遇到内存不足时才会扩容，所以最好是加上 `Xms` 设置； 
- 7.小内存下，`Serial` 也是有用武之处的，在内存中等的情况下，`parallel` 比 `CMS` 稍有优势，内存偏大时，`CMS` 与 `G1` 有优势，`G1` 最佳。

**问题分析调优经验：**

- 高分配速率: 可以根据 `GC` 之间的时间差，堆内存的增加量来进行计算；

  > 正常系统：分配速率较低 ~ 回收速率 -> 健康  
  > 内存泄漏：分配速率 持续大于 回收速率 -> OOM  
  > 性能劣化：分配速率很高 ~ 回收速率 -> 亚健康

- 过早提升，老年代中的对象存活率低。

  > 正常情况下老年代的对象都应该存活的更长，如果每次回收后老年代都回收了很多的对象，很可能就是出现过早提升了，可以加大年轻代空间，提高晋升年龄



**诊断工具：**

- `Eclipse MAT`
- `jhat`
- `Arthas`

### 2.IO模型与Netty

**传统的 `socket` 通信方式：**

1. 创建服务端 `ServerSocket`，绑定端口，等待客户端的 `Socket`连接;
2. `accept` 接收到客户端的请求，获取客户端的输入流，取出数据；
3. 处理业务逻辑，使用输出流返回数据，关闭 `socket`。

在这种方式下，从 `accept ` 接受连接，处理业务逻辑并返回的整个过程都是阻塞的，单线程的弊端很明显，处理不过来。为每一个客户端启动一个线程来处理又不切实际，因为我们的资源是有限的。使用固定数量的线程池来处理，则完全需要根据机器情况来设置。并不能解决本质问题。



**`IO `模型:**

![](https://note.youdao.com/yws/api/group/102464226/file/909661549?method=download&inline=true&version=1&shareToken=97250695C9C44462BE5CF02A0E05E43E)

- 阻塞 `BIO`，一对一服务，单线程一直等待数据准备好，直到执行完成；
- `IO` 复用，事件驱动，由单线程统一管理  `socket`  连接，当数据准备好后，主动通知用户进程，并开始传输（阻塞）；
- `NIO`，内核立即返回，`CPU` 腾出时间去做其它事情（不阻塞），用户进程不断轮询是否准备好了，当数据准备好后开始传输（阻塞）；
- 信号驱动 `IO` ，用户进程先发送一个信号，随后去做别的事情，等待内核准备好数据后，回复一个信号，随后开始传输；
- 异步非阻塞 `IO`，都异步了，，，，当然不阻塞。。。。。。。。。。。

**`Netty` 特性：**

`java`自身的 `api` 很复杂笨重，而 `Netty` 改善了这一问题，使得网络和业务更容易分离，易维护。

- 高吞吐
- 低延迟
- 低开销
- 零拷贝
- 可扩容

**`Netty` 相关基础概念：**

- `Channel` 通道，`Java NIO` 中的基础概念，代表一个打开的连接,可执行读取/写入 `IO` 操作。`Netty`对  `Channel`  的所有 `IO` 操作都是非阻塞的。
- `ChannelFuture `，`Java` 的 `Future` 接口，只能查询操作的完成情况, 或者阻塞当前线程等待操作完成。`Netty` 封装一个 `ChannelFuture` 接口。我们可以将回调方法传给 `ChannelFuture`，在操作完成时自动执行。
- `Event & Handler Netty` 基于事件驱动，事件和处理器可以关联到入站和出站数据流。
- `Encoder &Decoder` 处理网络 `IO` 时，需要进行序列化和反序列化, 转换 `Java` 对象与字节流。对入站数据进行解码, 基类是 `ByteToMessageDecoder`。对出站数据进行编码, 基类是 `MessageToByteEncoder`。
- `ChannelPipeline` 数据处理管道就是事件处理器链。有顺序、同一 `Channel` 的出站处理器和入站处理器在同一个列表中