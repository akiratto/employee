package cdi;

import cdi.interseptor.TransactionDebugger;
import csv.CsvReader;
import csv.exception.CsvReadLineException;
import entity.TEmployee;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import static java.util.stream.Collectors.joining;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.context.Flash;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.Part;
import javax.transaction.Transactional;
import javax.validation.constraints.NotNull;
import util.Tuple;

/**
 *
 * @author Owner
 */
@Named
@ViewScoped
public class EmployeeBatch implements Serializable {
    @PersistenceContext
    EntityManager em;
    
    @NotNull(message = "一括登録するCSVファイルを指定してください。")
    private Part csvFile;
    
    private String uploadLog;
    private String folder = "c:\\Temp";
    private String logMessage = "";
    
    @PostConstruct
    public void init()
    {
        Flash flash = FacesContext.getCurrentInstance().getExternalContext().getFlash();
        this.logMessage = (String)flash.getOrDefault("log_message", "");
        
        String message = (String)flash.getOrDefault("message", "");
        if(!message.equals("")) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(message));
        }
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
    @TransactionDebugger
    public String uploadFile()
    {
        try(InputStream input = csvFile.getInputStream()) {
            String fileName = new File(csvFile.getSubmittedFileName()).getName();
            File csvFile = new File(folder, fileName);
            Files.copy(input, csvFile.toPath(),StandardCopyOption.REPLACE_EXISTING);
            
            Integer errorCount = 0;
            Path csvFilePath = csvFile.toPath();
            TEmployee employee = null;
            try(CsvReader csvFileReader = new CsvReader(csvFilePath, Charset.forName("UTF-8"))) {
                Tuple<TEmployee,Integer> employeeAndErrorCount = null;
                do {
                        employeeAndErrorCount = this.readLine100AndCommit(csvFileReader, errorCount);
                        System.out.println("--- 100 comitted ---");
                        employee = employeeAndErrorCount._1;
                } while(employee != null);
                
            } finally {
                Files.deleteIfExists(csvFilePath);
            }    
            Flash flash = FacesContext.getCurrentInstance().getExternalContext().getFlash();
            flash.put("message", "一括登録が完了しました。");
            flash.put("log_message", this.logMessage);
                     
        } catch(IOException e) {
            throw new IllegalStateException(e);
        } finally {
            
        }
        String currentPage = FacesContext.getCurrentInstance().getViewRoot().getViewId();
        return currentPage + "?faces-redirect=true";
    }
    
    @Transactional
    private Tuple<TEmployee,Integer> readLine100AndCommit(CsvReader csvFile, Integer errorCount) throws IOException
    {
        TEmployee employee = null;
        for(int i = 1; i <= 100; i++) {
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
        }
        return new Tuple<>(employee, errorCount);
    }
}
