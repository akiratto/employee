package application.dependent;

import presentation.jsf.base.JsfEntityListTextResources;
import javax.enterprise.context.Dependent;
import presentation.jsf.entity.JsfEmployee;

/**
 * デプロイ時にCDIがEntityListSetting<TEmployee>を注入可能にするため定義
 * 
 * 本クラスを定義しないと注入時にクラスの検索に失敗し、以下のエラーが発生する
 * org.jboss.weld.exceptions.DeploymentException: WELD-001408: 
 * Unsatisfied dependencies for type EntityListSetting<TEmployee> with qualifiers @Default
 * 
 * @author Owner
 */
@Dependent
public class EmployeeListSetting extends JsfEntityListTextResources<JsfEmployee> 
{ 
    @Override
    public Class<JsfEmployee> entityClazz() {
        return JsfEmployee.class;
    }
    
}
