/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jada.ngeditor.model.elements;

import de.lessvoid.nifty.controls.imageselect.builder.ImageSelectBuilder;
import jada.ngeditor.model.GUIFactory;
import jada.ngeditor.model.Types;
import jada.ngeditor.model.exception.IllegalDropException;
import org.w3c.dom.Element;

/**
 *
 * @author cris
 */
public class GImageSelect extends GElement{
    static{
         GUIFactory.registerProduct(new GImageSelect());
    }
    public GImageSelect() {
        super();
    }

    public GImageSelect(String id, Element docElement) throws IllegalArgumentException {
        super(id, docElement);
        this.builder = new ImageSelectBuilder(id);
    }

    @Override
    public Types getType() {
        return Types.IMAGESELECT;
    }

    @Override
    public GElement create(String id, Element ele) {
        return new GImageSelect(id,ele);
    }

    @Override
    public void initDefault() {
       element.setAttribute("name", Types.IMAGESELECT.toString());
      element.setAttribute("width", "100px");
      element.setAttribute("height", "100px");
    }
    
     @Override
    protected de.lessvoid.nifty.elements.Element getDropContext() {
        throw new IllegalDropException("You can not add elements to a imageselect");
    }
    
    
    
}
