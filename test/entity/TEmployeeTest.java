package entity;

import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Owner
 */
public class TEmployeeTest {
    
    public TEmployeeTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    @Test
    public void testValidationNameIsNull() {
        System.out.println("testValidationNameIsNull");
        TEmployee instance = new TEmployee();
        instance.setName(null);
        instance.setGender("M");
        
        ValidatorFactory vf = Validation.buildDefaultValidatorFactory();
        Validator validator = vf.getValidator();
        
        Set<ConstraintViolation<TEmployee>> vRet = validator.validate(instance);
        ConstraintViolation<TEmployee> message = vRet.stream().findFirst().orElse(null);
        assertNotNull(message);
        assertEquals(message.getMessage(), "社員名の入力は必須です。");
    }
    
    @Test
    public void testValidationNameIsEmpty() {
        System.out.println("testValidationNameIsEmpty");
        TEmployee instance = new TEmployee();
        instance.setName("");
        instance.setGender("M");
        
        ValidatorFactory vf = Validation.buildDefaultValidatorFactory();
        Validator validator = vf.getValidator();
        
        Set<ConstraintViolation<TEmployee>> vRet = validator.validate(instance);
        ConstraintViolation<TEmployee> message = vRet.stream().findFirst().orElse(null);
        assertNotNull(message);
        assertEquals(message.getMessage(), "社員名の入力は必須です。");
    }

    @Test
    public void testValidationName60Over() {
        System.out.println("testValidationName60Over");
        TEmployee instance = new TEmployee();
        instance.setName("0123456789012345678012345678012345678901234567890123456789012345678901234567890");
        instance.setGender("F");
        
        ValidatorFactory vf = Validation.buildDefaultValidatorFactory();
        Validator validator = vf.getValidator();
        
        Set<ConstraintViolation<TEmployee>> vRet = validator.validate(instance);
        ConstraintViolation<TEmployee> message = vRet.stream().findFirst().orElse(null);
        assertNotNull(message);
        assertEquals(message.getMessage(), "社員名は1～60文字以内で入力してください。");
    }
    
    @Test
    public void testValidationGenderOther() {
        System.out.println("testValidationGenderOther");
        TEmployee instance = new TEmployee();
        instance.setGender("Q");
        
        ValidatorFactory vf = Validation.buildDefaultValidatorFactory();
        Validator validator = vf.getValidator();
        
        Set<ConstraintViolation<TEmployee>> vRet = validator.validate(instance);
        ConstraintViolation<TEmployee> message = vRet.stream().findFirst().orElse(null);
        assertNotNull(message);
        assertEquals(message.getMessage(), "性別は男性(M),女性(F),その他(O)のいずれかを入力してください:Q");
    }


}
