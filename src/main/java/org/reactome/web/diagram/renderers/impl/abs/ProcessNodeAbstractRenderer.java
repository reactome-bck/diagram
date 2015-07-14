package org.reactome.web.diagram.renderers.impl.abs;

import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.canvas.dom.client.TextMetrics;
import org.reactome.web.diagram.data.layout.Coordinate;
import org.reactome.web.diagram.data.layout.DiagramObject;
import org.reactome.web.diagram.data.layout.Node;
import org.reactome.web.diagram.data.layout.NodeProperties;
import org.reactome.web.diagram.data.layout.impl.NodePropertiesFactory;
import org.reactome.web.diagram.profiles.diagram.DiagramColours;
import org.reactome.web.diagram.renderers.common.ColourProfileType;
import org.reactome.web.diagram.renderers.common.OverlayContext;
import org.reactome.web.diagram.renderers.common.RendererProperties;
import org.reactome.web.diagram.util.AdvancedContext2d;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public abstract class ProcessNodeAbstractRenderer extends NodeAbstractRenderer {

    @Override
    public void drawEnrichment(AdvancedContext2d ctx, OverlayContext overlay, DiagramObject item, Double factor, Coordinate offset) {
        drawAnalysisResult(ctx, item, factor, offset);
    }

    @Override
    public void drawExpression(AdvancedContext2d ctx, OverlayContext overlay, DiagramObject item, int t, double min, double max, Double factor, Coordinate offset){
        drawAnalysisResult(ctx, item, factor, offset);
    }

    public abstract void drawAnalysisResult(AdvancedContext2d ctx, DiagramObject item, Double factor, Coordinate offset);

    @Override
    public void draw(AdvancedContext2d ctx, DiagramObject item, Double factor, Coordinate offset) {
        if(!isVisible(item)) return;

        Node node = (Node) item;
        NodeProperties prop = NodePropertiesFactory.transform(node.getProp(), factor, offset);
        ctx.save();
        ctx.setStrokeStyle(ctx.getFillStyle());
        ctx.setLineWidth(RendererProperties.PROCESS_NODE_INSET_WIDTH);
        ctx.setFillStyle("#FEFDFF");
        shape(ctx, prop, node.getNeedDashedBorder());
        ctx.fill();
        ctx.stroke();
        ctx.restore();
    }

    @Override
    public void drawText(AdvancedContext2d ctx, DiagramObject item, Double factor, Coordinate offset){
        if(item.getDisplayName() == null || item.getDisplayName().isEmpty()) { return; }
        TextMetrics metrics = ctx.measureText(item.getDisplayName());

        Node node = (Node) item;
        Coordinate textPos = node.getPosition().transform(factor, offset);
        NodeProperties prop = NodePropertiesFactory.transform(node.getProp(), factor, offset);
        double padding = RendererProperties.NODE_TEXT_PADDING * 3;
        TextRenderer textRenderer = new TextRenderer(RendererProperties.WIDGET_FONT_SIZE, padding);
        if(metrics.getWidth() <= prop.getWidth() - 2 * padding ) {
            textRenderer.drawTextSingleLine(ctx, item.getDisplayName(), textPos);
        }else{
            textRenderer.drawTextMultiLine(ctx, item, factor, offset);
        }
    }

    @Override
    public boolean isVisible(DiagramObject item) {
        return true;
    }

    @Override
    public void setColourProperties(AdvancedContext2d ctx, ColourProfileType type) {
        type.setColourProfile(ctx, DiagramColours.get().PROFILE.getProcessnode());
    }

    @Override
    public void setTextProperties(AdvancedContext2d ctx, ColourProfileType type){
        ctx.setTextAlign(Context2d.TextAlign.CENTER);
        ctx.setTextBaseline(Context2d.TextBaseline.MIDDLE);
        ctx.setFont(RendererProperties.getFont(RendererProperties.WIDGET_FONT_SIZE));
        type.setTextProfile(ctx, DiagramColours.get().PROFILE.getProcessnode());
    }
}
