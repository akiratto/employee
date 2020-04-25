/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

/**
 *
 * @author Owner
 */
public class StringFunctions {
    public static String toSnakeCase(String target)
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
    
    public static String likeEscape(String likeCondition)
    {
        return likeCondition
                .replaceAll("_", "\\\\_")
                .replaceAll("%", "\\\\%");
    } 
}
