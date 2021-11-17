/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gk.sms.common;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 *
 * @author TUANPLA
 */
public class ExecuteShellComand {

    public static void main(String[] args) {

        ExecuteShellComand obj = new ExecuteShellComand();
        //in mac oxs
        String command = "telnet 42.112.233.246 9998";
        //in windows
        //String command = "ping -n 3 " + domainName;
        String output = obj.executeCommand(command);

        System.out.println(output);

    }

    private String executeCommand(String command) {
        StringBuilder output = new StringBuilder();
        Process p;
        try {
            p = Runtime.getRuntime().exec(command);
            p.waitFor();
            BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));

            String line = "";
            while ((line = reader.readLine()) != null) {
                output.append(line + "\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return output.toString();
    }
}
