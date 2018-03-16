package de.htwsaar.server.config;

import java.util.UUID;
/**
 * Created by Timo on 24.01.2017.
 *
 * Generiert global unique identifier
 */
public class GUID {

    public  String generateGUID() {
            UUID uuid = UUID.randomUUID();
              return uuid.toString();
        }

}
