package de.htwsaar.server.config;

import java.io.*;
import java.net.*;
import java.io.*;

public class LocalIpAddress {
    public static String getExternalIpAddress() throws IOException {
        URL whatismyip = new URL("http://checkip.amazonaws.com");
        BufferedReader in = new BufferedReader(new InputStreamReader(
                whatismyip.openStream()));

        String ip = in.readLine(); //you get the IP as a String
        return ip;
    }
}