package csv;

import csv.annotation.CsvColumn;
import csv.annotation.CsvConverter;
import csv.converter.CsvColumnConverter;
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
import java.util.Date;
import java.util.List;

/**
 *
 * @author Owner
 */
public class CsvFile implements AutoCloseable {
    private Path csvFilePath;
    private Charset charset;
    
    private BufferedReader csvFileBr;
    private String[] headers;
    
    
    public CsvFile(Path csvFilePath, Charset charset) throws IOException
    {
        this.csvFilePath = csvFilePath;
        this.charset = charset;   
        this.csvFileBr = Files.newBufferedReader(csvFilePath, charset);
        this.headers = csvFileBr.readLine().split(",");
    }

    @Override
    public void close() throws Exception {
        this.csvFileBr.close();
    }

    public <T> T readLine(Class<T> clazz) throws IOException, IllegalArgumentException, IllegalAccessException, InstantiationException
    {
        String line = csvFileBr.readLine();
        if(line == null) return null;
        
        T instance = clazz.newInstance();

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
                    CsvColumnConverter csvColumnConverter = csvColumnConverterClass.newInstance();
                    Object value = csvColumnConverter.convertToFieldObject(rowData[col]);
                    Class<?> fieldType = field.getType();
                    if(value != null) {
                        if(fieldType.isInstance(value)) {
                            boolean tmpAccessible = field.isAccessible();
                            field.setAccessible(true);
                            field.set(instance, value);
                            field.setAccessible(tmpAccessible);
                        } else {
                            //変換不可のエラー出力
                            System.out.println(clazz.getName() + "." + field.getName() + " can not convert to " + fieldType.getName() + "!");
                        }
                    } else {
                        //コンバータ内で変換不可の場合、エラー出力
                        System.out.println(clazz.getName() + "." + field.getName() + " can not convert to " + fieldType.getName() + "in " + csvConverter.getClass().getName() + "!");
                    }
                } else {
                    Class<?> fieldType = field.getType();
                    if(fieldType == String.class) {
                        boolean tmpAccessible = field.isAccessible();
                        field.setAccessible(true);
                        field.set(instance, rowData[col]);
                        field.setAccessible(tmpAccessible);
                    } else {
                        //変換不可のエラー出力
                        System.out.println(clazz.getName() + "." + field.getName() + " can not convert to " + fieldType.getName() + "!");
                    }
                }
            }
        }
        return instance;
    }       
}
