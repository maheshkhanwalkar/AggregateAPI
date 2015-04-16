package com.inixsoftware.haggregate.connector;

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

import java.io.*;
import java.net.Socket;

public class AggregateConnector
{
    private int port;
    private String ipAddr;

    private Socket socket;

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
        socket = new Socket(ipAddr, port);
        bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

        br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        bw.write("0.0.1\n");
        bw.flush();

        String resp = br.readLine();
        if(!resp.equals("ACK"))
        {
            br.close();
            bw.close();

            socket.close();
            throw new IOException("AggregateServer doesn't support this API version");
        }
    }

    public void shutdown() throws IOException
    {
        bw.write("CMD:CLOSE_SOCKET\n");
        bw.flush();

        String resp = br.readLine();
        if(resp.equals("ACK"))
        {
            bw.close();
            br.close();

            socket.close();
            return;
        }

        throw new IOException("Improper Disconnection from AggregateServer");
    }
}
