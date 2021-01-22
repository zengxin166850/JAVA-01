### GC日志分析及调优

`GC` 相关的常见概念：

- “分代假设” 思想，绝大部分对象的生命周期都非常短暂,存活时间短，在这个假设下，`JVM` 将堆内存分为年轻代和老年代两个部分；

- `young gc` (minorGC` ) ，年轻代发生的 `GC； 
- `full gc` 清理年轻代+老年代（ `majorGC` ），会发生 `STW` ；
- `STW` 在清理堆内存时所发生的全局暂停事件，在  `STW` 的过程中，应用程序不会进行工作，需要等到暂停结束才恢复，现代的 `GC` 收集器都在极力减小 `STW` 的时间。

打印 `GC` 日志的参数：

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

JDK8的 `GC`收集器组合：

- `serial + serialOld`，配置参数为 `-XX:+UseSerialGC` ；
- serial + CMS，配置参数为  `-XX:+UseSerialGC` ,`-XX:+UseConcMarkSweepGC`，
- `parNew + CMS`，配置参数为 `-XX:+UseConcMarkSweepGC`，默认的年轻代即 `ParNew`
- `parallel`组合，配置参数为 `-XX:+UseParallelGC` ，默认情况下parallel会开启自适应参数 `-XX:+UseAdaptiveSizePolicy`，自动调节分代的大小。
- G1，不强调分代概念，采用统一的回收策略，将堆内存划分为 region 区域进行回收，可以预配置 STW 的最长时间

### 演练 GCLogAnalysis 

GCLogAnalysis该类只运行1s，所以，发生gc时所占用的总时间会极大的影响生成对象的数量

我的机器配置为：24GB内存、6核心6线程CPU，分别测试512m、1g、2g、4g、8g的结果如下

内存设置\对象数量 | serial | parallel | CMS | G1
---|---|---|---|---
512M | 10000 |  9000 |  10000 | 10000
1G | 12500 |  15000 |  15000 | 15500
2G | 12500 |  16500 |  13500 | 14000-17000
4G | 8000 |  15000 |  13500 | 15000-18000
8G | 8000 |  7800 |  12500-13500 | 15000-18000

- 并行GC在配置参数 -XX:ParallelGCThreads=4 并行线程的数量时，由于我的机器只有6核心，默认的GC线程数量为6，调低该值时，可以明显发现GC占用的时间增多，生成对象的数量显著降低
- CMS 的年轻代大小在设置为2g往上时年轻代的大小就固定为了415M ，且只发生年轻代GC了
- 从表格中可以看出，在短时间内，G1的数值范围波动比较大，随机性较大，但内存设置的比较大之后，整体数量比较稳定，没有明显的下降趋势
- 在内存中等的情况下，parallel比CMS稍有优势，

### gateway-server演练
```java
@RestController
public class HelloController {

    @GetMapping("/api/hello")
    public String sayHello(HttpServletRequest request){
//        String code = request.getHeader("exchange-cloud");
        return "hello world";
    }
}
```