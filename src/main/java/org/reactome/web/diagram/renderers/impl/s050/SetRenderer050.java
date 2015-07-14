package org.reactome.web.diagram.renderers.impl.s050;

import com.google.gwt.canvas.dom.client.Context2d;
import org.reactome.web.diagram.data.graph.model.EntitySet;
import org.reactome.web.diagram.data.layout.Coordinate;
import org.reactome.web.diagram.data.layout.DiagramObject;
import org.reactome.web.diagram.data.layout.Node;
import org.reactome.web.diagram.data.layout.NodeProperties;
import org.reactome.web.diagram.data.layout.impl.NodePropertiesFactory;
import org.reactome.web.diagram.profiles.analysis.AnalysisColours;
import org.reactome.web.diagram.profiles.diagram.DiagramColours;
import org.reactome.web.diagram.renderers.common.ColourProfileType;
import org.reactome.web.diagram.renderers.common.OverlayContext;
import org.reactome.web.diagram.renderers.common.RendererProperties;
import org.reactome.web.diagram.renderers.impl.abs.SetAbstractRenderer;
import org.reactome.web.diagram.util.AdvancedContext2d;
import org.reactome.web.diagram.util.ExpressionUtil;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class SetRenderer050 extends SetAbstractRenderer {
    @Override
    public Double getExpressionHovered(DiagramObject item, Coordinate pos, int t) {
        EntitySet set = item.getDatabaseObject();
        NodeProperties prop = ((Node) item).getProp();

        List<Double> expression = new LinkedList<Double>(set.getParticipantsExpression(t).values());
        if(expression.isEmpty()) return null;

        Collections.sort(expression);       //Collections.sort(expression, Collections.reverseOrder());
        double value = ExpressionUtil.median(expression);
        double percentage = set.getHitParticipants().size() / (double) set.getParticipants().size();

        double minX = prop.getX();
        double maxX = minX + prop.getWidth() * percentage;
        if(pos.getX()>minX && pos.getX()<=maxX) return value;
        return null;
    }

    @Override
    public void drawEnrichment(AdvancedContext2d ctx, OverlayContext overlay, DiagramObject item, Double factor, Coordinate offset){
        EntitySet set = item.getDatabaseObject();
        double percentage = set.getHitParticipants().size() / (double) set.getParticipants().size();

        ctx.save();
        setColourProperties(ctx, ColourProfileType.ANALYSIS); //IMPORTANT
        if(item.getIsDisease()!=null) ctx.setStrokeStyle(DiagramColours.get().PROFILE.getProperties().getDisease());
        super.draw(ctx, item, factor, offset);
        ctx.restore();

        Node node = (Node) item;
        NodeProperties prop = NodePropertiesFactory.transform(node.getProp(), factor, offset);

        AdvancedContext2d buffer = overlay.getBuffer();
        buffer.save();
        buffer.setLineWidth(ctx.getLineWidth());
        buffer.setFillStyle(ctx.getFillStyle());
        buffer.fillRect(prop.getX(), prop.getY(), prop.getWidth() * percentage, prop.getHeight());

        buffer.setGlobalCompositeOperation(Context2d.Composite.SOURCE_ATOP);
        buffer.setStrokeStyle(ctx.getStrokeStyle());
        innerShape(buffer, prop, node.getNeedDashedBorder());
        buffer.stroke();

        buffer.setGlobalCompositeOperation(Context2d.Composite.DESTINATION_IN);
        buffer.roundedRectangle(prop.getX(), prop.getY(), prop.getWidth(), prop.getHeight(), RendererProperties.ROUND_RECT_ARC_WIDTH);
        buffer.fill();

        ctx.drawImage(buffer.getCanvas(), 0, 0); //TODO: Improve this to copy only the region
        buffer.restore();
    }

    @Override
    public void drawExpression(AdvancedContext2d ctx, OverlayContext overlay, DiagramObject item, int t, double min, double max, Double factor, Coordinate offset) {
        EntitySet set = item.getDatabaseObject();
        double percentage = set.getHitParticipants().size() / (double) set.getParticipants().size();

        List<Double> expression = new LinkedList<Double>(set.getParticipantsExpression(t).values());
        Collections.sort(expression);       //Collections.sort(expression, Collections.reverseOrder());
        double value = ExpressionUtil.median(expression);

        Node node = (Node) item;
        NodeProperties prop = NodePropertiesFactory.transform(node.getProp(), factor, offset);

        ctx.save();
        setColourProperties(ctx, ColourProfileType.ANALYSIS);
        if(item.getIsDisease()!=null) ctx.setStrokeStyle(DiagramColours.get().PROFILE.getProperties().getDisease());
        super.draw(ctx, item, factor, offset);
        ctx.restore();

        AdvancedContext2d buffer = overlay.getBuffer();
        buffer.save();
        buffer.setFillStyle(AnalysisColours.get().expressionGradient.getColor(value, min, max));
        buffer.fillRect(prop.getX(), prop.getY(), prop.getWidth() * percentage, prop.getHeight());

        buffer.setGlobalCompositeOperation(Context2d.Composite.SOURCE_ATOP);
        buffer.setStrokeStyle(ctx.getStrokeStyle());
        innerShape(buffer, prop, node.getNeedDashedBorder());
        buffer.stroke();

        buffer.setGlobalCompositeOperation(Context2d.Composite.DESTINATION_IN);
        buffer.roundedRectangle(prop.getX(), prop.getY(), prop.getWidth(), prop.getHeight(), RendererProperties.ROUND_RECT_ARC_WIDTH);
        buffer.fill();

        buffer.setGlobalCompositeOperation(Context2d.Composite.SOURCE_ATOP);
        setTextProperties(buffer, ColourProfileType.ANALYSIS);
        buffer.setShadowColor("#000000");
        buffer.setShadowBlur(5.0);
        buffer.setFillStyle(AnalysisColours.get().PROFILE.getExpression().getText());
        drawText(buffer, item, factor, offset);

        overlay.getOverlay().drawImage(buffer.getCanvas(), 0, 0); //TODO: Improve this to copy only the region
        buffer.restore();
    }
}
