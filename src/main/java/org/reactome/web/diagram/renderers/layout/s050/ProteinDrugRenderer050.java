package org.reactome.web.diagram.renderers.layout.s050;

import org.reactome.web.diagram.data.layout.Coordinate;
import org.reactome.web.diagram.data.layout.DiagramObject;
import org.reactome.web.diagram.data.layout.Node;
import org.reactome.web.diagram.data.layout.SummaryItem;
import org.reactome.web.diagram.data.layout.category.ShapeCategory;
import org.reactome.web.diagram.renderers.common.HoveredItem;
import org.reactome.web.diagram.renderers.layout.abs.ProteinDrugAbstractRenderer;
import org.reactome.web.diagram.util.AdvancedContext2d;

/**
 * @author Kostas Sidiropoulos <ksidiro@ebi.ac.uk>
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class ProteinDrugRenderer050 extends ProteinDrugAbstractRenderer {

    @Override
    @SuppressWarnings("Duplicates")
    public void draw(AdvancedContext2d ctx, DiagramObject item, Double factor, Coordinate offset) {
        super.draw(ctx, item, factor, offset);
        ctx.save();
        ctx.setGlobalAlpha((factor - 0.5) * 2);
        drawSummaryItems(ctx, (Node) item, factor, offset);
        ctx.restore();
    }

    @Override
    public void highlight(AdvancedContext2d ctx, DiagramObject item, Double factor, Coordinate offset) {
        super.highlight(ctx, item, factor, offset);
        drawSummaryItems(ctx, (Node) item, factor, offset);
    }

    @Override
    @SuppressWarnings("Duplicates")
    public HoveredItem getHovered(DiagramObject item, Coordinate pos) {
        Node node = (Node) item;

        SummaryItem interactorsSummary = node.getInteractorsSummary();
        if (interactorsSummary != null) {
            if (ShapeCategory.isHovered(interactorsSummary.getShape(), pos)) {
                return new HoveredItem(node.getId(), interactorsSummary);
            }
        }
        return super.getHovered(item, pos);
    }
}
