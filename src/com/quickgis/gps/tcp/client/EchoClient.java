 
package com.quickgis.gps.tcp.client;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;

 
 
public class EchoClient {

    private final String host;
    private final int port;
    private final int firstMessageSize;

    public EchoClient(String host, int port, int firstMessageSize) {
        this.host = host;
        this.port = port;
        this.firstMessageSize = firstMessageSize;
    }

    public void run() {
      
        ClientBootstrap bootstrap = new ClientBootstrap(
                new NioClientSocketChannelFactory(
                        Executors.newCachedThreadPool(),
                        Executors.newCachedThreadPool()));

        
        bootstrap.setPipelineFactory(new ChannelPipelineFactory() {
            public ChannelPipeline getPipeline() throws Exception {
                return Channels.pipeline(
                        new EchoClientHandler(firstMessageSize));
            }
        });

        
        ChannelFuture future = bootstrap.connect(new InetSocketAddress(host, port));

        
     //   future.getChannel().getCloseFuture().awaitUninterruptibly();

        
   //     bootstrap.releaseExternalResources();
       
    }

    public static void main(String[] args) throws Exception {
        /*for(int i=0;i<1000;i++){
           new EchoClient("localhost", 8088, i+1).run();
        }*/
    	/*for(int i=0; i<10; i++){
    		new EchoClient("localhost", 8088, 12).run();
    		System.out.println("send GPS info: " + i);
    		Thread.sleep(3000);
    	}*/
        //	vvmecs.3322.org:9191
    	new EchoClient("localhost", 8088, 12).run();
    	//new EchoClient("222.128.31.152", 9191, 12).run();
      
    }
}
