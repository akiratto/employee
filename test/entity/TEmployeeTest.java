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
    public void testNormalValidationName() {
        System.out.println("testNormalValidationName");
        TEmployee instance = new TEmployee();
        instance.setName("Yamada tarou");
        instance.setGender("M");
        instance.setPhone("000-1111-2222");
        instance.setMobilePhone("000-1111-2222");
        instance.setZipCode("900-1111");
        instance.setAddress("〇〇県〇〇市");
        instance.setRemarks("備考あいうえおかきくけこ");
        
        ValidatorFactory vf = Validation.buildDefaultValidatorFactory();
        Validator validator = vf.getValidator();
        
        Set<ConstraintViolation<TEmployee>> vRet = validator.validate(instance);
        assertTrue(vRet.isEmpty());       
    }
    
    @Test
    public void testAbnormalValidationNameIsNull() {
        System.out.println("testAbnormalValidationNameIsNull");
        TEmployee instance = new TEmployee();
        instance.setName(null);
        instance.setGender("M");
        instance.setPhone("000-1111-2222");
        instance.setMobilePhone("000-1111-2222");
        instance.setZipCode("900-1111");
        instance.setAddress("〇〇県〇〇市");
        instance.setRemarks("備考あいうえおかきくけこ");
        
        ValidatorFactory vf = Validation.buildDefaultValidatorFactory();
        Validator validator = vf.getValidator();
        
        Set<ConstraintViolation<TEmployee>> vRet = validator.validate(instance);
        ConstraintViolation<TEmployee> message = vRet.stream().findFirst().orElse(null);
        assertNotNull(message);
        assertEquals(message.getMessage(), "社員名の入力は必須です。");
    }
    
    @Test
    public void testAbnormalValidationNameIsEmpty() {
        System.out.println("testAbnormalValidationNameIsEmpty");
        TEmployee instance = new TEmployee();
        instance.setName("");
        instance.setGender("M");
        instance.setPhone("000-1111-2222");
        instance.setMobilePhone("000-1111-2222");
        instance.setZipCode("900-1111");
        instance.setAddress("〇〇県〇〇市");
        instance.setRemarks("備考あいうえおかきくけこ");
        
        ValidatorFactory vf = Validation.buildDefaultValidatorFactory();
        Validator validator = vf.getValidator();
        
        Set<ConstraintViolation<TEmployee>> vRet = validator.validate(instance);
        ConstraintViolation<TEmployee> message = vRet.stream().findFirst().orElse(null);
        assertNotNull(message);
        assertEquals(message.getMessage(), "社員名の入力は必須です。");
    }

    @Test
    public void testAbnormalValidationNameIs60_Over() {
        System.out.println("testAbnormalValidationNameIs60_Over");
        TEmployee instance = new TEmployee();
        instance.setName("0123456789012345678012345678012345678901234567890123456789012345678901234567890");
        instance.setGender("F");
        instance.setPhone("000-1111-2222");
        instance.setMobilePhone("000-1111-2222");
        instance.setZipCode("900-1111");
        instance.setAddress("〇〇県〇〇市");
        instance.setRemarks("備考あいうえおかきくけこ");
        
        ValidatorFactory vf = Validation.buildDefaultValidatorFactory();
        Validator validator = vf.getValidator();
        
        Set<ConstraintViolation<TEmployee>> vRet = validator.validate(instance);
        ConstraintViolation<TEmployee> message = vRet.stream().findFirst().orElse(null);
        assertNotNull(message);
        assertEquals(message.getMessage(), "社員名は1～60文字以内で入力してください。");
    }
    
    @Test
    public void testNormalValidationGenderIsM() {
        System.out.println("testNormalValidationGenderIsM");
        TEmployee instance = new TEmployee();
        instance.setName("Yamada tarou");
        instance.setGender("M");
        instance.setPhone("000-1111-2222");
        instance.setMobilePhone("000-1111-2222");
        instance.setZipCode("900-1111");
        instance.setAddress("〇〇県〇〇市");
        instance.setRemarks("備考あいうえおかきくけこ");
        
        ValidatorFactory vf = Validation.buildDefaultValidatorFactory();
        Validator validator = vf.getValidator();
        
        Set<ConstraintViolation<TEmployee>> vRet = validator.validate(instance);
        assertTrue(vRet.isEmpty());
    }
    
    @Test
    public void testNormalValidationGenderIsF() {
        System.out.println("testNormalValidationGenderIsF");
        TEmployee instance = new TEmployee();
        instance.setName("Yamada hanako");
        instance.setGender("F");
        instance.setPhone("000-1111-2222");
        instance.setMobilePhone("000-1111-2222");
        instance.setZipCode("900-1111");
        instance.setAddress("〇〇県〇〇市");
        instance.setRemarks("備考あいうえおかきくけこ");
        
        ValidatorFactory vf = Validation.buildDefaultValidatorFactory();
        Validator validator = vf.getValidator();
        
        Set<ConstraintViolation<TEmployee>> vRet = validator.validate(instance);
        assertTrue(vRet.isEmpty());
    }
    
   @Test
    public void testNormalValidationGenderIsO() {
        System.out.println("testNormalValidationGenderIsO");
        TEmployee instance = new TEmployee();
        instance.setName("Yamada saburo");
        instance.setGender("O");
        instance.setPhone("000-1111-2222");
        instance.setMobilePhone("000-1111-2222");
        instance.setZipCode("900-1111");
        instance.setAddress("〇〇県〇〇市");
        instance.setRemarks("備考あいうえおかきくけこ");
        
        ValidatorFactory vf = Validation.buildDefaultValidatorFactory();
        Validator validator = vf.getValidator();
        
        Set<ConstraintViolation<TEmployee>> vRet = validator.validate(instance);
        assertTrue(vRet.isEmpty());
    }
    
    @Test
    public void testAbnormalValidationGenderIsOtherThanMFO() {
        System.out.println("testAbnormalValidationGenderIsOtherThanMFO");
        TEmployee instance = new TEmployee();
        instance.setGender("Q");
        instance.setPhone("000-1111-2222");
        instance.setMobilePhone("000-1111-2222");
        instance.setZipCode("900-1111");
        instance.setAddress("〇〇県〇〇市");
        instance.setRemarks("備考あいうえおかきくけこ");
        
        ValidatorFactory vf = Validation.buildDefaultValidatorFactory();
        Validator validator = vf.getValidator();
        
        Set<ConstraintViolation<TEmployee>> vRet = validator.validate(instance);
        ConstraintViolation<TEmployee> message = vRet.stream().findFirst().orElse(null);
        assertNotNull(message);
        assertEquals(message.getMessage(), "性別は男性(M),女性(F),その他(O)のいずれかを入力してください:Q");
    }
}
