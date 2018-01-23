package de.htwsaar.server.config;

import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

public class LocalIpAddress {
    /**
     * Diese Funktion dient zur Feststellung der eigenen, externen IP.
     *
     * @return ip
     * @throws IOException Exception für evtl. Fehler des Readers
     */
    public static String getExternalIpAddress() throws IOException {
//        URL whatismyip = new URL("http://checkip.amazonaws.com");
//        BufferedReader in = new BufferedReader(new InputStreamReader(
//                whatismyip.openStream()));
//
//        String ip = in.readLine(); //you get the IP as a String
//        return ip;
        Enumeration<NetworkInterface> n = null;
        try {
            n = NetworkInterface.getNetworkInterfaces();
        } catch (SocketException e) {
            e.printStackTrace();
        }
        for (; n != null && n.hasMoreElements(); ) {
            NetworkInterface e = n.nextElement();

            Enumeration<InetAddress> a = e.getInetAddresses();
            for (; a.hasMoreElements(); ) {
                InetAddress addr = a.nextElement();
                if (addr.getHostAddress().length() <= 16) {
                    if (!addr.getHostAddress().contains("127") && (!addr.getHostAddress().contains(":")))
                        return addr.getHostAddress();
                }
            }
        }
        return "IP_not_solved";

    }


}