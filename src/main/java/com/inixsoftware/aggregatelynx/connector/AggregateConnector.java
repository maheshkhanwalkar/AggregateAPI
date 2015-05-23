package com.inixsoftware.aggregatelynx.connector;

/*
    Copyright 2015 Mahesh Khanwalkar

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
*/

import com.inixsoftware.nioflex.nio.utils.NIOUtils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;

public class AggregateConnector
{
    private int port;
    private String ipAddr;

    private SocketChannel socket;

    private BufferedWriter bw;
    private BufferedReader br;

    /**
     * @param ipAddr - IP Address of AggregateServer
     * @param port - AggregateServer Port
     */
    public AggregateConnector(String ipAddr, int port)
    {
        this.port = port;
        this.ipAddr = ipAddr;
    }

    public void connect() throws IOException
    {
        socket = SocketChannel.open(new InetSocketAddress(ipAddr, port));
        NIOUtils.writeString("0.1.0", socket);

        int len = NIOUtils.readInt(socket);
        String resp = NIOUtils.readString(len, socket);

        //the server either responds ACK or ERR_UNSUPPORTED
        if(!resp.equals("ACK"))
        {
            socket.close();
            throw new IOException("AggregateServer doesn't support this API version");
        }
    }

    public void shutdown() throws IOException
    {
        NIOUtils.writeInt(12, socket);
        NIOUtils.writeString("CLOSE_SOCKET", socket);

        int len = NIOUtils.readInt(socket);
        String resp = NIOUtils.readString(len, socket);

        if(resp.equals("ACK"))
        {
            socket.close();
            return;
        }

        throw new IOException("Improper Disconnection from AggregateServer"); //not good :(
    }

    public void createAggregate(String name,
          String fields, String aggregates, String values)
    {
        NIOUtils.writeInt(16, socket);
        NIOUtils.writeString("CREATE_AGGREGATE", socket);

        NIOUtils.writeInt(name.length(), socket);
        NIOUtils.writeString(name, socket);

        NIOUtils.writeInt(fields.length(), socket);
        NIOUtils.writeString(fields, socket);

        NIOUtils.writeInt(aggregates.length(), socket);
        NIOUtils.writeString(aggregates, socket);

        NIOUtils.writeInt(values.length(), socket);
        NIOUtils.writeString(values, socket);
    }

    public void sendData(String name, String data)
    {
        NIOUtils.writeInt(12, socket);
        NIOUtils.writeString("DO_AGGREGATE", socket);

        NIOUtils.writeInt(name.length(), socket);
        NIOUtils.writeString(name, socket);

        NIOUtils.writeInt(data.length(), socket);
        NIOUtils.writeString(data, socket);
        //TODO
    }

    public int getResult(String name)
    {
        NIOUtils.writeInt(10, socket);
        NIOUtils.writeString("GET_RESULT", socket);

        NIOUtils.writeInt(name.length(), socket);
        NIOUtils.writeString(name, socket);

        return NIOUtils.readInt(socket);
    }
}
