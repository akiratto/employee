package cdi;

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
        ValidatorFactory vf = Validation.buildDefaultValidatorFactory();
        Validator validator = vf.getValidator();
        try(InputStream input = csvFile.getInputStream()) {
            String fileName = new File(csvFile.getSubmittedFileName()).getName();
            Files.copy(input, new File(folder, fileName).toPath(),StandardCopyOption.REPLACE_EXISTING);
            
            Path csvFilePath = new File(folder, fileName).toPath();
            try(BufferedReader br = Files.newBufferedReader(csvFilePath, Charset.forName("UTF-8"))) {
                String[] headers = null;
                int lineCount = 1;
                int errorCount = 0;
                List<String> errorMessages = new ArrayList<>();
                String line;
                while((line = br.readLine())!=null) {
                    if(lineCount==1) {
                        headers = line.split(",");
                    } else {
                        String[] rowData = line.split(",");
                        TEmployee emp = new TEmployee();
                        for(int col=0; col < headers.length; col++) {
                            String headerName = headers[col];
                            switch(headerName) {
                                case "連番":
                                    emp.setEmployee_id(Integer.parseInt(rowData[col]));
                                    break;
                                case "氏名":
                                    emp.setName(rowData[col]);
                                    break;
                                case "性別":
                                    String gender = rowData[col].equals("男") ? "M"
                                                  : rowData[col].equals("女") ? "F"
                                                  : "O";
                                    emp.setGender(gender);
                                    break;
                                case "生年月日":
                                    SimpleDateFormat birthdayFormat = new SimpleDateFormat("yyyy/MM/dd");
                                    Date birthday = null;
                                    try {
                                        birthday = birthdayFormat.parse(rowData[col]);
                                    } catch(ParseException ex) {
                                        errorMessages.add("生年月日が正しい形式ではありません。(例.2019/01/01)");
                                    }
                                    emp.setBirthday(birthday);
                                    break;
                                case "電話番号":
                                    emp.setPhone(rowData[col]);
                                    break;
                                    
                                case "郵便番号":
                                    emp.setZipCode(rowData[col]);
                                    break;
                                    
                                case "住所1":
                                    emp.setAddress(rowData[col]);
                                    break;
                                    
                                case "住所2":
                                case "住所3":
                                case "住所4":
                                case "住所5":
                                    emp.setAddress(emp.getAddress() + rowData[col]);
                                    break;                                  
                            }
                        }
                        Set<ConstraintViolation<TEmployee>> vRet = validator.validate(emp);
                        if(!vRet.isEmpty()) {
                            errorMessages.addAll(
                                vRet.stream().map(e -> e.getMessage()).collect(Collectors.toList())
                            );
                            this.logMessage += lineCount + "行目に以下のエラーが発生しました。\n";
                            this.logMessage += vRet.stream().map(e -> "・" + e.getMessage()).collect(joining("\n"));
                            this.logMessage += "\n";
                            errorCount++;
                            if(errorCount>10) {
                                this.logMessage += "エラーが10件を超えました。処理を中止します。\n";
                                break;
                            }
                              
                        } else {
                            em.persist(emp);
                            em.clear();
                        }
                    }
                    lineCount++;
                }
                if(errorCount == 0) {
                    this.logMessage += "一括登録処理が完了しました。";
                }
            }
            
            Flash flash = FacesContext.getCurrentInstance().getExternalContext().getFlash();
            flash.put("log_message", this.logMessage);
                     
        } catch(IOException e) {
            e.printStackTrace();
        }
        String currentPage = FacesContext.getCurrentInstance().getViewRoot().getViewId();
        return currentPage + "?faces-redirect=true";
    }
}
