package medium;

import model.Response;
import com.alibaba.fastjson.JSONObject;
import model.ServerRequest;

import java.lang.reflect.Method;
import java.util.HashMap;

public class Medium {  //中介者，单例
    public static final HashMap<String, BeanMethod> mediaMap = new HashMap<String, BeanMethod>();
    private static Medium medium = null;
    private Medium() {}

    public static Medium newInstance(){
        if(medium == null){
            medium = new Medium();
        }

        return medium;
    }

    public Response process(ServerRequest request){ //服务器让中介者处理请求,返回响应(反射处理业务代码)
        Response result = null;
        try {
            String command = request.getCommand();//command是key，可以理解成request要求执行的操作
            BeanMethod beanMethod = mediaMap.get(command);
            //System.out.println(command);
            if(beanMethod == null){
                return null;
            }
            //根据Request的command，得到需要操作的 类 方法 参数
            Object bean = beanMethod.getBean();
            Method method = beanMethod.getMethod();
            Class type = method.getParameterTypes()[0];//先只实现1个参数的方法
            Object content = request.getContent();
            Object args = JSONObject.parseObject(JSONObject.toJSONString(content), type);//content->String->args

            result = (Response) method.invoke(bean, args);//执行bean.method(args),返回值是Response 反射处理业务代码
            result.setId(request.getId());
          //  result.setContent("is ok");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;

    }

}
