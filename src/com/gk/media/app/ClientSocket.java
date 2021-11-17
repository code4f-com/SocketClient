/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gk.media.app;

import com.gk.sms.common.DateProc;
import com.gk.sms.common.Md5;
import com.gk.sms.common.Tool;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

/**
 *
 * @author tuanpla
 */
public class ClientSocket extends Thread {

    //
    private static final String USER = "htcprivate";
    private static final String PASS = "smshtc8*7$%";
    //--
    private static DataOutputStream dataOut = null;
    private static BufferedReader dataIn = null;
    private static Socket client = null;
    public static boolean appStart = true;
    String sign;

    public static enum PREFIX {
        REG("REG"),
        ENQ("ENQ"), // Nhan ket qua tu CMC OK
        MESS("MESS"), // Da gui toi box chua biet the nao
        CLOSE("CLOSE"), // Box gui thanh cong toi User
        ERROR("ERROR"), // 
        ;
        public String val;

        private PREFIX(String val) {
            this.val = val;
        }
    }

    public ClientSocket() {
        try {
            client = new Socket();
            client.setKeepAlive(true);
            client.setReuseAddress(true);
            client.setTcpNoDelay(true);
            client.setSoTimeout(10 * 1000);
            //--
            SocketAddress sockaddr = new InetSocketAddress("127.0.0.1", 8888);
            client.connect(sockaddr, 10 * 1000);
            // Write Data To Sever
            dataOut = new DataOutputStream(client.getOutputStream());
            //read the server response message
            dataIn = new BufferedReader(new InputStreamReader(client.getInputStream()));
            sign = Md5.encryptMD5(USER + DateProc.createDDMMYYYY() + PASS);
            writeMess(PREFIX.REG + ":" + USER + ":" + sign, dataOut);
            new EnquireLinkSender().start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        System.out.println("IS Connected!");
        while (client != null && !client.isClosed() && appStart) {
            try {
                String response = readMess(dataIn);
                if (response.startsWith("check")) {
                    writeMess("online", dataOut);
                }
                if (response.startsWith("sms:")) {
                    writeMess("1", dataOut);
                }
            } catch (IOException e) {
                System.out.println("Socket Die a??:" + e.getMessage());
                try {
                    closeSK("run:" + e.getMessage());
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
        try {
            closeSK("Client Socket is ended");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void writeMess(String mess, DataOutputStream _out) throws IOException {
        _out.writeBytes(mess + System.lineSeparator());
    }

    private String readMess(BufferedReader read) throws IOException {
        String content = read.readLine();
        if (content == null) {
            // Throw io exception since possibly connection has been lost
            throw new IOException();
        }
        System.out.println("Server response:[" + content + "]");
        return content;
    }

    public void sendMessage(String mess) throws IOException {
        writeMess(mess, dataOut);
//        dataOut.writeBytes(mess + System.lineSeparator());
        System.out.println("Send Message:" + mess);
    }

    public static ClientSocket cl;

    private class EnquireLinkSender extends Thread {

        @Override
        public void run() {
            while (client != null && appStart && !client.isClosed()) {
                try {
                    writeMess(PREFIX.ENQ + ":" + sign, dataOut);
                    Thread.sleep(8 * 1000);
                } catch (IOException | InterruptedException e) {
                    closeSK("EnquireLinkSender stop by Error:" + e.getMessage());
                    Tool.debug(Tool.getLogMessage(e));
                }
            }
            Tool.debug("EnquireLinkSender stop");
        }
    }

    public void closeSK(String funCall) {
        try {
            if (client != null) {
                Tool.debug("Socket Session:" + this.hashCode() + " is Close [issue:" + funCall + "]");
                client.close();
                dataIn = null;
                dataOut = null;
                client = null;
                appStart = false;
            } else {
                Tool.debug("Socket not exist when you call Close it...");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            Tool.debug("===>>Cho nay thu xem co loi khong nao:" + ex.getMessage());
        }
    }

    public static void main(String args[]) throws IOException {
        cl = new ClientSocket();
        cl.start();
    }
}
