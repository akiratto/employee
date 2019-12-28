/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cdi;

import entity.TEmployee;
import entity.TEmployee_;
import java.io.Serializable;
import java.util.List;
import java.util.function.Consumer;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import jsf.ui.annotation.JsfUIModel;

/**
 *
 * @author owner
 */
public class EntityCRUD<E extends Serializable, PK> {
    @PersistenceContext
    private EntityManager em;
    
    public void create(Consumer<E> creater, Class<E> entityClazz) throws InstantiationException, IllegalAccessException {
        E newEntity = entityClazz.newInstance();
        creater.accept(newEntity);
        em.persist(newEntity);
        em.flush();
        em.clear();
    }

    public E find(PK entityId, Class<E> entityClazz) {
        E foundEntity = em.find(entityClazz, entityId);
        em.clear();
        return foundEntity;
    }

    public int update(Integer entityId, Consumer<E> updater, Class<E> entityClazz) {
        E foundEntity = em.find(entityClazz, entityId);
        if(foundEntity==null) {
            em.clear();
            return 0;
        }
        updater.accept(foundEntity);    //管理状態のエンティティをupdater内でsetterによる値設定を行えば, UPDATE文が発行される
        em.flush();
        em.clear();
        return 1;
    }

    public int delete(PK entityId, Class<E> entityClazz) {
        E entity = em.find(entityClazz, entityId);
        if(entity==null) {
            em.clear();
            return 0;
        }
        em.remove(entity);
        em.flush();
        em.clear();
        return 1;
    }

    public int deleteAll(Class<E> entityClazz) {
        JsfUIModel jsfUIModel = entityClazz.getDeclaredAnnotation(JsfUIModel.class);
        String entityName = jsfUIModel.modelName();
        int deleteCount = em.createQuery("DELETE FROM " + entityName).executeUpdate();
        return deleteCount;
    }

    public Long countAll(E condition,  Class<E> entityClazz) {
        CriteriaBuilder build = em.getCriteriaBuilder();
        CriteriaQuery<Long> cqCount = build.createQuery(Long.class);
        Root<E> rootCount = cqCount.from(entityClazz);
        Predicate where = constructWhere(condition,build,rootCount);
        cqCount = cqCount.select(build.count(rootCount)).where(where);
        return em.createQuery(cqCount).getSingleResult();        
    }

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
    
    private Predicate constructWhere(E condition, CriteriaBuilder build, Root<E> root)
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
