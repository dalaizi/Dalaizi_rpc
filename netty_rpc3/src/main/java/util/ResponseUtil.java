package util;

import model.Response;

//Response的工具类，生产 （含文本内容的）成功回复 和 含失败码的失败回复

public class ResponseUtil {
    public static Response createSuccessResponse(){
        return new Response();
    }

    public static Response createSuccessResponse(Object content){
        Response response = new Response();
        response.setContent(content);

        return response;
    }

    public static Response createFailResponse(String code,String msg){
        Response response = new Response();
        response.setCode(code);
        response.setMsg(msg);

        return response;
    }
}
