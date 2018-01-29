package de.htwsaar.server.config;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class FloodingCheck {
    private List<String> floodingGUIDs = new ArrayList<String>();

     public boolean isPresent(String guid){
        for(String guidfromlist : floodingGUIDs){
            if(guidfromlist.equals(guid)){
                return true;
            }
        }
        return false;
     }

     public void addGui(String guid){
         if(!isPresent(guid)) {
             floodingGUIDs.add(guid);
         }
     }

}
