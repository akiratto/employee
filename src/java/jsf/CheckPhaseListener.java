package jsf;

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
        System.out.println("before:" + event.getPhaseId());
    }
    
    @Override
    public void afterPhase(PhaseEvent event) {
        System.out.println("after:" + event.getPhaseId());
    }


    @Override
    public PhaseId getPhaseId() {
        return PhaseId.ANY_PHASE;
    }
    
    
}
