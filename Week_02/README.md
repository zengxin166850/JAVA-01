### 笔记整理




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