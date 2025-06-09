# todoApi

## 功能

一个todo列表的后端服务, 提供如下接口

- 新增任务
- 修改任务
- 删除任务
- 查询所有任务
- 按照截止日期查询任务

## 运行

1. 下载源码, 使用git 或者下载源码zip
2. 进入todoApi目录
3. 运行命令 `npm i`
4. 运行命令 `npm run start`

> 接口文档在`/api-docs` 路由中

## 常见问题

### 端口占用

**现象**

```shell
Server failed to start: Error: listen EADDRINUSE: address already in use :::3000
    at Server.setupListenHandle [as _listen2] (node:net:1912:16)
```

**解决方法**

运行下方命令, 修改PORT后面的值

```shell
npx cross-env PORT=4000 npm run start
```



