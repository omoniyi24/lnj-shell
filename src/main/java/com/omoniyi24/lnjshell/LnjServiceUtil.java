package com.omoniyi24.lnjshell;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author OMONIYI ILESANMI
 */
@Component
public class LnjServiceUtil {

    private LDJService ldjService;

    public void setLDJService(LDJService ldjService) {
        this.ldjService = ldjService;
    }

    public LDJService getLDJService(){
        return ldjService;
    }


}
