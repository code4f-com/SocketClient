/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package demo.timeout;

import java.io.*;
import java.net.*;

/**
 * EchoServer offers an echo service to multiple clients. The echo service is
 * limited to a set number of connections, to prevent server over-load. It also
 * includes timeout handling code to prevent server threads from blocking if a
 * client is stalled.
 */
public class EchoServer extends Thread {

    /**
     * Connection to client
     */
    private Socket m_connection;

    /**
     * Number of connections
     */
    private static int number_of_connections = 0;

    /**
     * Maximum number of connections
     */
    private static final int max_connections = 2;

    /**
     * Port to bind to
     */
    private static final int service_port = 2000;

    /**
     * Timeout length
     */
    private static final int timeout_length = 30000;

    /**
     * Creates a new instance of EchoServer thread, to service the specified
     * client connection.
     *
     * @param connection	Connection to service
     */
    public EchoServer(Socket connection) {
        // Assign to member variable
        m_connection = connection;

        // Set a timeout of 'timeout_length' milliseconds
        try {
            m_connection.setSoTimeout(timeout_length);
        } catch (SocketException se) {
            System.err.println("Unable to set socket option SO_TIMEOUT");
        }

        // Increment number of connections
        number_of_connections++;
    }

    public void run() {
        try {
            // Get I/O streams
            InputStream in = m_connection.getInputStream();
            OutputStream out = m_connection.getOutputStream();

            try {
                for (;;) // Echo data straight back to client
                {
                    out.write(in.read());
                }
            } catch (InterruptedIOException iioe) {
                System.out.println("Timeout occurred - killing connection");
                m_connection.close();
            }
        } catch (IOException ioe) {
            // No code required - thread will terminate at end
            // of the run method
        }

        // Decrement the number of connections
        number_of_connections--;
    }

    public static void main(String args[]) throws Exception {
        // Bind to a local port
        ServerSocket server = new ServerSocket(service_port);

        for (;;) {
            // Accept the next connection
            Socket connection = server.accept();

            // Check to see if maximum reached
            if (number_of_connections > max_connections - 1) {
                // Kill the connection
                PrintStream pout = new PrintStream(connection.getOutputStream());
                pout.println("Too many users");
                connection.close();
                continue;
            } else {
                // Launch a new thread
                new EchoServer(connection).start();
            }
        }
    }
}
