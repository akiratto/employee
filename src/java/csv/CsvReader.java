package csv;

import csv.annotation.CsvColumn;
import csv.annotation.CsvColumnFormula;
import csv.annotation.CsvConverter;
import csv.converter.CsvColumnConverter;
import csv.exception.CsvReadLineException;
import java.io.BufferedReader;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;
import javax.el.ELProcessor;
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
    private int lineCount;
    private CsvReadLineCustomValidationHandler csvReadCustomErrorHandler;
    
    private BufferedReader csvFileBr;
    private String[] headers;
    
    public CsvReader(Path csvFilePath, Charset charset) throws IOException
    {
        this.csvFilePath = csvFilePath;
        this.charset = charset;   
        this.lineCount = 0;
        this.csvFileBr = Files.newBufferedReader(csvFilePath, charset);
        this.headers = csvFileBr.readLine().split(",");
        this.csvReadCustomErrorHandler = null;
    }
    
    public CsvReader(Path csvFilePath, 
                     Charset charset, 
                     CsvReadLineCustomValidationHandler csvReadCustomErrorHandler) throws IOException
    {
        this.csvFilePath = csvFilePath;
        this.charset = charset;   
        this.lineCount = 0;
        this.csvFileBr = Files.newBufferedReader(csvFilePath, charset);
        this.headers = csvFileBr.readLine().split(",");
        this.csvReadCustomErrorHandler = csvReadCustomErrorHandler;
    }

    @Override
    public void close() throws IOException {
        this.csvFileBr.close();
    }

    public Path getCsvFilePath() {
        return csvFilePath;
    }

    public Charset getCharset() {
        return charset;
    }

    public int getLineCount() {
        return lineCount;
    }

    public <T> T readLine(Class<T> clazz) 
            throws CsvReadLineException, IOException
    {
        ValidatorFactory vf = Validation.buildDefaultValidatorFactory();
        Validator validator = vf.getValidator();
        
        //CSVの行の内容を読み込む
        String line = csvFileBr.readLine();
        if(line == null) return null;
        String[] rowData = line.split(",");
        this.lineCount++;

        //現在の行の、カラム名と値の組み合わせを持つ連想配列を作成する
        Map<String, String> headerAndValues = new HashMap<>();
        for(int col=0; col < headers.length; col++) {
            headerAndValues.put(headers[col], rowData[col]);
        }
        
        //カラム名とその値をEL式に変数として登録する
        ELProcessor elProcessor = new ELProcessor();
        for(Entry<String,String> entry : headerAndValues.entrySet()) {
            elProcessor.defineBean(entry.getKey(), entry.getValue());
        }
        
        //読み込んだCSV行の内容を格納するクラスのインスタンスを作成する
        T instance;
        try {
            instance = clazz.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            //インスタンスの生成に失敗した場合は、パラメータ例外を投げる
            throw new IllegalArgumentException(e);
        }
        
        //作成したインスタンスの各フィールドにCSVで読み込んだ行の内容を設定する
        //フィールドへの値設定時にエラーが発生した場合は、fieldErrorMessagesへ
        //フィールドごとに複数のメッセージを設定する
        Map<String, List<String>> fieldErrorMessages = new HashMap<>();
        for(Field field : clazz.getDeclaredFields()) {
            CsvColumn csvColumn = field.getAnnotation(CsvColumn.class);
            CsvConverter csvConverter = field.getAnnotation(CsvConverter.class);
            CsvColumnFormula csvColumnFormula = field.getAnnotation(CsvColumnFormula.class);
            
            if(csvColumn != null) {
                if(headerAndValues.containsKey(csvColumn.field())) {
                    String csvColumnValue = headerAndValues.get(csvColumn.field());
                    processCsvColumn(csvColumnValue, validator, clazz, instance, field, csvConverter, fieldErrorMessages);
                }
            } else if(csvColumnFormula != null) {
                String csvColumnValue = (String)elProcessor.eval(csvColumnFormula.formula());
                processCsvColumn(csvColumnValue, validator, clazz, instance, field, csvConverter, fieldErrorMessages);
            }
        }
        
        Map<String,List<String>> mergeFieldErrorMessages = new HashMap<>();
        if(this.csvReadCustomErrorHandler != null) {
        //カスタムエラーハンドラーが指定されている場合は、それでバリデーションを実行し
        //そのエラーメッセージを標準のバリデーションのエラーメッセージを一つにまとめる。
            
            Map<String,List<String>> customFieldErrorMessages 
                    = this.csvReadCustomErrorHandler.validate(clazz, headerAndValues);
            if(customFieldErrorMessages != null && customFieldErrorMessages.isEmpty()==false) {
            //カスタムバリデーションでエラーが発生した場合
                for(Field field : clazz.getDeclaredFields()) {
                    List<String> mergeErrorMessages = new ArrayList<>();

                    if(fieldErrorMessages.containsKey(clazz.getName() + "." + field.getName())) {
                        mergeErrorMessages.addAll(fieldErrorMessages.get(clazz.getName() + "." + field.getName()));
                    }
                    if(customFieldErrorMessages.containsKey(clazz.getName() + "." + field.getName())) {
                        mergeErrorMessages.addAll(customFieldErrorMessages.get(clazz.getName() + "." + field.getName()));
                    }
                    if(mergeErrorMessages.isEmpty()==false) {
                        mergeFieldErrorMessages.put(clazz.getName() + "." + field.getName(), mergeErrorMessages);
                    }
                }
            } else {
            //カスタムバリデーションでエラーが発生しなかった場合
                mergeFieldErrorMessages = fieldErrorMessages;
            }
        } else {
        //カスタムエラーハンドラーが指定されていない場合は何もしない。 
            mergeFieldErrorMessages = fieldErrorMessages;
        }

        //エラーメッセージが1つでも存在する場合はCsvReadLineException例外をスローする
        if(mergeFieldErrorMessages.size() > 0) {
            throw new CsvReadLineException(csvFilePath, lineCount, clazz, mergeFieldErrorMessages);
        }
        
        //エラーがなければ行の内容を読み込んだインスタンスを返す
        return instance;
    }    
    
    //指定されたCsvのカラム値をフィールドに設定する
    private <T> void processCsvColumn(
                        String csvColumnValue,      //CSVのカラム値
                        Validator validator,        //Bean Validator
                        Class clazz,                //行内容を読み込むクラス
                        T instance,                 //行内容を読み込むクラスのインスタンス
                        Field field,                //CSVのカラム値を設定するフィールド
                        CsvConverter csvConverter,  //フィールドに指定された@CsvConverterアノテーション
                        Map<String, List<String>> fieldErrorMessages)   //フィールド設定時に発生したエラーのメッセージ
    {
        if(csvConverter != null) {
        //コンバータが指定されている場合
        
            //@CsvConverterアノテーションで指定されたCsvColumnConverterクラスを取得する
            Class<? extends CsvColumnConverter> csvColumnConverterClass = csvConverter.converter();
            
            //CsvColumnConverterクラスのインスタンスを生成する
            CsvColumnConverter csvColumnConverter;
            try {
                csvColumnConverter = csvColumnConverterClass.newInstance();
            } catch(IllegalAccessException | InstantiationException e) {
                throw new IllegalArgumentException(e);
            }
            
            //CSVカラムの値を対応するフィールドに格納する値へ変換する
            Object value = csvColumnConverter.convertToFieldObject(csvColumnValue);
            
            if(value != null) {
            //コンバータによる変換に成功した場合
                //フィールドの型となるクラスを取得する
                Class<?> fieldType = field.getType();
                
                if(fieldType.isInstance(value)) {
                //変換後の値がフィールドと同じ型である場合
                    //フィールドへのアクセスを一時的に許可してカラム値を
                    //フィールドへ設定する。
                    //設定後はフィールドへのアクセス許可は元に戻す。
                    boolean tmpAccessible = field.isAccessible();
                    field.setAccessible(true);
                    try {
                        field.set(instance, value);
                    } catch(IllegalAccessException e) {
                        throw new IllegalArgumentException(e);
                    }
                    field.setAccessible(tmpAccessible);
                    
                    //フィールドへのバリデーションを実行する
                    Set<ConstraintViolation<T>> messages = validator.validateProperty(instance, field.getName());

                    //バリデーションの結果、エラーがあればメッセージを設定する
                    if(messages.size() > 0) {
                        List<String> errorMessages = messages.stream().map(m -> m.getMessage()).collect(Collectors.toList());
                        fieldErrorMessages.put(clazz.getName()+"." + field.getName(), errorMessages);
                    }
                } else {
                //変換後の値がフィールドと異なる型である場合
                
                    //変換不可のエラーメッセージを設定する
                    String errorMessage = clazz.getName() 
                                        + "." + field.getName() 
                                        + " can not convert to " + fieldType.getName() + "!";
                    List<String> errorMessages = Arrays.asList(errorMessage);
                    fieldErrorMessages.put(clazz.getName()+"." + field.getName(), errorMessages);
                }
            } else {
            //コンバータによる変換に失敗した場合
                //フィールドの型となるクラスを取得する
                Class<?> fieldType = field.getType();
                
                //コンバータ変換不可のエラーメッセージを設定する
                String errorMessage = clazz.getName() 
                                        + "." + field.getName() 
                                        + " can not convert to " + fieldType.getName() 
                                        + "in " + csvConverter.getClass().getName() + "!";
                List<String> errorMessages = Arrays.asList(errorMessage);
                fieldErrorMessages.put(clazz.getName()+"." + field.getName(), errorMessages);
           }
        } else {
        //コンバータが指定されていない場合
            //フィールドの型となるクラスを取得する
            Class<?> fieldType = field.getType();
            
            if(fieldType == String.class) {
            //フィールドの型がStringクラスの場合
                
                //フィールドへのアクセスを一時的に許可してカラム値を
                //フィールドへ設定する。
                //設定後はフィールドへのアクセス許可は元に戻す。
                boolean tmpAccessible = field.isAccessible();
                field.setAccessible(true);
                try {
                    field.set(instance, csvColumnValue);
                } catch(IllegalAccessException e) {
                    throw new IllegalArgumentException(e);
                }
                field.setAccessible(tmpAccessible);
                
                //フィールドへのバリデーションを実行する
                Set<ConstraintViolation<T>> messages = validator.validateProperty(instance, field.getName());
                
                //バリデーションの結果、エラーがあればメッセージを設定する
                if(messages.size() > 0) {
                    List<String> errorMessages = messages.stream().map(m -> m.getMessage()).collect(Collectors.toList());
                    fieldErrorMessages.put(clazz.getName()+"." + field.getName(), errorMessages);
                }
            } else {
            //フィールドの型がStringクラス以外の場合
            
                //変換不可のエラーメッセージを設定する
                String errorMessage = clazz.getName() 
                                    + "." + field.getName() 
                                    + " can not convert to " + fieldType.getName() + "!";
                List<String> errorMessages = Arrays.asList(errorMessage);
                fieldErrorMessages.put(clazz.getName() + "." + field.getName(), errorMessages);
            }
        }
    }
}
