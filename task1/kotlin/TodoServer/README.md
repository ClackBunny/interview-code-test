# TodoServer

这个项目使用 [Ktor Project Generator](https://start.ktor.io).创建

## 功能

> **在项目运行后, 访问 `host:port/swagger` 路由访问swagger, 或者 `/api.json` 获得接口的json格式描述**

提供一个Todo的后端服务, 接口遵循RESTful接口风格, 包括以下功能

- 根据ID获取任务
- 根据截止日期查询所有任务
- 新增任务
- 根据ID更新任务
- 根据ID删除任务

认证相关

- 登录接口
- 修改密码接口

## 构建

To build or run the project, use one of the following tasks:

| Task                         | Description                                           |
| ---------------------------- | ----------------------------------------------------- |
| `./gradlew test`             | Run the tests                                         |
| `./gradlew build`            | Build everything                                      |
| try                          | Publish the docker image locally                      |
| **`run`**                    | **Run the server**                                    |
| `runDocker`                  | Run using the local docker image                      |
| **`./gradlew shadowJar`**    | **Build a fatJar with all dependencies included**     |
| **`./gradlew jlinkRuntime`** | **Create a minimal runtime environment**              |
| **`./gradlew packageApp`**   | **Package a program that can be run directly (.exe)** |

### 构建jar包

运行命令 `./gradlew shadowJar` 会在`build/libs`下创建一个`TodoServer-all.jar`,  可以使用 `java -jar TodoServer-all.jar` 命令进行运行 

### 构建二进制程序

运行命令`./gradlew packageApp` 会在 `build/jpackage` 下创建一个`TodoServer`目录, 目录包含了必要的运行环境和程序, 可以直接点击`.exe` 文件运行

## 运行

这里有三种运行方式

### source code

> 推荐是相关开发人员使用此方法

通过git拉去最新代码

```shell
git clone git@github.com:ClackBunny/interview-code-test.git
```

终端中运行

> 请确保你已经安装了JDK, 并且配置了相关环境

```shell
./gradlew run
```

### Jar

> 推荐已经有Java环境的, 不想查看源码的人员使用此方法

到release中下载对应的`.jar`包, 然后在本地的终端中运行, 其中的 *TodoServer.jar* 根据实际情况进行替换

```shell
java -jar TodoServer-all.jar
```

### exe

> 推荐没有环境, 只想运行服务的用户

1. 到release中下载对应的 `.zip`,
2. 解压缩 `zip` 文件
3. 进入到解压缩的文件夹(`TodoServer`)
4. 双击`TodoServer.exe` 运行即可, 关闭命令行工具(黑框) 就退出服务

### 成功样例

如果项目运行成功, 你会看到如下样式的代码

```

2025-06-08 17:20:15.545 [main] INFO  Application - Loaded port = 8180
2025-06-08 17:20:15.808 [main] INFO  Exposed - Preparing create tables statements took 15ms
2025-06-08 17:20:15.821 [main] INFO  Exposed - Executing create tables statements took 12ms
....
2025-06-08 17:20:16.344 [main] INFO  Application - Application started in 1.45 seconds.
2025-06-08 17:20:16.638 [main] INFO  Application - Responding at http://127.0.0.1:8180
```

## 常见问题

### 端口占用

> 服务默认端口 8180

**现象**

```shell
2025-06-08 17:45:47.460 [main] INFO  Application - Application started in 1.109 seconds.
Exception in thread "main" java.net.BindException: Address already in use: bind
        at java.base/sun.nio.ch.Net.bind0(Native Method)
        at java.base/sun.nio.ch.Net.bind(Net.java:565)
        at java.base/sun.nio.ch.ServerSocketChannelImpl.netBind(ServerSocketChannelImpl.java:344)
        at java.base/sun.nio.ch.ServerSocketChannelImpl.bind(ServerSocketChannelImpl.java:301)
        at io.netty.channel.socket.nio.NioServerSocketChannel.doBind(NioServerSocketChannel.java:141)
        at io.netty.channel.AbstractChannel$AbstractUnsafe.bind(AbstractChannel.java:561)
        at io.netty.channel.DefaultChannelPipeline$HeadContext.bind(DefaultChannelPipeline.java:1281)
        at io.netty.channel.AbstractChannelHandlerContext.invokeBind(AbstractChannelHandlerContext.java:600)
        at io.netty.channel.AbstractChannelHandlerContext.bind(AbstractChannelHandlerContext.java:579)
        at io.netty.channel.DefaultChannelPipeline.bind(DefaultChannelPipeline.java:922)
        at io.netty.channel.AbstractChannel.bind(AbstractChannel.java:259)
        at io.netty.bootstrap.AbstractBootstrap$2.run(AbstractBootstrap.java:380)
        at io.netty.util.concurrent.AbstractEventExecutor.runTask(AbstractEventExecutor.java:173)
        at io.netty.util.concurrent.AbstractEventExecutor.safeExecute(AbstractEventExecutor.java:166)
        at io.netty.util.concurrent.SingleThreadEventExecutor.runAllTasks(SingleThreadEventExecutor.java:472)
        at io.netty.channel.nio.NioEventLoop.run(NioEventLoop.java:569)
        at io.netty.util.concurrent.SingleThreadEventExecutor$4.run(SingleThreadEventExecutor.java:998)
        at io.netty.util.internal.ThreadExecutorMap$2.run(ThreadExecutorMap.java:74)
        at io.netty.util.concurrent.FastThreadLocalRunnable.run(FastThreadLocalRunnable.java:30)
        at java.base/java.lang.Thread.run(Thread.java:1583)
```

#### 解决方法

##### 关闭占用8180端口的进程

**Windows**

```shell
netstat -ano |findstr 8180
#  TCP    0.0.0.0:8180           0.0.0.0:0              LISTENING       35348
#  TCP    [::]:8180              [::]:0                 LISTENING       35348
# 记住进程的PID(最后一个数字, 35348),下面的youPID替换成正确的PID
tasklist | findstr youPID
# 查看具体的进程是什么
# java.exe                     35348 Console                    3    126,488 K
# 可以手动关闭对应的程序,或者使用taskkill强行关闭进程, 请谨慎操作, 这个操作可能引起系统不稳定,youPID替换成具体的PID
taskkill /F /PID youPID
```

**Linux**

```shell
# 查询什么进程占用了端口
lsof -i :8180
#COMMAND   PID USER   FD   TYPE DEVICE SIZE/OFF NODE NAME
#java     12345 user   45u  IPv6  123456      0t0  TCP *:http-alt (LISTEN)
# 强制关闭进程,12345是进程的PID
kill -9 12345
```

##### 换个端口启动服务

**source code**

源码用户直接修改`task1/kotlin/TodoServer/src/main/resources/application.yaml` 文件中的 `port` 值

**Jar 和exe 用户**

在`Jar` 和 `exe` **同目录下**, 创建一个`application.yaml` 文件, 并在里面输入如下内容(注意缩进), 里面的`port` 值可以改成你需要的

```shell
文件结构如下
TodoServer
|    application.yaml
|    TodoServer-all.jar
|    TodoServer.exe
```

`application.yaml` 文件如下

```yaml
ktor:
  deployment:
#    把这个port改成你想要的值
    port: 8180
```

### Could not determine java version

> 用自己的gradle启动项目, gradle版本不足导致的

#### 解决方法

1. 升级gradle
2. 使用项目自带的 gradle-wraper(./gradlew)

### 依赖下载失败

1. 检查网络(魔法上网)
2. 更换镜像, 修改`build.gradle.kts` 中的`repositories`(项目自带的已经是阿里云镜像)

### 找不到主类

检查 `build.gradle.kts` 中的 `mainClass` 设置
