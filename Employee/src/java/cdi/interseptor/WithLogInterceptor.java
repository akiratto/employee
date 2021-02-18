package cdi.interseptor;

import java.util.logging.Logger;
import javax.annotation.Priority;
import javax.annotation.Resource;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.interceptor.Interceptor;

/**
 *
 * @author owner
 */
@Interceptor
@Dependent
@WithLog
@Priority(Interceptor.Priority.APPLICATION)
public class WithLogInterceptor {
    @Inject
    private Logger logger;
    
    @Resource(lookup="java:app/AppName")
    String appName;
}
