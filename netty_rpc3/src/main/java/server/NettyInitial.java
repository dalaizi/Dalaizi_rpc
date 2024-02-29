package server;

import handler.ServerHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.timeout.IdleStateHandler;
import org.apache.curator.framework.CuratorFramework;
import org.apache.zookeeper.CreateMode;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import zookeeper.constants.Constants;
import zookeeper.factory.ZooKeeperFactory;

import java.net.InetAddress;
import java.util.concurrent.TimeUnit;

@Component
public class NettyInitial implements ApplicationListener<ContextRefreshedEvent> {


    public void start() {
        EventLoopGroup bossGroup = new NioEventLoopGroup(); // (1)
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap(); // (2)
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class) // (3)
                    .childHandler(new ChannelInitializer<SocketChannel>() { // (4)
                        @Override
                        public void initChannel(SocketChannel ch) throws Exception {
                            ByteBuf delimiter= Unpooled.copiedBuffer("$_".getBytes());
                            ch.pipeline().addLast(new IdleStateHandler(60,45,20, TimeUnit.SECONDS));
                            ch.pipeline().addLast(new DelimiterBasedFrameDecoder(65535, delimiter)); //3.二进制$_ ->二进制
                            ch.pipeline().addLast(new StringDecoder());  //4.二进制->String
                            ch.pipeline().addLast(new ServerHandler());  //5.String->处理
                            ch.pipeline().addLast(new StringEncoder());  //b.String$_->二进制$_
                        }
                    })
                    .option(ChannelOption.SO_BACKLOG, 128)          // (5)
                    .childOption(ChannelOption.SO_KEEPALIVE, true); // (6)

            // Bind and start to accept incoming connections.
            int port = 8080;
            ChannelFuture f = b.bind(port).sync(); // (7)
            //服务端通过curator注册在zookeeper中
            InetAddress address = InetAddress.getLocalHost();
            CuratorFramework client = ZooKeeperFactory.getClient();
            if(client != null){
                System.out.println(client);
                client.create().withMode(CreateMode.EPHEMERAL_SEQUENTIAL).forPath(Constants.SERVER_PATH+"/"+address.getHostAddress()+"#"+port+"#");
                System.out.println("成功");
            }
            System.out.println("server is ready=========");

            f.channel().closeFuture().sync();
            System.out.println("server is close=========");
        } catch(Exception e) {
            e.printStackTrace();
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }


    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        System.out.println("---------------------");
        System.out.println("ContextRefreshedEvent 触发,server开始初始化=====");
        this.start();
    }
}
