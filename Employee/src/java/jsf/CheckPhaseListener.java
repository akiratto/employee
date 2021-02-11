package jsf;

import java.util.Optional;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;

/**
 *
 * @author owner
 */
public class CheckPhaseListener implements PhaseListener {

    @Override
    public void beforePhase(PhaseEvent event) {
        String viewId = Optional.of(event)
                                .map(PhaseEvent::getFacesContext)
                                .map(FacesContext::getViewRoot)
                                .map(UIViewRoot::getViewId)
                                .orElse("Not Found");
        
        String phaseId = event.getPhaseId().toString();
        System.out.println(String.format("[%s] before: %s", viewId, phaseId));
    }
    
    @Override
    public void afterPhase(PhaseEvent event) {
        String viewId = Optional.of(event)
                                .map(PhaseEvent::getFacesContext)
                                .map(FacesContext::getViewRoot)
                                .map(UIViewRoot::getViewId)
                                .orElse("Not Found");
        
        String phaseId = event.getPhaseId().toString();
        System.out.println(String.format("[%s] after: %s", viewId, phaseId));
    }


    @Override
    public PhaseId getPhaseId() {
        return PhaseId.ANY_PHASE;
    }
    
    
}
