package client.ws;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class UrlList {
    private List<String> urlList = new ArrayList<String>();


    public void addUrl(String url){
        urlList.add(url);
    }

    public String getUrl(){
        return urlList.get(urlList.size()-1);
    }

    public void remove(){
        urlList.remove(urlList.size()-1);
    }
    public int getSize(){
        return urlList.size();
    }
}
