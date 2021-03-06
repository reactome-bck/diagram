package org.reactome.web.diagram.handlers;

import com.google.gwt.event.shared.EventHandler;
import org.reactome.web.diagram.events.AnalysisResultErrorEvent;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public interface AnalysisResultErrorHandler extends EventHandler {
    void onAnalysisResultError(AnalysisResultErrorEvent event);
}
