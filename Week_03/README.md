### 1.`Doug lea nio` 读书笔记

无论是简单的 `web` 服务，还是分布式系统，他们的基础网络服务是相通的，不外乎以下几个步骤：

- Read
- decode
- Process
- Encode
- Send

传统的 `socket` 设计中由于阻塞问题，这五个步骤都是一个线程在处理（`handler`）,处理连接请求时，虽然可以用单个线程、线程池、或者每个连接一个线程来处理，但均无法做到高性能，除去 `read` 和 `write` 之外，中间的步骤 `CPU` 均在等待，浪费了资源。

**解决办法：**

拆分为小任务，每个任务单独完成，互相不阻塞，于是计算机在底层为了支持 `NIO`，实现了如下功能：

- 非阻塞的 `read`、`write`
- 将任务调度（`dispatch`）给感兴趣的、相关联的 `IO` 事件

**事件驱动的特点：**

- 使用更少的资源，不需要为每个客户端连接开一个线程
- 更小的开销，更少的上下文切换、加锁解锁操作等
- 必须手动绑定事件状态，`read` 、`write` 、`accept` 等
- 编码上更加困难

#### 基于事件驱动的 `IO-Reactor` 模式

为了响应 `IO` 事件，`Reactor` 需要调度与事件对应的 `handler`，此过程类似于 `AWT`  框架中的线程（`Thread`）,而处理器（`handler`）的运行则类似于 `AWT` 的事件监听器 `ActionListeners`  。同理，为 `handler` 绑定 `IO` 事件的过程则是 `addActionListener`。

> `AWT` 为 开发 `java` 窗口应用的一套 `API` 框架

**`java nio api` 中的相关概念：**

- `channels`，通向文件、`socket` 的一系列**双向**通道
- `buffers`，一个对象数组，可以被 `channel` 直接读取、写入
- `selectors`，通知哪一组 `channel` 有 `IO` 事件
- `selectionKeys`，管理 `IO` 事件的状态和绑定信息

一个单线程的 `reactor` 设计如下：

- 主进程为统一入口，reactor 根据事件驱动将任务转发给 handler；
- handler 根据事件类型，采用多路复用方式，调用对应的 read 、write 方法。

![](https://note.youdao.com/yws/api/group/102464226/file/909738149?method=download&inline=true&version=1&shareToken=97250695C9C44462BE5CF02A0E05E43E)



采用多个工作线程：

- 进一步的将与 IO事件不相关的任务分离开，使用 worker 线程池来完成 process（decode、compute、encode） 过程；

- 将工作线程分离之后，主进程的 reactor 就可以更快速的将任务进行分发，另外，使用线程池也易于调整和管理。

  ![worker thread pool](https://note.youdao.com/yws/api/group/102464226/file/909841599?method=download&inline=true&version=1&shareToken=97250695C9C44462BE5CF02A0E05E43E)

多 reactor 方式：

- 使用多个 Reactor，减小访问数量过大时，单个reactor的压力，不同Reactor之间可以根据 CPU 和 IO 能力进行负载均衡；
- 可以尝试为每个 handler增加回调，添加队列（跨阶段传输buffer），以及通过 Future 异步获取结果等。





![](https://note.youdao.com/yws/api/group/102464226/file/909840618?method=download&inline=true&version=1&shareToken=97250695C9C44462BE5CF02A0E05E43E)



### 2.第五课思维导图：

![](https://note.youdao.com/yws/api/group/102464226/file/909865016?method=download&inline=true&version=1&shareToken=97250695C9C44462BE5CF02A0E05E43E)

### 3.第六课思维导图：

![](https://note.youdao.com/yws/api/group/102464226/file/909867200?method=download&inline=true&version=1&shareToken=97250695C9C44462BE5CF02A0E05E43E)

#### 线程池工作流程：

当提交一个新任务到线程池中，线程池的处理流程如下：

- 判断线程是否已经达到核心线程数，如果当前池中线程数少于**核心线程数**，创建一个新线程来执行，否则进入下一个流程。
- 核心线程数已满，判断**工作队列**是否已满，如果未满，则将任务放入队列中，如果队列已满，则进行下一个流程。
- 工作队列已满，判断当前池中线程数是否已达到**最大线程数**，如果未达到，则创建新的线程并执行任务，如果已达到最大线程数，则任务会被**拒绝**，执行拒绝策略，同时提交任务失败。
- 最后，当其他的线程执行完任务时，会进入空闲状态，如果队列中有任务，会取出来执行，当队列为空之后，超过**空闲存活时间**的线程会被回收。

![](https://note.youdao.com/yws/api/group/102464226/file/909868153?method=download&inline=true&version=1&shareToken=97250695C9C44462BE5CF02A0E05E43E)

![](https://note.youdao.com/yws/api/group/102464226/file/909868170?method=download&inline=true&version=1&shareToken=97250695C9C44462BE5CF02A0E05E43E)