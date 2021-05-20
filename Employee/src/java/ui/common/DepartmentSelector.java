package ui.common;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import type.TDepartment;

/**
 *
 * @author owner
 */
@Named
@ViewScoped
public class DepartmentSelector implements Serializable {
    @PersistenceContext
    private EntityManager em;
    
    private List<TDepartment> departments = new ArrayList<>();
    private TDepartment selectDepartment;
    
    public void call()
    {
        departments = em.createNamedQuery("TDepartment.findAll", TDepartment.class).getResultList();
    }
    
    public List<TDepartment>  getDepartments()
    {
        return departments;
    }

    public TDepartment getSelectDepartment() {
        return selectDepartment;
    }
    
    public void select(TDepartment department)
    {
        selectDepartment = departments.stream().filter(dep -> dep == department).findFirst().orElse(null);
    }
}
