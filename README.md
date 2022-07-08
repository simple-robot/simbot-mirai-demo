# simbot Demo - Mirai

这是[simple-robot](https://github.com/ForteScarlet/simpler-robot) 框架使用[mirai组件](https://github.com/ForteScarlet/simpler-robot/tree/dev/component/component-mirai) 对接[Mirai](https://github.com/mamoe/mirai) 的Demo项目。

## 需要做的
### 参考文档
如果存在问题，仍然需要参考 [文档](https://www.yuque.com/simpler-robot/simpler-robot-doc) 或者反馈问题。

### fork/clone
fork或者clone此项目到你的本地，并使用IDE工具打开并构建它。

### 修改配置文件

打开目录 [src/main/resources/simbot-bots](src/main/resources/simbot-bots)，
根据目录下的文件调整、删改，修改为你的bot的配置信息。

**xxx.bot**

```properties
code=BOT账号
password=BOT密码
```

### 阅读
- [listener](src/main/java/love/simbot/example/listener) 包下为一些监听函数示例。阅读它们的注释，并可以试着修改它们。

### 运行
执行[SimbotExampleApplication](src/main/java/love/simbot/example/SimbotExampleApplication.java) 中的main方法。

### 验证
如果你是第一次使用此框架，且出现了诸如需要“滑动验证”等相关错误，你可以尝试先使用一次 [simbot-mirai-login-solver-selenium-helperPack](https://github.com/simple-robot/simbot-mirai-login-solver-selenium-helperPack) 来使腾讯记住你的设备信息。

### 协助
如果你有一个好的示例点子，你可以通过[github pr](https://github.com/simple-robot/simbot-mirai-demo/pulls) 来协助此demo项目的更新。

