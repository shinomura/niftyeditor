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
public class GHorizontalScrollbar extends GScrollbar{
      static{
        GUIFactory.registerProduct(new GHorizontalScrollbar());
    }
    
    private GHorizontalScrollbar(String id,Element ele){
        super(id,ele,false);
    }

    private GHorizontalScrollbar() {
        super();
    }
    @Override
    public Types getType() {
        return Types.HORIZONTALSCROLLBAR;
    }

    @Override
    public GElement create(String id, Element ele) {
        return new GHorizontalScrollbar(id, ele);
    }

    @Override
    public void initDefault() {
         element.setAttribute("name", ""+Types.HORIZONTALSCROLLBAR);
         element.setAttribute("width", "50%");
    }
    
     @Override
    protected de.lessvoid.nifty.elements.Element getDropContext() {
        throw new IllegalDropException("You can not add elements to a scroolbar");
    }
    
}
