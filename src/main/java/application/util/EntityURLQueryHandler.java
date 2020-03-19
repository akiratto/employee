/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package application.util;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import static java.util.stream.Collectors.joining;
import javax.enterprise.context.Dependent;

/**
 *
 * @author owner
 */
@Dependent
public class EntityURLQueryHandler<E extends Serializable> implements Serializable  {
    
    public Map<String,String> generateQueryStrings(E searchCondition)
    {
        Map<String, String> queryStrings = new HashMap<>();
        for(Field field : searchCondition.getClass().getDeclaredFields()) {
            try {
                Class<?> fieldType = field.getType();
                String fieldName = toSnakeCase(field.getName());
                
                Object fieldValue = null;
                boolean tmpAccessible = field.isAccessible();
                field.setAccessible(true);
                fieldValue = field.get(searchCondition);
                field.setAccessible(tmpAccessible);

                String fieldValueAsString 
                        = fieldValue instanceof Integer && fieldValue != null                                          ? ((Integer)fieldValue).toString()
                        : fieldValue instanceof String  && fieldValue != null && ((String)fieldValue).isEmpty()==false ? (String)fieldValue
                        : fieldType.isEnum() && fieldValue != null                                                     ? ((Enum)fieldValue).name()
                        : fieldValue instanceof Date && fieldValue != null                                             ? (new SimpleDateFormat("yyyy/MM/dd")).format((Date)fieldValue) 
                        : null;
                if(fieldValueAsString != null) {
                    queryStrings.put(fieldName, fieldValueAsString);
                }
            } catch(IllegalAccessException | IllegalArgumentException e) {
                
            }
        }
        return queryStrings;
    }
    
    public String generateString(E searchCondition)
    {
        return generateQueryStrings(searchCondition)
                .entrySet()
                .stream()
                .map(e -> e.getKey() + "=" + urlEncode(e.getValue()))
                .collect(joining("&"));
    }
    
    public String urlEncode(String target)
    {
        try {
            return URLEncoder.encode(target, "UTF-8");
        } catch(UnsupportedEncodingException ex) {
            return "";
        }
    }   
    
    private String toSnakeCase(String target)
    {
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < target.length(); i++) {
            char ch = target.charAt(i);
            if('A' <= ch && ch <= 'Z') {
                sb.append('_');
                sb.append(Character.toLowerCase(ch));
            } else {
                sb.append(ch);
            }
        }
        return sb.toString();
    }    
}
