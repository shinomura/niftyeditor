/* Copyright 2012 Aguzzi Cristiano

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 */
package jada.ngeditor.model.elements;

import de.lessvoid.nifty.builder.PanelBuilder;
import jada.ngeditor.model.GUIFactory;
import jada.ngeditor.model.Types;

/**
 *
 * @author cris
 */
public class GPanel extends GElement{
    static{
         GUIFactory.registerProduct(new GPanel());
    }
  
    
    private GPanel(){
        super();
    }
    public GPanel(String id,org.w3c.dom.Element docElement){
      super(id,docElement);
      if(!docElement.getTagName().equals("panel"))
          throw new IllegalArgumentException("Illegal tag name");
      builder = new PanelBuilder(id);
     
    }
    
    @Override
    public Types getType() {
        return Types.PANEL;
    }

    

    

    @Override
    public void initDefault() {
      element.setAttribute("width", "50%");
      element.setAttribute("height", "30%");
      element.setAttribute("childLayout", "absolute");
      element.setAttribute("style", "nifty-panel-simple");
    }

    @Override
    public GElement create(String id,org.w3c.dom.Element ele) {
        return new GPanel(id,ele);
    }
    
}
