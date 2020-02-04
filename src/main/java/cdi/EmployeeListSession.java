package cdi;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;
import util.ResourceFileHandler;
import util.UserEntityClassLoader;

/**
 *
 * @author Owner
 */
@Named
@SessionScoped
public class EmployeeListSession implements Serializable {
    private String sessionId;
    private String test;
    private String employeeEntityTemplate;
    
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
        
        try {
            this.test = ResourceFileHandler.loadResource(this.getClass().getClassLoader(), "entity/template/test");
        } catch(IOException ex) {
            ex.printStackTrace();
        }
        
        try {
            this.employeeEntityTemplate = ResourceFileHandler.loadResource(
                                                                this.getClass().getClassLoader(), 
                                                                "entity/template/TEmployee.jtpl");
        } catch(IOException ex) {
            ex.printStackTrace();
        }
        
        String employeeEntitySource = this.employeeEntityTemplate.replaceAll("%SESSIONID%", this.sessionId);
        String employeeEntitySourceFileName = "TEmployee_" + this.sessionId + ".java";
        
        try {
            Files.createFile(Paths.get(employeeEntitySourceFileName));
            String[] lineArray = employeeEntitySource.split("\\n");
            List<String> lines = Arrays.asList(lineArray);
            Files.write(Paths.get(employeeEntitySourceFileName), lines, Charset.forName("UTF-8"), StandardOpenOption.WRITE);
        } catch(IOException ex) {
            ex.printStackTrace();
        }
        
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        if(compiler == null) {
            System.out.println("Compiler is not found");
            return;
        }
        String sep = File.pathSeparator;
        String classPath = System.getProperty("entity.compiler.classpath") + sep + "C:/Users/owner/Documents/NetBeansProjects/Employee/target/Employee-1.0-SNAPSHOT/WEB-INF/classes";
        String p = Paths.get(employeeEntitySourceFileName).toAbsolutePath().toString();
        int ret = compiler.run(null, null, null, new String[] { "-encoding", "UTF-8", "-classpath", classPath, p } );
        if(ret == 0) {
            System.out.println("Compile successed");
        } else {
            System.out.println("Compile failed");
        }        
        
    }

    public String getSessionId() {
        return sessionId;
    }

    public String getTest() {
        return test;
    }
}
