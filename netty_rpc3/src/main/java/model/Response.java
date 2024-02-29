package model;

import java.util.concurrent.atomic.AtomicLong;

public class Response {  //响应消息协议
    private long id;
    private Object content;
    private String code = "00000";//00000表示成功，其他表示失败
    private String msg;//失败信息

    public Response() {

    }

    public void setId(long id) {
        this.id = id;
    }

    public void setContent(Object content) {
        this.content = content;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public long getId() {
        return id;
    }

    public Object getContent() {
        return content;
    }
}
