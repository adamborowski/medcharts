/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.adamborowski.utils.geom;

import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import java.awt.geom.Path2D;

/**
 *
 * @author test
 */
public class ShapeFactory
{

    public static ShapeProxy createCloudLabel(int width, int height, int radius, int arrowWidth, int arrowHeight)
    {
        final int wdiv2 = width / 2;
        final int arrowWdiv2 = arrowWidth / 2;
        GeneralPath gp = new GeneralPath();

        /*
         *     ________B/\C_______
         *   A/                   \D 
         *   |                     |
         *   |                     |
         *   |                     |
         *  F\_____________________/E
         */
        gp.moveTo(0, radius + arrowHeight);
        gp.quadTo(0, arrowHeight, radius, arrowHeight);             //A
        gp.lineTo(wdiv2 - arrowWdiv2, arrowHeight);                   //B
        gp.lineTo(wdiv2, 0);                                        //B-C
        gp.lineTo(wdiv2 + arrowWdiv2, arrowHeight);                   //C
        gp.lineTo(width - radius, arrowHeight);                       //D
        gp.quadTo(width, arrowHeight, width, arrowHeight + radius);   //D
        gp.lineTo(width, height - radius);                            //E
        gp.quadTo(width, height, width - radius, height);             //E
        gp.lineTo(radius, height);                                   //F
        gp.quadTo(0, height, 0, height - radius);                     //F
        gp.closePath();
        return new ShapeProxy(gp);
    }

    public final static class ShapeProxy
    {

        public final Path2D shape;

        public ShapeProxy(Path2D shape)
        {
            this.shape = shape;
        }
        private AffineTransform af = new AffineTransform();
        private int curX;
        private int curY;

        public void setLocation(int x, int y)
        {

            af.setToTranslation(x - curX, y - curY);
            shape.transform(af);
            curX = x;
            curY = y;
        }
    }
}
