package handler;

import model.Response;
import com.alibaba.fastjson.JSONObject;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import medium.Medium;
import model.ServerRequest;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class ServerHandler extends ChannelInboundHandlerAdapter {

    private static final Executor exec = Executors.newFixedThreadPool(10);

    int counter = 0;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        //5.获取String类型的请求消息
        String body=(String)msg;
        System.out.println("This is "+ ++counter+"times receive client : ["+body+"]");


        exec.execute(new Runnable() {
            @Override
            public void run() {
                ServerRequest request = JSONObject.parseObject(body,ServerRequest.class);
//              System.out.println(Request.getCommand());
                Medium medium = Medium.newInstance();//生成中介者模式
                Response response = medium.process(request);
                //向客户端发送Resonse
                ctx.channel().writeAndFlush(JSONObject.toJSONString(response)+"$_");
            }
        });

        //6.String->JSON反序列化->Request(读取出Request)
      //  ServerRequest request = JSONObject.parseObject(body,ServerRequest.class);

        //7.1 构建Response
        //创建中介者（存储着bean和方法）,处理request（业务逻辑），返回response
       // Medium medium = Medium.newInstance();
       // Response response = medium.process(request);
//        response = new Response();
//        response.setId(request.getId());
//        response.setContent("is ok");

        //7.2 Response写回客户端
      //  ctx.channel().writeAndFlush(JSONObject.toJSONString(response) + "$_");  //a.Response->String$_



        //由于设置了DelimiterBasedFrameDecoder过滤掉了分隔符"$_"，   因此需要将返回消息尾部拼接上分隔符
        //body+="$_";
        //将接收到的消息再放到ByteBuf中重新发送给客户端
        //ByteBuf buf= Unpooled.copiedBuffer(body.getBytes());
        //把待发送的消息放到发送缓冲数组中，并把缓冲区中的消息全部写入SockChannel发送给客户端
        //ctx.writeAndFlush(buf);

    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if(evt instanceof IdleStateEvent) {
            IdleStateEvent event = (IdleStateEvent) evt;
            if(event.state().equals(IdleState.READER_IDLE)) {
                System.out.println("read idle,server close the channel=====");
                ctx.channel().close();
            } else if(event.state().equals(IdleState.WRITER_IDLE)) {
                System.out.println("write idle=========");
            } else if(event.state().equals(IdleState.ALL_IDLE)) {
                System.out.println("read and write idle,sent ping to client=====");
                String ping = "ping$_";
                ByteBuf buf= Unpooled.copiedBuffer(ping.getBytes());
                ctx.channel().writeAndFlush(buf);
            }
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();

    }
}
