package csv;

import csv.annotation.CsvColumn;
import csv.annotation.CsvConverter;
import csv.converter.CsvColumnConverter;
import csv.exception.CsvReadLineException;
import entity.TEmployee;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

/**
 *
 * @author Owner
 */
public class CsvReader implements AutoCloseable {
    private Path csvFilePath;
    private Charset charset;
    
    private BufferedReader csvFileBr;
    private String[] headers;
    private int lineCount;
    
    
    public CsvReader(Path csvFilePath, Charset charset) throws IOException
    {
        this.csvFilePath = csvFilePath;
        this.charset = charset;   
        this.csvFileBr = Files.newBufferedReader(csvFilePath, charset);
        this.headers = csvFileBr.readLine().split(",");
        this.lineCount = 0;
    }

    @Override
    public void close() throws IOException {
        this.csvFileBr.close();
    }

    public <T> T readLine(Class<T> clazz) 
            throws CsvReadLineException, IOException
    {
        ValidatorFactory vf = Validation.buildDefaultValidatorFactory();
        Validator validator = vf.getValidator();
        
        String line = csvFileBr.readLine();
        if(line == null) return null;
        
        //読み込んだCSV行の内容を格納するクラスのインスタンスを作成する
        T instance;
        try {
            instance = clazz.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            //インスタンスの生成に失敗した場合は、パラメータ例外を投げる
            throw new IllegalArgumentException(e);
        }
        
        //行数をカウントする
        this.lineCount++;

        Map<String, List<String>> fieldErrorMessages = new HashMap<>();
        String[] rowData = line.split(",");
        for(int col=0; col < headers.length; col++) {
            String headerName = headers[col];

            for(Field field : clazz.getDeclaredFields()) {
                CsvColumn csvColumn = field.getAnnotation(CsvColumn.class);
                CsvConverter csvConverter = field.getAnnotation(CsvConverter.class);

                if(csvColumn == null) continue;
                if(csvColumn.field().equals(headerName) == false) continue;

                if(csvConverter != null) {
                    Class<? extends CsvColumnConverter> csvColumnConverterClass = csvConverter.converter();
                    CsvColumnConverter csvColumnConverter;
                    try {
                        csvColumnConverter = csvColumnConverterClass.newInstance();
                    } catch(IllegalAccessException | InstantiationException e) {
                        throw new IllegalArgumentException(e);
                    }
                    Object value = csvColumnConverter.convertToFieldObject(rowData[col]);
                    Class<?> fieldType = field.getType();
                    if(value != null) {
                        if(fieldType.isInstance(value)) {
                            boolean tmpAccessible = field.isAccessible();
                            field.setAccessible(true);
                            try {
                                field.set(instance, value);
                            } catch(IllegalAccessException e) {
                                throw new IllegalArgumentException(e);
                            }
                            field.setAccessible(tmpAccessible);
                            Set<ConstraintViolation<T>> messages = validator.validateProperty(instance, field.getName());
                            
                            if(messages.size() > 0) {
                                List<String> errorMessages = messages.stream().map(m -> m.getMessage()).collect(Collectors.toList());
                                fieldErrorMessages.put(clazz.getName()+"." + field.getName(), errorMessages);
                            }
                        } else {
                            //変換不可のエラー出力
                            String errorMessage = clazz.getName() 
                                                + "." + field.getName() 
                                                + " can not convert to " + fieldType.getName() + "!";
                            
                            List<String> errorMessages = Arrays.asList(errorMessage);
                            fieldErrorMessages.put(clazz.getName()+"." + field.getName(), errorMessages);
                        }
                    } else {
                        //コンバータ内で変換不可の場合、エラー出力
                        String errorMessage = clazz.getName() 
                                                + "." + field.getName() 
                                                + " can not convert to " + fieldType.getName() 
                                                + "in " + csvConverter.getClass().getName() + "!";
                        
                        List<String> errorMessages = Arrays.asList(errorMessage);
                        fieldErrorMessages.put(clazz.getName()+"." + field.getName(), errorMessages);
                   }
                } else {
                    Class<?> fieldType = field.getType();
                    if(fieldType == String.class) {
                        boolean tmpAccessible = field.isAccessible();
                        field.setAccessible(true);
                        try {
                            field.set(instance, rowData[col]);
                        } catch(IllegalAccessException e) {
                            throw new IllegalArgumentException(e);
                        }
                        field.setAccessible(tmpAccessible);
                        Set<ConstraintViolation<T>> messages = validator.validateProperty(instance, field.getName());
                        if(messages.size() > 0) {
                            messages.stream().forEach(m -> System.out.println(clazz.getName() + "." + field.getName() + ":" + m.getMessage()));
                        }
                    } else {
                        //変換不可のエラー出力
                        String errorMessage = clazz.getName() 
                                            + "." + field.getName() 
                                            + " can not convert to " + fieldType.getName() + "!";

                        List<String> errorMessages = Arrays.asList(errorMessage);
                        fieldErrorMessages.put(clazz.getName() + "." + field.getName(), errorMessages);
                    }
                }
            }
        }
        if(fieldErrorMessages.size() > 0) {
            throw new CsvReadLineException(csvFilePath, lineCount, clazz, fieldErrorMessages);
        }
        return instance;
    }       
}
