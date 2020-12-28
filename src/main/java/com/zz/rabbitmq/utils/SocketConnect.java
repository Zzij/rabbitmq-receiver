package com.zz.rabbitmq.utils;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 *@author: zzj
 *@date: 2020/12/28
 *@description 模拟异常
**/
public class SocketConnect {

    private int port;

    private String address;

    public SocketConnect(int port, String address) {
        this.port = port;
        this.address = address;
    }

    public void connect() throws IOException {
        Socket socket = new Socket();
        socket.connect(new InetSocketAddress(address, port));
    }
}
