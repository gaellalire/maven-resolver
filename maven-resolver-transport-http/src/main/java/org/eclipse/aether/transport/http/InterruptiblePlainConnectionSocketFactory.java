package org.eclipse.aether.transport.http;

import java.io.IOException;
import java.net.Socket;
import java.nio.channels.SocketChannel;

import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.protocol.HttpContext;

public class InterruptiblePlainConnectionSocketFactory extends PlainConnectionSocketFactory 
{
    public static final InterruptiblePlainConnectionSocketFactory INSTANCE = new InterruptiblePlainConnectionSocketFactory();

    public static InterruptiblePlainConnectionSocketFactory getSocketFactory() {
        return INSTANCE;
    }

    @Override
    public Socket createSocket( HttpContext context )
        throws IOException
    {
        return SocketChannel.open().socket();
    }
    
}
