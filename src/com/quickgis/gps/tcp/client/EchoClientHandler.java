 
package com.quickgis.gps.tcp.client;

import java.util.concurrent.atomic.AtomicLong;
 

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;

 
public class EchoClientHandler extends SimpleChannelUpstreamHandler {

   
    private final ChannelBuffer firstMessage;
    private final AtomicLong transferredBytes = new AtomicLong();

   
    public EchoClientHandler(int firstMessageSize) {
        if (firstMessageSize <= 0) {
            throw new IllegalArgumentException(
                    "firstMessageSize: " + firstMessageSize);
        }
        //firstMessage = ChannelBuffers.buffer(firstMessageSize); 
        //firstMessage.writeByte((byte) firstMessageSize);
        
        //*HQ,2130527699,V1,004835,V,3954.3197,N,11615.8178,E,000.00,000,060180,FFE7FBFF#
        //*HQ,2131125318,V1,045057,A,3358.1046,N,11753.2763,W,000.00,273,240214,EFE7FBFF#
        //*HQ,2130527481,V1,045057,A,3358.1046,N,11753.2763,W,000.00,273,240214,EFE7FBFF#
        //*G,15986701034,114.2,22.0,80,280,130313100000,1,100,111,222,#
        String res = "*HQ,2130527481,V1,125057,A,3058.1046,N,11053.2763,W,000.00,273,240214,EFE7FBFF#";    
        byte[] data = res.getBytes();    
        int dataLength = data.length;    
        firstMessage = ChannelBuffers.dynamicBuffer();
       // firstMessage.writeInt(dataLength);    
        firstMessage.writeBytes(data);
       
    }

    public long getTransferredBytes() {
        return transferredBytes.get();
    }

    @Override
    public void channelConnected(
            ChannelHandlerContext ctx, ChannelStateEvent e) { 
    	//   System.out.println("client hander  connected");
    	e.getChannel().write(firstMessage);
    }

    @Override
    public void messageReceived(
            ChannelHandlerContext ctx, MessageEvent e) {
    	  
        transferredBytes.addAndGet(((ChannelBuffer) e.getMessage()).readableBytes());
        System.out.println("client read:"+((ChannelBuffer) e.getMessage()).readByte());
      //  e.getChannel().write(e.getMessage());
    }

    @Override
    public void exceptionCaught(
            ChannelHandlerContext ctx, ExceptionEvent e) { 
         System.out.println(" client hander exception close");
    }
}
