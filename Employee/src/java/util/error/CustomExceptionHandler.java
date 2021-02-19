package util.error;

import java.util.Iterator;
import javax.faces.FacesException;
import javax.faces.application.NavigationHandler;
import javax.faces.context.ExceptionHandler;
import javax.faces.context.ExceptionHandlerWrapper;
import javax.faces.context.FacesContext;
import javax.faces.event.ExceptionQueuedEvent;
import javax.faces.event.ExceptionQueuedEventContext;

/**
 *
 * @author Owner
 */
public class CustomExceptionHandler extends ExceptionHandlerWrapper {
    private final ExceptionHandler wrapped;

    public CustomExceptionHandler(ExceptionHandler wrapped) {
        this.wrapped = wrapped;
    }
    
    
    
    @Override
    public void handle() throws FacesException {
        Iterator i = getUnhandledExceptionQueuedEvents().iterator();
        
        while(i.hasNext()) {
            ExceptionQueuedEvent event = (ExceptionQueuedEvent)i.next();
            ExceptionQueuedEventContext context = (ExceptionQueuedEventContext)event.getSource();
            Throwable t = context.getException();
            FacesContext fc = FacesContext.getCurrentInstance();
            
            System.err.println("jsf.exception.CustomExceptionHandler occured.");
            t.printStackTrace();
            
            try {
                NavigationHandler navHandler = fc.getApplication().getNavigationHandler();
                navHandler.handleNavigation(fc, null, "error/errorException.xhtml?faces-redirect=true");
                fc.renderResponse();
            } finally {
                i.remove();
            }
            getWrapped().handle();
        }
    }
    
    
    @Override
    public javax.faces.context.ExceptionHandler getWrapped() {
        return wrapped;
    }


    
}
