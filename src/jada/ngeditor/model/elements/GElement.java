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



import de.lessvoid.nifty.EndNotify;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.builder.ElementBuilder;
import de.lessvoid.nifty.controls.AbstractController;
import de.lessvoid.nifty.controls.Parameters;
import de.lessvoid.nifty.controls.dynamic.attributes.ControlAttributes;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.elements.render.ImageRenderer;
import de.lessvoid.nifty.loaderv2.types.ElementType;
import de.lessvoid.nifty.loaderv2.types.StyleType;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.tools.SizeValue;
import de.lessvoid.xml.xpp3.Attributes;
import jada.ngeditor.model.Types;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import org.w3c.dom.Node;

/**
 *
 * @author cris
 */
public abstract class GElement {
    
    static private int UID = 0;
    private LinkedList<GElement> children;
    private int UniID;
    protected GElement parent;
    protected  String id;
    protected final org.w3c.dom.Element element;
    protected Element nElement;
    protected ElementBuilder builder;
    private String oldStyle;
    private ArrayList<String> toBeRemoved = new ArrayList<String>();
    
    protected GElement(){
        element=null;
    }
    
    public GElement(org.w3c.dom.Element docElement){
       if(docElement != null){
       if(docElement.hasAttribute("id"))
            this.id      = docElement.getAttribute("id");
       else 
           id = "";
       
       this.element = docElement;
       this.parent = null;
       
       this.children = new LinkedList<GElement>();
       this.UniID=UID;
       UID++;
       } else 
           throw new IllegalArgumentException("Element null");
    }
    
    protected GElement(String id, org.w3c.dom.Element docElement) throws IllegalArgumentException {
       if(docElement == null){
           throw new IllegalArgumentException("Element null");
       }
       if(this.getType().isControl() && !docElement.getTagName().equals("control")){
           throw new IllegalArgumentException("Illegal tag name");
       }
       if(!this.getType().isControl() && !docElement.getTagName().equals(this.getType().toString())){
          throw new IllegalArgumentException("Illegal tag name");
       }
       this.id = id;
       this.element = docElement;
       this.parent = null;
       
       this.children = new LinkedList<GElement>();
       this.UniID=UID;
       UID++; 
       this.element.setAttribute("id", id);
       
            
       
    }
    
    public int getUniID(){
        
        return this.UniID;
    }
    public void setIndex(int index){
        nElement.setIndex(index);
        if((index+1)<children.size()){
            GElement after = children.get(index+1);
            parent.element.removeChild(this.element);
            parent.element.insertBefore(element,after.element);
        }else{
           GElement after = parent.children.get(index);
           parent.element.insertBefore(element,after.element);
        }  
    }
    public void removeFromParent(){
        if(parent != null){
            this.parent.element.removeChild(this.element);
            this.parent.children.remove(this);
            this.parent= null;
        }
    }
    public void addChild(GElement toAdd,boolean xml){
        this.children.add(toAdd);
        toAdd.parent=this;
        if(xml)
            this.element.appendChild(toAdd.element);
        
    }
    
    
    public String getID(){
        return id;
    }
    
    public void setParent(GElement parent){
        this.parent=parent; 
    }
    public java.awt.Rectangle getBounds(){
        int ex = nElement.getX();
	int ey = nElement.getY();
	int ew = nElement.getWidth();
	int eh = nElement.getHeight();
	return new java.awt.Rectangle(ex, ey, ew, eh);
    }
    //GUI editor Vecchio
    public boolean contains(Point2D point){
        int ex = nElement.getX();
	int ey = nElement.getY();
	int ew = nElement.getWidth();
	int eh = nElement.getHeight();
	return new java.awt.Rectangle(ex, ey, ew, eh).contains(point.getX(), point.getY());
    }
    public GElement getParent(){
        return this.parent;
    }
    public Element getNiftyElement(){
        
        
        return nElement;
        
        
    }
    public Map<String,String> getAttributes(){
      Map<String,String> res = new HashMap<String,String>();
      Types type = getType();
      if(!type.isControl()){
      for(String prop : jada.ngeditor.model.PropretiesResolver.inst.resolve(this.getType()+"Type")){
          String defvalue = getAttribute(prop);
          res.put(prop, defvalue);
      }
      }else{
         for(String prop : jada.ngeditor.model.PropretiesResolver.inst.resolve(this.getType()+"Type")){
          String defvalue = getAttribute(prop);
          res.put(prop, defvalue);
      } 
      }
     return res;
    }
     public org.w3c.dom.Element toXml(){
        return this.element;
    }
    
    public LinkedList<GElement> getElements(){
        return this.children;
    }
    
    public void removeAttribute(String key){
        this.element.removeAttribute(key);
        if(key.equals("style")){
            this.nElement.setStyle("");
            this.nElement.getElementType().removeWithTag("style");
            this.nElement.getRenderer(ImageRenderer.class).setImage(null);
        }else{
            Attributes att = this.nElement.getElementType().getAttributes();
            att.set(key, "");
            //this.nElement.setConstraintY(SizeValue.px(200));
            this.toBeRemoved.add(key);
        }
        
    }
    
    protected void processRemoved(){
         Attributes att = this.nElement.getElementType().getAttributes();
         for(String s : this.toBeRemoved){
             att.remove(s);
         }
    }
    public String getAttribute(String key){
         Attributes att = this.nElement.getElementType().getAttributes();
         if(att.get(key)==null)
             return "";
         return att.get(key);
    }
    public void addAttribute(String key , String val){
        Attributes att = this.nElement.getElementType().getAttributes();
        if(key.equals("id")){
            this.id = val;
        }else if(key.equals("style")) {
            this.oldStyle = att.get("style");
        }
        
        this.element.setAttribute(key, val);
        att.set(key, val);
    }
    /*
     * Heavy method for controls should be called not often
     */
    public void refresh(){
       Nifty temp = nElement.getNifty();
       Attributes att = this.nElement.getElementType().getAttributes();
       String newStyle = att.get("style");
       Attributes attcopy = new Attributes(att);
       // Add the old style if there was one
       if(oldStyle != null && !oldStyle.equals(newStyle)){
            
            att.set("style", oldStyle);
            nElement.setStyle(newStyle);
            attcopy = att;
            oldStyle = newStyle;
       }
       if(att.isSet("renderOrder")){
           nElement.setRenderOrder(att.getAsInteger("renderOrder"));
       }
       nElement.setId(id);
       if(getType().isControl()){
          this.heavyRefresh(temp,attcopy);
       }else{
           this.lightRefresh(attcopy);
       }
       this.processRemoved();
    }
    /*
     * used for simple elment attributes
     */
     public void lightRefresh(){
        Nifty temp = nElement.getNifty();
        Screen currentScreen = temp.getCurrentScreen();
        Attributes att = this.nElement.getElementType().getAttributes();
        nElement.initializeFromAttributes(currentScreen,att, temp.getRenderEngine());
        currentScreen.layoutLayers();
     }
    
    private void lightRefresh(Attributes att){
       Nifty temp = nElement.getNifty();
        Screen currentScreen = temp.getCurrentScreen();
        nElement.initializeFromAttributes(currentScreen,att, temp.getRenderEngine());
        currentScreen.layoutLayers();
    }
    
    protected void heavyRefresh(Nifty nifty,Attributes att){
        int index= parent.getNiftyElement().getChildren().indexOf(nElement);
        final GElement telement = this;
        
         nElement.markForRemoval(new EndNotify() {

            @Override
            public void perform() {
               this.buildChild(telement);
            }
            
            private void buildChild(GElement ele){
                for(GElement e : ele.getElements()){
                    ele.getDropContext().addChild(e.getNiftyElement());
                    e.refresh();
                    this.buildChild(e);
                }
            }
        });
         final HashMap<String,String> attributes = new HashMap<String,String>();
        for(int i =0;i<element.getAttributes().getLength();i++){
            Node n = element.getAttributes().item(i);
            attributes.put(n.getNodeName(),n.getNodeValue());
            
        }
         for(String sel : attributes.keySet()){
               builder.set(sel, attributes.get(sel));
         }
        
         nElement = builder.build(nifty, nifty.getCurrentScreen(), this.parent.getDropContext(),index);
         nifty.getCurrentScreen().layoutLayers();
    }
    
   
    public void reloadElement(Nifty manager){
           Nifty nif = manager;
        if(nElement != null)
            nif = nElement.getNifty();
        nElement = parent.nElement.findElementById(id);
    }
    @Override
    public String toString(){
        return this.id;
    }
    @Override
    public boolean equals(Object e){
        if(e instanceof GElement){
            GElement temp = (GElement)e;
            return UniID == temp.getUniID();
        } else
            return false;
    }
    protected Element getDropContext(){
        return nElement;
    }
    
    private void createWithChildren(Nifty nifty){
        this.createNiftyElement(nifty);
        for(GElement ele : children)
            ele.createWithChildren(nifty);
    }
    public abstract Types getType();
    public void createNiftyElement(Nifty nifty){
        final HashMap<String,String> attributes = new HashMap<String,String>();
        for(int i =0;i<element.getAttributes().getLength();i++){
            Node n = element.getAttributes().item(i);
            attributes.put(n.getNodeName(),n.getNodeValue());
            
        }
         for(String sel : attributes.keySet()){
               builder.set(sel, attributes.get(sel));
         }
         nElement = builder.build(nifty, nifty.getCurrentScreen(), this.parent.getDropContext());
        
    }
    public abstract GElement create(String id, org.w3c.dom.Element ele);
    public abstract void initDefault();
   
    
   
    
}
