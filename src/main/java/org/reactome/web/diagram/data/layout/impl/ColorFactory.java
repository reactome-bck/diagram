package org.reactome.web.diagram.data.layout.impl;

import org.reactome.web.diagram.data.layout.Color;

/**
 * @author Kostas Sidiropoulos <ksidiro@ebi.ac.uk>
 */
public class ColorFactory implements Color{

    private Integer r;
    private Integer g;
    private Integer b;

    private ColorFactory(Integer r, Integer g, Integer b){
        this.r = r;
        this.g = g;
        this.b = b;
    }

    public static Color get(Integer r, Integer g, Integer b){
        return new ColorFactory(r, g, b);
    }

    public static Color get(String colorInHex){
        return new ColorFactory(
                Integer.valueOf( colorInHex.substring( 1, 3 ), 16 ),
                Integer.valueOf( colorInHex.substring( 3, 5 ), 16 ),
                Integer.valueOf( colorInHex.substring( 5, 7 ), 16 )
        );

    }

    @Override
    public Integer getR() {
        return r;
    }

    @Override
    public Integer getG() {
        return g;
    }

    @Override
    public Integer getB() {
        return b;
    }

    public static Color lighter(Color input, Double boost){
        // Naive approach from: http://stackoverflow.com/questions/141855/programmatically-lighten-a-color
        Integer r = Math.min ( (int)(input.getR() * boost), 255);
        Integer g = Math.min ( (int)(input.getG() * boost), 255);
        Integer b = Math.min ( (int)(input.getB() * boost), 255);

        return new ColorFactory(r, g, b);
    }
}
