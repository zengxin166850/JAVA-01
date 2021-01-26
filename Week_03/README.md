### `Doug lea nio` 笔记整理

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

一个单线程的 `reactor` 设计如下：

![](https://note.youdao.com/yws/api/group/102464226/file/909738149?method=download&inline=true&version=1&shareToken=97250695C9C44462BE5CF02A0E05E43E)

**`java nio api` 中的相关概念：**

- `channels`，通向文件、`socket` 的一系列**双向**通道
- `buffers`，一个对象数组，可以被 `channel` 直接读取、写入
- `selectors`，通知哪一组 `channel` 有 `IO` 事件
- `selectionKeys`，管理 `IO` 事件的状态和绑定信息

详细实现见文件 `Reactor.java`