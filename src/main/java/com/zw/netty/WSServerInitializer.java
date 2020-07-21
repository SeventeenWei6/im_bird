package com.zw.netty;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.handler.timeout.IdleStateHandler;

public class WSServerInitializer  extends ChannelInitializer<NioSocketChannel> {
    @Override
    protected void initChannel(NioSocketChannel channel) throws Exception {
        ChannelPipeline pipeline = channel.pipeline();
        //webSocket基于http协议，所以需要http编解码器
        pipeline.addLast(new HttpServerCodec())
                //对写大数据流的支持
                .addLast(new ChunkedWriteHandler())
                //聚合器，将零散的HttpMessage聚合成为FullHttpRequest和FullHttpResponse,几乎Netty的编程都需要用到，设置消息的最大的长度
                .addLast(new HttpObjectAggregator(1024*64));

        //=================================================以上是用户支持http协议===========================================================

        //===========================增加心跳支持==============================

        /**
         * 针对客户端，如果在1分钟时间内没有向服务端发送读写心跳（ALL），则主动断开连接
         * 如果有读空闲和写空闲，则不做任何处理
         */
        pipeline.addLast(new IdleStateHandler(8,10,12));
        //自定义的空闲状态检测的handler
        pipeline.addLast(new HeartBeatHandler());
        /**
         * 针对WebSocket处理的协议，用于指定给客户端连接访问的路由
         * 本handler会帮忙处理一些繁琐复杂的工作
         * 处理握手动作：handshaking (close-关闭,ping-请求,pong-响应) ping+pong=心跳
         *对于webSocket来说，传输都是以frames进行传输，不同的数据类型对于的frames不同
         */
        pipeline.addLast(new WebSocketServerProtocolHandler("/ws"));
        pipeline.addLast(new ChatHandler());//自定义的handler




    }
}
