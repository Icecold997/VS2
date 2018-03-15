package de.htwsaar.server.config;

import de.htwsaar.ConnectionConfig;

import java.util.Comparator;

public class ConnectionConfigComperator implements Comparator<ConnectionConfig> {
    @Override
    public int compare(ConnectionConfig connectionConfig1,ConnectionConfig connectionConfig2){
        return  connectionConfig1.getConnections() <= connectionConfig2.getConnections() ? 1 : 0;
    }
}
