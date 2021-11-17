/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 *
 * @author TUANPLA
 */
public class MySeverSoket extends Thread {

    private static final int severPort = 8888;
    private static boolean appStart = true;
    private ServerSocket serverSocket;
    public static final ExecutorService waitBindExecService = Executors.newFixedThreadPool(10);

    public MySeverSoket(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        serverSocket.setSoTimeout(10000);
    }

    @Override
    public void run() {
        while (appStart) {
            try {
//                System.out.println("Sever available on port " + serverSocket.getLocalPort() + "...");
                Socket clientSocket = serverSocket.accept();
                System.out.println("Just connected to " + clientSocket.getRemoteSocketAddress());
                waitBindExecService.execute(new ClientConn(clientSocket));
            } catch (SocketTimeoutException s) {
                // System.out.println("Socket timed out!");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    static Map<String, ClientConn> CLIENT_REIGS = new HashMap<>();

    private class ClientConn implements Runnable {

        private final Socket clientSocket;
        BufferedReader read;
        DataOutputStream out;
        String clientName;
        
        public ClientConn(Socket clientSocket) {
            this.clientSocket = clientSocket;
            try {
                read = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                out = new DataOutputStream(clientSocket.getOutputStream());
                out.writeBytes("Accepted!" + System.lineSeparator());
            } catch (Exception e) {
                // TODO: handle exception
            }
        }

        @Override
        public void run() {
            while (!clientSocket.isClosed()) {
                try {
                    String clMessage = readMess(read);
                    processMessage(clMessage);
                } catch (SocketTimeoutException s) {
                    // System.out.println("Socket timed out!");
                } catch (IOException e) {
                    try {
                        if (clientName != null) {
                            removeClient(clientName);
                        }
                        System.out.println("Socket is Close :" + clientSocket.hashCode());
                        clientSocket.close();
                    } catch (Exception ex) {
                    }
                }
            }
        }

        public void processMessage(String message) {
            try {
                if (message != null && !message.equals("")) {
                    String[] arr = message.split(":");
                    if (arr != null && arr.length > 1) {
                        if (arr[0].equalsIgnoreCase("REG")) {
                            clientName = arr[1];
                            regis(arr[1], this);
                            writeMess("Registed", out);
                        } else if (arr.length == 3) {
                            // Send message to User
                            ClientConn client = CLIENT_REIGS.get(arr[1]);
                            if (client != null) {
                                sendMessage(arr[0] + ":" + arr[2]);
                            } else {
                                System.out.println("Not find client Socket in Regismap:" + arr[1]);
                            }
                        } else {
                            System.out.println("Message not valid:" + message);
                        }
                    } else {
                        System.out.println("Message not valid:" + message);
                    }
                } else {
                    System.out.println("Mesage is null or Empty");
                }
            } catch (Exception e) {
                System.out.println("Process message error:" + e.getMessage());
                // TODO: handle exception
            }

        }

        public void regis(String username, ClientConn client) {
            System.out.println("Register Client:" + clientName);
            CLIENT_REIGS.put(username, client);
        }

        public void removeClient(String username) {
            System.out.println("Remove Client From Map:" + clientName);
            CLIENT_REIGS.remove(username);
        }

        public int sendMessage(String message) {
            int result = 0;
            try {
                //write to toClient
                System.out.println("Send Message to:" + clientSocket.hashCode());
                writeMess(message, out);
                // 
//                Tool.debug("Sending request to Socket Server");
//                    String response = (String) _read.readLine();
//                    if (response != null && response.startsWith("ASK:")) {
//                        // Phan hoi cho thang Gui
//                        writeMess(response, out);
//                    }
//                    System.out.println("Response from client:" + response);

            } catch (Exception e) {
                e.printStackTrace();
            }
            return result;
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
            System.out.println("Client data Send:[" + content + "]");
            return content;
        }
    }

    public static void main(String[] args) {
        try {
            Thread sv = new MySeverSoket(severPort);
            sv.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
