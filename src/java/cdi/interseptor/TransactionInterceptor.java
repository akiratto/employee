package cdi.interseptor;

import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;

/**
 *
 * @author Owner
 */
@Interceptor
@TransactionDebugger
public class TransactionInterceptor {
    @AroundInvoke
    public Object runInTransaction(InvocationContext invocationContext) throws Exception
    {
        Object result = null;
        try {
            result = invocationContext.proceed();
        } catch(Exception e) {
            e.printStackTrace();
            throw e;
        }
        return result;
    }
}
