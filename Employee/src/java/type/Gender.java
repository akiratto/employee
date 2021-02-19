package type;

import java.util.function.Function;
import java.util.function.Predicate;

/**
 *
 * @author Owner
 */
public enum Gender {
    M,
    F,
    O;
    
    public String jpName()
    {
        return name().equals("M") ? "男" :
               name().equals("F") ? "女" :
               name().equals("O") ? "他" : "";
    }
    
    public String fullName()
    {
        return name().equals("M") ? "Male" :
               name().equals("F") ? "Female" :
               name().equals("O") ? "Other" : "";
    }
    
    public String jpFullName()
    {
        return name().equals("M") ? "男性" :
               name().equals("F") ? "女性" :
               name().equals("O") ? "その他" : "";
    }
    
    public static Gender valueForJpName(String name)
    { 
        for(Gender gender : Gender.values()) {
            if(gender.jpName().equals(name)) {
                return gender;
            }
        }
        throw new IllegalArgumentException("failed to parse " + name);
    }
    
    public static Gender valueForFullName(String name) 
    {
        for(Gender gender : Gender.values()) {
            if(gender.fullName().equals(name)) {
                return gender;
            }
        }
        throw new IllegalArgumentException("failed to parse " + name);
    }
    
    public static Gender valueForJpFullName(String name)
    {
        for(Gender gender : Gender.values()) {
            if(gender.jpFullName().equals(name)) {
                return gender;
            }
        }
        throw new IllegalArgumentException("failed to parse " + name);
    }
    
    public static Gender valueForAllNames(String name)
    {
        for(Gender gender : Gender.values()) {
            if(gender.name().equals(name)
               || gender.jpName().equals(name)
               || gender.fullName().equals(name)
               || gender.jpFullName().equals(name)) 
            {
                return gender;
            }
        }
        throw new IllegalArgumentException("failed to parse " + name);
    }
}
