package org.reactome.web.diagram.search.infopanel;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.ui.*;
import org.reactome.web.diagram.data.graph.model.GraphObject;
import org.reactome.web.diagram.data.interactors.model.InteractorSearchResult;
import org.reactome.web.diagram.util.MapSet;
import org.reactome.web.diagram.util.interactors.ResourceNameFormatter;

import java.util.Set;

/**
 * @author Kostas Sidiropoulos <ksidiro@ebi.ac.uk>
 */
public class InteractorInfoPanel extends Composite {

    InteractorSearchResult interactor;
    EventBus eventBus;
    FlowPanel mainPanel;

    public InteractorInfoPanel(EventBus eventBus, InteractorSearchResult interactor) {
        this.eventBus = eventBus;
        this.interactor = interactor;
        this.mainPanel = new FlowPanel();

        GraphObjectInfoPanel.SuggestionPanelCSS css = GraphObjectInfoPanel.OBJECT_INFO_RESOURCES.getCSS();
        this.init(css);

        FlowPanel header = new FlowPanel();
        header.setStyleName(css.infoHeader());
        header.add(new InlineLabel(interactor.getDisplayName()));
        this.add(header);
        if(interactor.getAlias()!=null){
            this.add(new Label("Accession: " + interactor.getAccession()));
        }
        this.add(new Label("Type: Interactor"));
        this.add(new Label("Resource: " + ResourceNameFormatter.format(interactor.getResource())));
        MapSet<String, GraphObject> interactsWith = interactor.getInteractsWith();
        for(String interactionId:interactsWith.keySet()) {
            Set<GraphObject> interactors = interactsWith.getElements(interactionId);
            if (!interactors.isEmpty()) {
                Double score = interactor.getInteractionScore(interactionId);
                String title = "Interacts with: ";
                if(interactionId == null || interactionId.matches("^\\-\\d+")) {
                    title = title + "(Score: " + (score!=null ? NumberFormat.getFormat("0.000").format(score): "-") + ")";
                } else {
                    title = title + "[" + interactionId + "] (Score: " + (score!=null ? NumberFormat.getFormat("0.000").format(score): "-") + ")";
                }
                this.add(new DatabaseObjectListPanel(title, interactors, eventBus));
            }
        }
    }
    /**
     * Padding grows wider the div but this is not the expected behaviour here. To make it compatible
     * across all browsers the recommendation is having two divs
     * <div> // defines the object info panel properties
     *     <div> // defines the object info mainPanel (set the padding here)
     *         Info mainPanel
     *     </div>
     * </div>
     */
    private void init(GraphObjectInfoPanel.SuggestionPanelCSS css){
        SimplePanel sp = new SimplePanel();
        sp.setStyleName(css.objectInfoPanel());
        this.mainPanel.setStyleName(css.objectInfoContent());
        sp.add(this.mainPanel);
        initWidget(sp);
    }

    public void add(IsWidget widget){
        this.mainPanel.add(widget);
    }
}
