package cdi;

import entity.TEmployee;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.Part;
import javax.transaction.Transactional;

/**
 *
 * @author Owner
 */
@Named
@RequestScoped
public class CsvUploader {
    @PersistenceContext
    EntityManager em;
    
    private Part csvFile;
    private String uploadLog;
    private String folder = "c:\\Temp";

    public Part getCsvFile() {
        return csvFile;
    }

    public void setCsvFile(Part csvFile) {
        this.csvFile = csvFile;
    }
    
    @Transactional
    public String uploadFile()
    {
        try(InputStream input = csvFile.getInputStream()) {
            String fileName = new File(csvFile.getSubmittedFileName()).getName();
            Files.copy(input, new File(folder, fileName).toPath(),StandardCopyOption.REPLACE_EXISTING);
            
            Path csvFilePath = new File(folder, fileName).toPath();
            try(BufferedReader br = Files.newBufferedReader(csvFilePath, Charset.forName("UTF-8"))) {
                String[] headers = null;
                int lineCount = 1;
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
                        em.persist(emp);
                    }
                    lineCount++;
                }
            }
                     
        } catch(IOException e) {
            e.printStackTrace();
        }
        String currentPage = FacesContext.getCurrentInstance().getViewRoot().getViewId();
        return currentPage + "?faces-redirect=true";
    }
}
