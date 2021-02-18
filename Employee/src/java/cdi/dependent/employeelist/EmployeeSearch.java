package cdi.dependent.employeelist;

import entity.TEmployee;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import static java.util.stream.Collectors.joining;
import javax.enterprise.context.Dependent;

/**
 *
 * @author owner
 */
@Dependent
public class EmployeeSearch implements Serializable {
    
    public String generateSearchURLQueryString(TEmployee searchCondition)
    {
        return generateSearchParameterMap(searchCondition)
                .entrySet()
                .stream()
                .map(e -> e.getKey() + "=" + encodeURL(e.getValue()))
                .collect(joining("&"));
    }
    
    public Map<String,String> generateSearchParameterMap(TEmployee searchCondition)
    {
        Map<String, String> queryStrings = new HashMap<>();
        if(searchCondition.getEmployeeCode()!=null) {
            queryStrings.put("employee_code",searchCondition.getEmployeeCode().toString());
        }
        if(searchCondition.getName()!=null && !searchCondition.getName().isEmpty()) {
            queryStrings.put("name",searchCondition.getName());
        }
        if(searchCondition.getGender()!=null) {
            queryStrings.put("gender",searchCondition.getGender().name());
        }
        if(searchCondition.getBirthday()!=null) {
            SimpleDateFormat birthdayFormat = new SimpleDateFormat("yyyy/MM/dd");
            queryStrings.put("birthday", birthdayFormat.format(searchCondition.getBirthday()));
        }
        if(searchCondition.getPhone()!=null && !searchCondition.getPhone().isEmpty()) {
            queryStrings.put("phone",searchCondition.getPhone());
        }
        if(searchCondition.getMobilePhone()!=null && !searchCondition.getMobilePhone().isEmpty()) {
            queryStrings.put("mobile_phone",searchCondition.getMobilePhone());
        }
        if(searchCondition.getZipCode()!=null && !searchCondition.getZipCode().isEmpty()) {
            queryStrings.put("zip_code",searchCondition.getZipCode());
        }
        if(searchCondition.getAddress()!=null && !searchCondition.getAddress().isEmpty()) {
            queryStrings.put("address",searchCondition.getAddress());
        }
        if(searchCondition.getRemarks()!=null && !searchCondition.getRemarks().isEmpty()) {
            queryStrings.put("remarks",searchCondition.getRemarks());
        }
        return queryStrings;
    }
    
    private String encodeURL(String target)
    {
        try {
            return URLEncoder.encode(target, "UTF-8");
        } catch(UnsupportedEncodingException ex) {
            ex.printStackTrace();
            return "";
        }
    }    
}
