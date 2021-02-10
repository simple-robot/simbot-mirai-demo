# simbot Demo - Mirai

这是[simple-robot](https://github.com/ForteScarlet/simpler-robot) 框架使用[mirai组件](https://github.com/ForteScarlet/simpler-robot/tree/dev/component/component-mirai) 对接[Mirai](https://github.com/mamoe/mirai) 的Demo项目。

## 需要做的
### fork/clone
fork或者clone此项目到你的本地，并使用IDE工具打开并构建它。

### 修改配置文件
打开文件 [simbot.yml](src/main/resources/simbot.yml) 并修改其中的 `simbot.core.bots` 项为你测试用的QQ账号，例如：
```yaml
simbot: 
  core:
    bots: 123456789:password
```

### 保证安静
将你的bot放在一些测试用的群而不是一些大型群。

### 阅读
- [listener](src/main/java/love/simbot/example/listener) 包下为一些监听函数示例。阅读它们的注释，并可以试着修改它们。

### 运行
执行[SimbotExampleApplication](src/main/java/love/simbot/example/SimbotExampleApplication.java) 中的main方法。

### 协助
如果你有一个好的示例点子，你可以通过[github pr](https://github.com/simple-robot/simbot-mirai-demo/pulls) 来协助此demo项目的更新。

