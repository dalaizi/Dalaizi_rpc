package model;

public class ServerRequest {
    private long id;
    private Object content;  //方法参数
    private String command;  //命令（类 + 方法）


    public void setId(long id) {
        this.id = id;
    }
    public long getId() {
        return id;
    }

    public void setContent(Object content) {
        this.content = content;
    }
    public Object getContent() {
        return content;
    }

    public void setCommand(String command) {
        this.command = command;
    }
    public String getCommand() {
        return command;
    }
}
