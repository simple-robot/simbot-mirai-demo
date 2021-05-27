package love.simbot.example.listener;

import love.forte.common.ioc.annotation.Beans;
import love.forte.common.ioc.annotation.Depend;
import love.forte.simbot.annotation.OnGroupAddRequest;
import love.forte.simbot.annotation.OnGroupMemberIncrease;
import love.forte.simbot.api.message.MessageContent;
import love.forte.simbot.api.message.MessageContentBuilder;
import love.forte.simbot.api.message.MessageContentBuilderFactory;
import love.forte.simbot.api.message.Reply;
import love.forte.simbot.api.message.assists.Flag;
import love.forte.simbot.api.message.containers.AccountInfo;
import love.forte.simbot.api.message.containers.BotInfo;
import love.forte.simbot.api.message.containers.GroupInfo;
import love.forte.simbot.api.message.events.GroupAddRequest;
import love.forte.simbot.api.message.events.GroupMemberIncrease;
import love.forte.simbot.api.sender.Sender;
import love.forte.simbot.api.sender.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 这是一个 自动通过加群申请并自动迎新 的实例监听器。
 *
 * @author ForteScarlet
 */
@Beans
public class MyNewGroupMemberListen {

    /**
     * 注入得到一个消息构建器工厂。
     */
    @Depend
    private MessageContentBuilderFactory messageBuilderFactory;

    /**
     * 用来缓存入群申请的时候所填的信息。
     */
    private static final Map<String, String> REQUEST_TEXT_MAP = new ConcurrentHashMap<>();

    /**
     * logger
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(MyNewGroupMemberListen.class);

    /**
     * {@link OnGroupAddRequest} 是一个模板注解，其等同于 {@code @Listen(GroupAddRequest.class)},
     * 即监听一个 {@link GroupAddRequest} 类型的事件。
     * <p>
     * {@link GroupAddRequest} 顾名思义，即 “群添加申请” 事件。
     * <p>
     * 这个事件不仅仅代表别人加入某群，也有可能代表有人邀请当前bot入群。
     * <p>
     * 当然了，如果是处理其他人的加群申请，那么这个bot必须是个管理员才能接收到请求事件。
     *
     * @param groupAddRequest 群添加申请/邀请事件。
     * @param setter          一般用来通过申请，使用的是Setter。当然，你也可以使用 {@link love.forte.simbot.api.sender.MsgSender#SETTER}, 它们所代表的是同一个对象。
     * @see GroupAddRequest
     */
    @OnGroupAddRequest
    public void onRequest(GroupAddRequest groupAddRequest, Setter setter) {
        // 此事件的“申请者”
        AccountInfo accountInfo = groupAddRequest.getRequestAccountInfo();
        // 收到此事件的bot
        BotInfo botInfo = groupAddRequest.getBotInfo();

        // 如果上述两者的账号不相同，则说明此事件不是bot被邀请，而是别人申请入群。
        // 这步判断操作似乎很繁琐，未来版本可能会提供更简洁的方案
        // 如果你有好的点子，可以通过 github issue 或 github pr向simbot提出。 https://github.com/ForteScarlet/simpler-robot
        if (!accountInfo.getAccountCode().equals(botInfo.getBotCode())) {
            // 获取入群的时候的申请消息（如果有的话
            String text = groupAddRequest.getText();
            if (text != null) {
                // 如果有，记录这一条信息。
                REQUEST_TEXT_MAP.put(accountInfo.getAccountCode(), text);
            }
            GroupInfo groupInfo = groupAddRequest.getGroupInfo();

            LOGGER.info("{}({}) 申请加入群 {}({}), 申请备注：{}",
                    accountInfo.getAccountNickname(), accountInfo.getAccountCode(),
                    groupInfo.getGroupName(), groupInfo.getGroupCode(),
                    text
            );


            // 通过申请
            // 通过setter来通过加群申请有多个方法：
            // 方法1：acceptGroupAddRequest(flag)
            // flag 是请求事件的一个”标识“
            setter.acceptGroupAddRequest(groupAddRequest.getFlag());

            // 方法2：setGroupAddRequest(flag, agree, blockList, why)
            // 4个参数分别代表：标识、是否同意、是否加入黑名单(一般是只有在拒绝时生效, 但是mirai目前不支持此参数)、以及这么操作的原因(一般是在拒绝时生效, 可以为null)
            // setter.setGroupAddRequest(groupAddRequest.getFlag(), true, false, null);

            // 方法3：return Reply.accept()
            // 将方法返回值设置为 ReplyAble 或者 Reply, 然后直接返回 Reply.accept() 实例来快速同意申请。
            // 这种方法依靠的是响应值处理器。文档参考：https://www.yuque.com/simpler-robot/simpler-robot-doc/aioxhh

            // 方法4：鉴于目前 Flag 的设计仍然有一些风险和隐患，未来可能会提供其他更便捷的方式对相关事件进行处理。

        }

    }


    /**
     * 新人入群申请之后，便是 ”群成员增加“ 事件，如果你想要什么迎新操作，建议都在这个事件中处理。
     * <p>
     * 通过 {@link OnGroupMemberIncrease} 监听群人数增加事件，这也是一个模板注解，其等效于 {@code @Listen(GroupMemberIncrease.class)}
     *
     * @param groupMemberIncrease 群人数增加事件实例
     * @param sender              既然是”迎新“示例，则当然要发消息。
     * @see GroupMemberIncrease
     */
    @OnGroupMemberIncrease
    public void newGroupMember(GroupMemberIncrease groupMemberIncrease, Sender sender) {
        // 得到一个消息构建器。
        MessageContentBuilder builder = messageBuilderFactory.getMessageContentBuilder();

        // 入群者信息
        AccountInfo accountInfo = groupMemberIncrease.getAccountInfo();

        // 尝试从缓存中获取他入群的时候所记录的信息
        // 如果不希望看到null，则记得自行处理。
        String text = REQUEST_TEXT_MAP.remove(accountInfo.getAccountCode());

        // 假设我们的迎新消息是这样的：
        /*
            @xxx 欢迎入群！
            你的入群申请信息是：xxxxxx
         */
        MessageContent msg = builder
                // at当事人
                .at(accountInfo)
                // tips 通过 \n 换行
                .text(" 欢迎入群！\n")
                .text("你的入群申请信息是：").text(text)
                .build();

        // 增加了人的群信息
        GroupInfo groupInfo = groupMemberIncrease.getGroupInfo();

        // 发送消息
        sender.sendGroupMsg(groupInfo, msg);
    }


}
