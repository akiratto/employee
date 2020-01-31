/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cdi;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import util.UserEntityClassLoader;

/**
 *
 * @author Owner
 */
@Named
@SessionScoped
public class EmployeeListSession implements Serializable {
    private String sessionId;
    
    @PostConstruct
    public void init()
    {
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        this.sessionId = externalContext.getSessionId(false);
        if(sessionId.isEmpty()) {
            //エラー
            System.out.println("no session");
            return;
        }
        
        File sessionFile = new File("s_" + sessionId);
        try {
            sessionFile.createNewFile();
            System.out.println("user.dir=" + System.getProperty("user.dir"));
            System.out.println("sessionFile=" + sessionFile.getAbsolutePath());
        } catch(IOException ex) {
            ex.printStackTrace();
        }
        
    }

    public String getSessionId() {
        return sessionId;
    }
    
    
}
