package model;

import java.util.concurrent.atomic.AtomicLong;

public class ClientRequest {  //请求消息协议
    private final long id;
    private Object content;  //要执行的方法的参数
    private String command;  //要执行的命令（类 + 方法）
    private final AtomicLong aid = new AtomicLong(1);

    public ClientRequest() {
        id = aid.getAndIncrement();
    }

    public long getId() {
        return id;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public Object getContent() {
        return content;
    }

    public void setContent(Object content) {
        this.content = content;
    }


}
