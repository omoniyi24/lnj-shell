package com.omoniyi24.lnjshell;

import org.springframework.stereotype.Component;

/**
 * @author OMONIYI ILESANMI
 */
@Component
public class LnjServiceUtil {

    private LNJService LNJService;

    public void setLDJService(LNJService LNJService) {
        this.LNJService = LNJService;
    }

    public LNJService getLDJService(){
        return LNJService;
    }


}
