/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cdi;

import cdi.Employee.Mode;
import entity.TEmployee;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author owner
 */
@Named
@RequestScoped
public class EmployeeList {
    @PersistenceContext
    private EntityManager em;
    
//    @Inject
//    Employee employee;
    
    public List<TEmployee> extract()
    {
        return em.createQuery("SELECT t FROM TEmployee t").getResultList();
                
    }
    
    public String gotoDetail(String employeeId, String mode) throws UnsupportedEncodingException
    {
        TEmployee tEmployee = em.find(TEmployee.class, employeeId);
        if(tEmployee == null) {
            return "";
        }
        
        return "employeeDetail?faces-redirect=true&employee_id=" + URLEncoder.encode(employeeId, "UTF-8") + "&mode=" + URLEncoder.encode(mode, "UTF-8");
        
        /*
        FlashオブジェクトでTEmployeeをまるまる遷移先に渡す。
        ⇒遷移先画面でブラウザの更新ボタンを押すと、Flashオブジェクトから
          設定が消え、入力項目が空欄になる。
          ViewScopedはブラウザの更新を行ってもViewIdが変わらなければ
          CDIビーンは保持されると想定していたが、
          実際には他画面の遷移時と同様、CDIビーンは破棄されてしまう。
        
        FacesContext.getCurrentInstance()
                    .getExternalContext()
                    .getFlash()
                    .put("employee", tEmployee);
        return "employeeDetail?faces-redirect=true";
        */
        /*
            下記だとリダイレクトでHTTPリクエストが2回発生するので
            設定した値が初期化されてしまう。
        
        employee.setAddress(tEmployee.getAddress());
        employee.setEmployeeId(tEmployee.getEmployee_id());
        employee.setGender(tEmployee.getGender());
        employee.setMobilePhone(tEmployee.getMobilePhone());
        employee.setName(tEmployee.getName());
        employee.setPhone(tEmployee.getPhone());
        employee.setRemarks(tEmployee.getRemarks());
        employee.setZipCode(tEmployee.getZipCode());
        return "employeeDetail?faces-redirect=true";  //return "employeeDetail";
        */
    }
}
