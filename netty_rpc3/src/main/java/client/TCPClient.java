package client;

import com.alibaba.fastjson.JSONObject;
import handler.SimpleClientHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.timeout.IdleStateHandler;
import model.ClientRequest;
import model.Response;

import java.util.concurrent.TimeUnit;

import static java.lang.Thread.sleep;

public class TCPClient {
    static final Bootstrap b = new Bootstrap(); // (1)
    static ChannelFuture f = null;
            //b.connect(host, port).sync(); // (5)
    static {
                EventLoopGroup workerGroup = new NioEventLoopGroup();
                b.group(workerGroup); // (2)
                b.channel(NioSocketChannel.class); // (3)
                b.option(ChannelOption.SO_KEEPALIVE, true); // (4)
                b.handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    public void initChannel(SocketChannel ch) throws Exception {
                        ByteBuf delimiter = Unpooled.copiedBuffer("$_".getBytes());
                        ch.pipeline().addLast(new IdleStateHandler(60, 45, 20, TimeUnit.SECONDS));
                        ch.pipeline().addLast(new DelimiterBasedFrameDecoder(Integer.MAX_VALUE, delimiter));  //c.二进制&_->二进制
                        ch.pipeline().addLast(new StringDecoder());  //d.二进制->String
                        ch.pipeline().addLast(new SimpleClientHandler()); //e.处理String
                        ch.pipeline().addLast(new StringEncoder());//2. 字符串编码器 String->二进制
                    }
                });
                String host = "localhost";
                int port = 8080 ;

                try {
                    f = b.connect(host, port).sync();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    workerGroup.shutdownGracefully();
                }
            }
    public static Response send (ClientRequest request) {
        f.channel().writeAndFlush(JSONObject.toJSONString(request) + "$_");  //1. request->JSON序列化->String
        //根据 Request 去 defaultFuture 取 Response
        Long timeOut = 60l;
        DefaultFuture defaultFuture = new DefaultFuture(request);
        return defaultFuture.get(timeOut);
    }


}

