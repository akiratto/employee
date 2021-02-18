package common.interseptor;

import java.io.Serializable;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;

/**
 *
 * @author Owner
 */
@Interceptor
@TransactionDebugger
public class TransactionInterceptor implements Serializable {
    @AroundInvoke
    public Object runInTransaction(InvocationContext invocationContext) throws Exception
    {
        Object result = null;
        try {
            result = invocationContext.proceed();
        } catch(Exception e) {
            System.out.println("@Transaction Debugger catch. -------------------------------------------------------");
            e.printStackTrace();
            throw e;
        }
        return result;
    }
}
