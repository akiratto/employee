package cdi;

import cdi.base.EntityDbAction;
import entity.TEmployee;
import entity.TEmployee_;
import java.io.Serializable;
import java.util.List;
import java.util.function.Consumer;
import javax.faces.view.ViewScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

/**
 *
 * @author Owner
 */
@ViewScoped
public class EmployeeDbAction implements EntityDbAction<TEmployee, Integer>, Serializable {
    @PersistenceContext
    private EntityManager em;
    
    @Override
    public void create(Consumer<TEmployee> creater) {
        TEmployee newEntity = new TEmployee();
        creater.accept(newEntity);
        em.persist(newEntity);
        em.flush();
        em.clear();
    }

    @Override
    public TEmployee find(Integer entityId) {
        TEmployee foundEntity = em.find(TEmployee.class, entityId);
        em.clear();
        return foundEntity;
    }

    @Override
    public int update(Integer entityId, Consumer<TEmployee> updater) {
        TEmployee foundEntity = em.find(TEmployee.class, entityId);
        if(foundEntity==null) {
            em.clear();
            return 0;
        }
        updater.accept(foundEntity);    //管理状態のエンティティをupdater内でsetterによる値設定を行えば, UPDATE文が発行される
        em.flush();
        em.clear();
        return 1;
    }

    @Override
    public int delete(Integer entityId) {
        TEmployee entity = em.find(TEmployee.class, entityId);
        if(entity==null) {
            em.clear();
            return 0;
        }
        em.remove(entity);
        em.flush();
        em.clear();
        return 1;
    }

    @Override
    public int deleteAll() {
        int deleteCount = em.createQuery("DELETE FROM TEmployee").executeUpdate();
        return deleteCount;
    }

    @Override
    public Long countAll(TEmployee condition) {
        CriteriaBuilder build = em.getCriteriaBuilder();
        CriteriaQuery<Long> cqCount = build.createQuery(Long.class);
        Root<TEmployee> rootCount = cqCount.from(TEmployee.class);
        Predicate where = constructWhere(condition,build,rootCount);
        cqCount = cqCount.select(build.count(rootCount)).where(where);
        return em.createQuery(cqCount).getSingleResult();        
    }

    @Override
    public List<TEmployee> search(TEmployee condition, int offset, int rowCountPerPage) {
        CriteriaBuilder build = em.getCriteriaBuilder();
        CriteriaQuery<TEmployee> cq = build.createQuery(TEmployee.class);
        Root<TEmployee> root = cq.from(TEmployee.class);
        Predicate where = constructWhere(condition, build, root);
        cq = cq.select(root)
                .where(where)
                .orderBy(build.asc(root.get(TEmployee_.employeeCode)));
        return em.createQuery(cq)
                    .setFirstResult(offset)
                    .setMaxResults(rowCountPerPage)
                    .getResultList();
    }
    
    private Predicate constructWhere(TEmployee condition, CriteriaBuilder build, Root<TEmployee> root)
    {
        Predicate where = build.conjunction();
        if(condition.getEmployeeCode()!=null) {
            where = build.and(where, build.equal(
                                        root.get(TEmployee_.employeeCode), 
                                        condition.getEmployeeCode()
            ));
        }
        if(condition.getName()!=null && !condition.getName().isEmpty()) {
            where = build.and(where, build.like(
                                        root.get(TEmployee_.name), 
                                        "%" + likeEscape(condition.getName()) + "%", '\\'
            ));
        }
        if(condition.getGender()!=null) {
            where = build.and(where, build.equal(
                                        root.get(TEmployee_.gender), 
                                        condition.getGender()
            ));
        }
        if(condition.getBirthday()!=null) {
            where = build.and(where, build.equal(root.get(TEmployee_.birthday), condition.getBirthday()));
        }
        if(condition.getPhone()!=null && !condition.getPhone().isEmpty()) {
            where = build.and(where, build.like(
                                        root.get(TEmployee_.phone), 
                                        "%" + likeEscape(condition.getPhone()) + "%", '\\'
            ));
        }
        if(condition.getMobilePhone()!=null && !condition.getMobilePhone().isEmpty()) {
            where = build.and(where, build.like(
                                        root.get(TEmployee_.mobilePhone), 
                                        "%" + likeEscape(condition.getMobilePhone()) + "%", '\\'
            ));
        }
        if(condition.getZipCode()!=null && !condition.getZipCode().isEmpty()) {
            where = build.and(where, build.like(
                                        root.get(TEmployee_.zipCode), 
                                        "%" + likeEscape(condition.getZipCode()) + "%", '\\'
            ));
        }
        if(condition.getAddress()!=null && !condition.getAddress().isEmpty()) {
            where = build.and(where, build.like(
                                        root.get(TEmployee_.address), 
                                        "%" + likeEscape(condition.getAddress()) + "%", '\\'
            ));
        }
        if(condition.getRemarks()!=null && !condition.getRemarks().isEmpty()) {
            where = build.and(where, build.like(
                                        root.get(TEmployee_.remarks), 
                                        "%" + likeEscape(condition.getRemarks()) + "%", '\\'
            ));
        }     
        return where;
    }

    private String likeEscape(String likeCondition)
    {
        return likeCondition
                .replaceAll("_", "\\\\_")
                .replaceAll("%", "\\\\%");
    }
}
