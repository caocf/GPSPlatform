package com.quickgis.gps.tcp;

 
 
import java.net.InetSocketAddress; 
import java.util.concurrent.Executors; 

import org.apache.log4j.Logger;
import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.DefaultChannelPipeline;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;

  
  
public class NettyServer  {  
	static Logger logger=Logger.getLogger(NettyServer.class);
	private ServerBootstrap bootstrap = null;  
	private  int listenPort=8088;  
	private static NettyServer nettyServer;
	private NettyServer(int port) {
		this.listenPort=port;
		bootstrap = new ServerBootstrap(new NioServerSocketChannelFactory(
				Executors.newCachedThreadPool(),
				Executors.newCachedThreadPool()));
		bootstrap.setOption("tcpNoDelay",  true);
		bootstrap.setOption("reuseAddress",true);
		 
		bootstrap.setPipelineFactory(new ChannelPipelineFactory() {
			public ChannelPipeline getPipeline() throws Exception {
				ChannelPipeline pipeline = new DefaultChannelPipeline(); 
				pipeline.addLast("handler", new NettyServerHandler());
				return pipeline;
			}
		});
		bootstrap.bind(new InetSocketAddress(listenPort));
		logger.info("server listen on port:"+port);
	}
	
	public static NettyServer getInstance(int port) {
		synchronized(NettyServer.class){
			nettyServer=new NettyServer(port); 
			return nettyServer;
		}
	} 
	
	public void stop() throws Exception { 
		bootstrap.releaseExternalResources();
		 
	}
	
	public static void main(String[] args){ 
		NettyServer nettyServer=NettyServer.getInstance(8088);  
	}

}
