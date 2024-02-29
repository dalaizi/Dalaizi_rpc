package consumer.handler;


import com.alibaba.fastjson.JSONObject;
import consumer.core.DefaultFuture;
import consumer.model.Response;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;


public class SimpleClientHandler extends ChannelInboundHandlerAdapter {

    int counter;

    //发送到服务端的消息，注意结尾的分隔符一定要和服务端配置的分隔符一致，否则服务端ChannelInitializer.initChannel()方法虽然能够调用，但是DelimiterBasedFrameDecoder无法找到分隔符，不会调用读取消息的channelRead方法
//    static final String ECHO_REQ="Hi,I am NettyClient.$_";
//
//
//    @Override
//    public void channelActive(ChannelHandlerContext ctx) throws Exception {
//        System.out.println("client is ready========");
//
//        for (int i = 0; i < 5; i++ )
//        {
//            //Unpooled.copiedBuffer()方法是深克隆，也可以使用Unpooled.buffer()写入消息发送
//            //ctx.writeAndFlush(Unpooled.copiedBuffer(ECHO_REQ.getBytes()));
//        }
//
//    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        //e.接收String
        String body=(String)msg;
        if(body.equals("ping")) {   //服务器问客户端还活着吗
            String pong = "pong$_";
            ByteBuf buf= Unpooled.copiedBuffer(pong.getBytes());
            ctx.channel().writeAndFlush(buf);
            return;
        }
        System.out.println("This is "+ ++counter+" times receive server:["+body+"]");
        //f.String->JSON反序列化->Response
        Response response = JSONObject.parseObject(body, Response.class);
        //g.
        DefaultFuture.receive(response);//通过response的ID可以在map中找到对应的request,并为相应的request设置response,使得调用get()客户端得到结果

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
