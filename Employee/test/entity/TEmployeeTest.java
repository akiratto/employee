package entity;

import type.TEmployee;
import type.Gender;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
    public void testNormalValidationName() throws ParseException {
        System.out.println("testNormalValidationName");
        TEmployee instance = new TEmployee();
        instance.setEmployeeCode("0000000001");
        instance.setName("Yamada tarou");
        instance.setGender(Gender.M);
        instance.setBirthday(new SimpleDateFormat("yyyyMMdd").parse("19810101"));
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
        instance.setEmployeeCode("0000000001");
        instance.setName(null);
        instance.setGender(Gender.M);
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
    public void testAbnormalValidationNameIsEmpty() throws ParseException {
        System.out.println("testAbnormalValidationNameIsEmpty");
        TEmployee instance = new TEmployee();
        instance.setEmployeeCode("0000000001");
        instance.setName("");
        instance.setGender(Gender.M);
        instance.setBirthday(new SimpleDateFormat("yyyyMMdd").parse("19810101"));
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
    public void testAbnormalValidationNameIs60_Over() throws ParseException {
        System.out.println("testAbnormalValidationNameIs60_Over");
        TEmployee instance = new TEmployee();
        instance.setEmployeeCode("0000000001");
        instance.setName("0123456789012345678012345678012345678901234567890123456789012345678901234567890");
        instance.setGender(Gender.F);
        instance.setBirthday(new SimpleDateFormat("yyyyMMdd").parse("19810101"));
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
    public void testNormalValidationGenderIsM() throws ParseException {
        System.out.println("testNormalValidationGenderIsM");
        TEmployee instance = new TEmployee();
        instance.setEmployeeCode("0000000001");
        instance.setName("Yamada tarou");
        instance.setGender(Gender.M);
        instance.setBirthday(new SimpleDateFormat("yyyyMMdd").parse("19810101"));
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
    public void testNormalValidationGenderIsF() throws ParseException {
        System.out.println("testNormalValidationGenderIsF");
        TEmployee instance = new TEmployee();
        instance.setEmployeeCode("0000000001");
        instance.setName("Yamada hanako");
        instance.setGender(Gender.F);
        instance.setBirthday(new SimpleDateFormat("yyyyMMdd").parse("19810101"));
        instance.setPhone("000-1111-2222");
        instance.setMobilePhone("000-1111-2222");
        instance.setZipCode("900-1111");
        instance.setAddress("〇〇県〇〇市");
        instance.setRemarks("備考あいうえおかきくけこ");
        
        ValidatorFactory vf = Validation.buildDefaultValidatorFactory();
        Validator validator = vf.getValidator();
        
        Set<ConstraintViolation<TEmployee>> vRet = validator.validate(instance);
        vRet.stream().forEach(cv -> System.out.println(cv.getMessage()));
        assertTrue(vRet.isEmpty());
    }
    
   @Test
    public void testNormalValidationGenderIsO() throws ParseException {
        System.out.println("testNormalValidationGenderIsO");
        TEmployee instance = new TEmployee();
        instance.setEmployeeCode("0000000001");
        instance.setName("Yamada saburo");
        instance.setGender(Gender.O);
        instance.setBirthday(new SimpleDateFormat("yyyyMMdd").parse("19810101"));
        instance.setPhone("000-1111-2222");
        instance.setMobilePhone("000-1111-2222");
        instance.setZipCode("900-1111");
        instance.setAddress("〇〇県〇〇市");
        instance.setRemarks("備考あいうえおかきくけこ");
        
        ValidatorFactory vf = Validation.buildDefaultValidatorFactory();
        Validator validator = vf.getValidator();
        
        Set<ConstraintViolation<TEmployee>> vRet = validator.validate(instance);
        vRet.stream().forEach(cv -> System.out.println(cv.getMessage()));
        assertTrue(vRet.isEmpty());
    }
    
    @Test
    public void testAbnormalValidationGenderIsOtherThanMFO() throws ParseException {
        System.out.println("testAbnormalValidationGenderIsOtherThanMFO");
        TEmployee instance = new TEmployee();
        instance.setEmployeeCode("0000000001");
        instance.setName("yamada saburo");
        instance.setGender(null);
        instance.setBirthday(new SimpleDateFormat("yyyyMMdd").parse("19810101"));
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
        assertEquals(message.getMessage(), "性別の入力は必須です。");
    }
}
