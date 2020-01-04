package cdi.dependent;

import cdi.base.dependent.EntityListSetting;
import entity.TEmployee;
import javax.enterprise.context.Dependent;

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
public class EmployeeListSetting extends EntityListSetting<TEmployee> { }
