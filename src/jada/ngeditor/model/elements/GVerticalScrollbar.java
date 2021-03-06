/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jada.ngeditor.model.elements;

import jada.ngeditor.model.GUIFactory;
import jada.ngeditor.model.Types;
import jada.ngeditor.model.exception.IllegalDropException;
import org.w3c.dom.Element;

/**
 *
 * @author cris
 */
public class GVerticalScrollbar extends GScrollbar{
    static{
         GUIFactory.registerProduct(new GVerticalScrollbar());
    }
    
    public GVerticalScrollbar(){
        super();
    }
    
    public GVerticalScrollbar(String id, Element docElement) throws IllegalArgumentException {
        super(id, docElement, true);
    }
    @Override
    public Types getType() {
        return Types.VERTICALSCROLLBAR;
    }

    @Override
    public GElement create(String id, Element ele) {
        return new GVerticalScrollbar(id,ele);
    }

    @Override
    public void initDefault() {
        element.setAttribute("name", ""+Types.VERTICALSCROLLBAR);
        element.setAttribute("height", "50%");
    }
    
     @Override
    protected de.lessvoid.nifty.elements.Element getDropContext() {
        throw new IllegalDropException("You can not add elements to a scrollbar");
    }
    
}
