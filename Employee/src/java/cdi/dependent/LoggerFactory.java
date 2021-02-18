package cdi.dependent;

import java.util.logging.Logger;
import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;

/**
 *
 * @author owner
 */
@Dependent
public class LoggerFactory {
    @Produces
    @Dependent
    public Logger getLogger(InjectionPoint ip)
    {
        return Logger.getLogger(ip.getMember().getDeclaringClass().getPackage().getName());
    }
}
