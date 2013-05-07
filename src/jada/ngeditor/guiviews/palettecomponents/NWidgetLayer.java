/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jada.ngeditor.guiviews.palettecomponents;

import jada.ngeditor.guiviews.DND.WidgetData;
import jada.ngeditor.model.GUI;
import jada.ngeditor.model.GUIFactory;
import jada.ngeditor.model.Types;
import jada.ngeditor.model.elements.GElement;
import jada.ngeditor.model.elements.GLayer;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

/**
 *
 * @author cris
 */
public class NWidgetLayer extends NWidget {
    
    public NWidgetLayer() throws IOException{
         BufferedImage image = ImageIO.read(getIcon("layer.png"));
         this.icon.setIcon(new ImageIcon(image));
         this.text.setText("Layer");
    }

    @Override
    public WidgetData getData() {
       GElement e = GUIFactory.getInstance().newGElement(Types.LAYER.toString());
        return new WidgetData(e);
    }
    
}
