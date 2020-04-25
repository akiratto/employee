package database.dependent;

import application.dependent.PageNavigator;
import database.base.EntityListSearcher;
import database.entity.TableEmployee;
import database.entity.TableEmployee_;
import java.util.List;
import java.util.Optional;
import javax.enterprise.context.Dependent;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import static util.StringFunctions.likeEscape;

/**
 *
 * @author owner
 */
@Dependent
public class EmployeeListSearcher implements EntityListSearcher<TableEmployee> {

    @PersistenceContext
    protected EntityManager em;

    @Override
    public List<TableEmployee> search(TableEmployee condition, PageNavigator pageNavigator) 
    {
        CriteriaBuilder build     = em.getCriteriaBuilder();
        CriteriaQuery<TableEmployee> query = build.createQuery(TableEmployee.class);
        Root<TableEmployee> root  = query.from(TableEmployee.class);
        
        CriteriaQuery<TableEmployee> cq = query.select(root)
                                               .where(generateWherePhrase(condition, build, root))
                                               .orderBy(build.asc(root.get(TableEmployee_.employeeCode)));
        
        return em.createQuery(cq)
                .setFirstResult(pageNavigator.getOffset())
                .setMaxResults(pageNavigator.getRowCountPerPage())
                .getResultList();
    }
    
    @Override
    public Long count(TableEmployee condition)
    {
        CriteriaBuilder build     = em.getCriteriaBuilder();
        CriteriaQuery<Long> query = build.createQuery(Long.class);
        Root<TableEmployee> root  = query.from(TableEmployee.class);
        
        CriteriaQuery<Long> cqCount = query.select(build.count(root))
                                           .where(generateWherePhrase(condition, build, root));
        
        return em.createQuery(cqCount).getSingleResult();
    }
    
    protected Predicate generateWherePhrase(TableEmployee condition, CriteriaBuilder build, Root<TableEmployee> root)
    {
        Predicate noCondition = build.conjunction();

        Predicate employeeCodeCondition = condition.getEmployeeCode() != null && condition.getEmployeeCode().isEmpty()
                                            ? build.equal(root.get(TableEmployee_.employeeCode), condition.getEmployeeCode())
                                            : build.conjunction();

        Predicate nameCondition = condition.getName() != null && condition.getName().isEmpty()
                                    ? build.like(root.get(TableEmployee_.name),"%" + likeEscape(condition.getName()) + "%", '\\')
                                    : build.conjunction();

        Predicate genderCondition = condition.getGender() != null
                                        ? build.equal(root.get(TableEmployee_.gender), condition.getGender())
                                        : build.conjunction();
        
        Predicate birthdayCondition
                = Optional.ofNullable(condition.getBirthday())
                    .map(searchValue -> build.equal(root.get(TableEmployee_.birthday), searchValue))
                    .orElse(noCondition);
        
        Predicate phoneCondition
                = Optional.ofNullable(condition.getPhone())
                        .filter(searchValue -> !searchValue.isEmpty())
                        .map(searchValue -> build.like(root.get(TableEmployee_.phone),"%" + likeEscape(searchValue) + "%", '\\'))
                        .orElse(noCondition);
        
        Predicate mobilePhoneCondition
                = Optional.ofNullable(condition.getMobilePhone())
                        .filter(searchValue -> !searchValue.isEmpty())
                        .map(searchValue -> build.like(root.get(TableEmployee_.mobilePhone),"%" + likeEscape(searchValue) + "%", '\\'))
                        .orElse(noCondition);
        
        Predicate zipCodeCondition
                = Optional.ofNullable(condition.getZipCode())
                        .filter(searchValue -> !searchValue.isEmpty())
                        .map(searchValue -> build.like(root.get(TableEmployee_.zipCode),"%" + likeEscape(searchValue) + "%", '\\'))
                        .orElse(noCondition);
        
        Predicate addressCondition
                = Optional.ofNullable(condition.getAddress())
                        .filter(searchValue -> !searchValue.isEmpty())
                        .map(searchValue -> build.like(root.get(TableEmployee_.address),"%" + likeEscape(searchValue) + "%", '\\'))
                        .orElse(noCondition);
        
        Predicate remarksCondition
                = Optional.ofNullable(condition.getRemarks())
                        .filter(searchValue -> !searchValue.isEmpty())
                        .map(searchValue -> build.like(root.get(TableEmployee_.remarks),"%" + likeEscape(searchValue) + "%", '\\'))
                        .orElse(noCondition);
        
        Predicate where = build.conjunction();
        where = build.and(where, employeeCodeCondition);
        where = build.and(where, nameCondition);
        where = build.and(where, genderCondition);
        where = build.and(where, birthdayCondition);
        where = build.and(where, phoneCondition);
        where = build.and(where, mobilePhoneCondition);
        where = build.and(where, zipCodeCondition);
        where = build.and(where, addressCondition);
        where = build.and(where, remarksCondition);
        
        return where;
    }

  
}
