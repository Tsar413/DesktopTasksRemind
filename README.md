
# DesktopWorkToDo

## 1. 项目简介

DesktopWorkToDo 是一套面向 Windows 本机使用场景的桌面待办事项管理系统。系统采用 **Spring Boot + MySQL + Electron** 的技术组合，实现了任务数据持久化、网页化管理界面以及桌面悬浮窗展示能力，适用于个人本地任务管理、轻量级桌面待办记录与原型验证等场景。

本项目采用单机部署模式，后端服务、数据库与桌面端均运行在同一台 Windows 设备上。系统整体由以下两部分组成：

* **Spring Boot 后端服务**
  负责业务逻辑处理、数据管理、接口提供及页面渲染。
* **Electron 桌面外壳**
  负责将本地网页界面封装为桌面悬浮窗，并提供拖动、最小化、关闭等桌面交互能力。

---

## 2. 功能概述

当前版本已实现以下核心功能：

### 2.1 任务管理能力

* 支持待完成任务管理
* 支持长时间任务管理
* 支持已完成任务管理
* 支持新增、修改、删除任务
* 支持任务备注查看
* 支持任务状态切换
* 支持已完成任务回撤至待完成状态

### 2.2 页面交互能力

* 支持顶部导航切换任务视图
* 支持任务快捷菜单操作
* 支持列表滚动浏览
* 支持半透明桌面风格展示

### 2.3 桌面端能力

* 支持以 Electron 悬浮窗方式运行
* 支持窗口拖动
* 支持窗口最小化
* 支持窗口关闭
* 支持统一启动后端服务与桌面悬浮窗
* 支持统一关闭后端服务与桌面悬浮窗

---

## 3. 技术栈

### 后端

* Java 8
* Spring Boot 2.1.0.RELEASE
* MyBatis / MyBatis-Plus
* JPA（当前项目中部分实体存在注解混用）
* MySQL

### 前端

* HTML
* CSS
* JavaScript

### 桌面端

* Electron
* Node.js
* npm

---

## 4. 运行环境要求

建议运行环境如下：

* Windows 11
* JDK 8
* Maven
* MySQL 5.7 或 8.x
* Node.js
* npm

---

## 5. 项目结构

建议项目按以下方式组织：

```text
DesktopWorkToDoShell/
├─ SpringProgram/
│  ├─ study-workToDo-0.0.1-SNAPSHOT.jar
│  ├─ start-worktodo-all.bat
│  ├─ stop-worktodo-all.bat
│
├─ Desktop/
│  ├─ main.js
│  ├─ preload.js
│  ├─ package.json
│  ├─ package-lock.json
│  └─ node_modules/
│
└─ SourceCode/
   └─ study-workToDo/
```

### 目录说明

* `SpringProgram`：存放 Spring Boot 打包产物及统一启停脚本
* `Desktop`：存放 Electron 桌面外壳源码
* `SourceCode`：存放 Spring Boot 工程源码

---

## 6. Spring Boot 部分说明

### 6.1 打包插件说明

项目使用 Maven 打包时，必须保留 Spring Boot Maven 插件，否则生成的 jar 无法通过 `java -jar` 启动。

```xml
<build>
    <plugins>
        <plugin>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-maven-plugin</artifactId>
        </plugin>
    </plugins>
</build>
```

### 6.2 application.yml 示例

```yml
server:
  port: 8098

spring:
  datasource:
    url: jdbc:mysql://127.0.0.1:3306/work_to_do?allowPublicKeyRetrieval=true&useSSL=false&characterEncoding=UTF-8&serverTimezone=GMT%2B8
    username: 填写自己的用户名
    password: 填写自己的密码
    driver-class-name: com.mysql.cj.jdbc.Driver

  thymeleaf:
    cache: false
    prefix: classpath:/views/

  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true
```

### 6.3 页面控制器示例

若页面文件位于：

```text
src/main/resources/views/index.html
```

则页面访问控制器可写为：

```java
@Controller
@RequestMapping("/work")
public class VIewController {

    @GetMapping
    public ModelAndView showAHtml() {
        return new ModelAndView("index");
    }
}
```

页面访问地址为：

```text
http://localhost:8098/work
```

### 6.4 打包命令

在 Spring Boot 项目根目录执行：

```bash
mvn clean package
```

打包成功后，可执行 jar 通常位于：

```text
target/study-workToDo-0.0.1-SNAPSHOT.jar
```

---

## 7. 数据库说明

系统使用本机 MySQL 作为数据存储。首次运行前，应确保数据库已创建。

示例建库语句如下：

```sql
CREATE DATABASE work_to_do CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
```

在启用 JPA 自动建表配置的情况下，项目首次启动后会自动创建或更新相关表结构。

---

## 8. 前端页面说明

系统前端采用 HTML + CSS + JavaScript 实现，主要负责以下功能：

* 展示待完成、长时间、已完成三类任务
* 提供任务新增、编辑、删除操作
* 提供备注查看功能
* 提供状态切换功能
* 提供菜单操作能力

为适配桌面悬浮使用场景，列表区域采用固定高度设计，并支持滚动浏览更多任务。当前页面样式以简洁、半透明、轻桌面化风格为主。

---

## 9. Electron 桌面外壳说明

Electron 负责将本地网页封装为桌面悬浮窗，并提供窗口级交互能力。

当前外壳具备以下能力：

* 无边框窗口
* 半透明显示
* 始终置顶
* 支持拖动
* 支持最小化
* 支持关闭

### 9.1 package.json 示例

```json
{
  "name": "worktodo-desktop",
  "version": "1.0.0",
  "description": "桌面待办外壳",
  "main": "main.js",
  "scripts": {
    "start": "electron ."
  },
  "devDependencies": {
    "electron": "^36.0.0"
  }
}
```

### 9.2 preload.js 示例

```javascript
const { contextBridge, ipcRenderer } = require('electron');

contextBridge.exposeInMainWorld('desktopShellAPI', {
  minimizeWindow: () => ipcRenderer.send('desktop:minimize'),
  closeWindow: () => ipcRenderer.send('desktop:close')
});
```

---

## 10. 启动与关闭方式

### 10.1 统一启动脚本

建议通过统一启动脚本完成以下操作：

* 检查并清理 `8098` 端口
* 启动 Spring Boot 服务
* 启动 Electron 悬浮窗
* 打开本地页面

### 10.2 统一关闭脚本

建议通过统一关闭脚本完成以下操作：

* 关闭 Electron 进程
* 清理 `8098` 端口
* 结束对应 Spring Boot 服务

该方式适合本机自用场景，可减少不同项目之间的端口冲突问题。

---

## 11. 本机部署与运行流程

### 第一步：确认环境

* MySQL 服务已启动
* 数据库 `work_to_do` 已创建
* JDK、Maven、Node.js、npm 已正确安装

### 第二步：打包后端

在 Spring Boot 源码目录执行：

```bash
mvn clean package
```

### 第三步：安装 Electron 依赖

在 `Desktop` 目录执行：

```bash
npm install
```

### 第四步：启动系统

双击统一启动脚本：

```text
start-worktodo-all.bat
```

### 第五步：关闭系统

双击统一关闭脚本：

```text
stop-worktodo-all.bat
```

---

## 12. 注意事项

1. 系统默认端口为 `8098`
2. 启动前应确保 MySQL 服务正常运行
3. 若页面无法显示，应优先检查：

   * Spring Boot 是否成功启动
   * `http://localhost:8098/work` 是否可访问
   * Electron 是否成功加载页面
4. 若配置文件中包含真实数据库账号密码，提交 GitHub 前应进行脱敏处理
5. `node_modules` 与打包产物体积较大，通常不建议提交至仓库

---

## 13. 后续扩展方向

项目后续可扩展以下能力：

* 系统托盘驻留
* 关闭后最小化到托盘
* 自定义透明度调节
* 悬浮窗位置锁定
* 开机自启动
* 任务分类颜色区分
* 提醒与通知功能
* 本地备份与导出能力

---

## 14. 项目定位

DesktopWorkToDo 适合作为以下方向的实践项目：

* 本地个人待办管理工具
* 桌面悬浮式任务展示工具
* Spring Boot 与 Electron 融合开发样例
* 单机桌面任务系统原型

---
