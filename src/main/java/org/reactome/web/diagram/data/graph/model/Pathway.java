package org.reactome.web.diagram.data.graph.model;

import com.google.gwt.resources.client.ImageResource;
import org.reactome.web.diagram.data.graph.model.images.DatabaseObjectImages;
import org.reactome.web.diagram.data.graph.raw.EntityNode;

import java.util.List;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class Pathway extends Event {

    private Double percentage;

    public Pathway(EntityNode node) {
        super(node);
    }

    public Double getPercentage() {
        return percentage;
    }

    public boolean isHit() {
        return percentage!=null && percentage>0.0;
    }

    public void setIsHit(Double percentage, List<Double> expression){
        this.percentage = percentage;
        this.expression = expression;
    }

    public void resetHit(){
        this.percentage = null;
        this.expression = null;
    }

    @Override
    public ImageResource getImageResource() {
        return DatabaseObjectImages.INSTANCE.pathway();
    }
}
