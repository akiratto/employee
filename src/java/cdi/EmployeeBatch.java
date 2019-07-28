package cdi;

import csv.CsvReader;
import csv.exception.CsvReadLineException;
import entity.TEmployee;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import static java.util.stream.Collectors.joining;
import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.context.Flash;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.Part;
import javax.transaction.Transactional;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

/**
 *
 * @author Owner
 */
@Named
@ViewScoped
public class EmployeeBatch implements Serializable {
    @PersistenceContext
    EntityManager em;
    
    private Part csvFile;
    private String uploadLog;
    private String folder = "c:\\Temp";
    private String logMessage = "";
    
    @PostConstruct
    public void init()
    {
        Flash flash = FacesContext.getCurrentInstance().getExternalContext().getFlash();
        this.logMessage = (String)flash.getOrDefault("log_message", "");
    }

    public Part getCsvFile() {
        return csvFile;
    }

    public void setCsvFile(Part csvFile) {
        this.csvFile = csvFile;
    }

    public String getLogMessage() {
        return logMessage;
    }

    public void setLogMessage(String logMessage) {
        this.logMessage = logMessage;
    }
    
    @Transactional
    public String uploadFile()
    {
        try(InputStream input = csvFile.getInputStream()) {
            String fileName = new File(csvFile.getSubmittedFileName()).getName();
            Files.copy(input, new File(folder, fileName).toPath(),StandardCopyOption.REPLACE_EXISTING);
            
            int errorCount = 0;
            Path csvFilePath = new File(folder, fileName).toPath();
            try(CsvReader csvFile = new CsvReader(csvFilePath, Charset.forName("UTF-8"))) {
                TEmployee employee;
                do {
                    try {
                        employee = csvFile.readLine(TEmployee.class);
                        if(employee==null) break;
                        em.persist(employee);
                    } catch(CsvReadLineException e) {
                        this.logMessage += e.getErrorOccurrenceLine() + "行目に以下のエラーが発生しました。\n";
                        this.logMessage += e.getFieldErrorMessages()
                                                .entrySet()
                                                .stream()
                                                .map(msg -> "・" + msg.getValue())
                                                .collect(joining("\n"));
                        this.logMessage += "\n";
                        errorCount++;
                        if(errorCount>10) {
                            this.logMessage += "エラーが10件を超えました。処理を中止します。\n";
                            break;
                        }
                    }
                } while(true);
                
            }            
            Flash flash = FacesContext.getCurrentInstance().getExternalContext().getFlash();
            flash.put("log_message", this.logMessage);
                     
        } catch(IOException e) {
            throw new IllegalStateException(e);
        }
        String currentPage = FacesContext.getCurrentInstance().getViewRoot().getViewId();
        return currentPage + "?faces-redirect=true";
    }
}
