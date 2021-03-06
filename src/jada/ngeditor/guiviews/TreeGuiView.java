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
package jada.ngeditor.guiviews;

import jada.ngeditor.controller.GUIEditor;
import jada.ngeditor.listeners.ElementSelectionListener;
import jada.ngeditor.listeners.PopUpShowListener;
import jada.ngeditor.listeners.actions.Action;
import jada.ngeditor.model.Types;
import jada.ngeditor.model.elements.GElement;
import jada.ngeditor.model.elements.GScreen;
import jada.ngeditor.renderUtil.NiftyTreeRender;
import java.util.Enumeration;
import java.util.Observable;
import java.util.Observer;
import javax.swing.LookAndFeel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

/**
 *
 * @author cris
 */
public class TreeGuiView extends javax.swing.JPanel implements Observer {

    private GUIEditor currentGui;

    /**
     * Creates new form TreeGuiView
     */
    public TreeGuiView() {
        initComponents();
        this.jTree2.addMouseListener(new PopUpShowListener(this.jPopupMenu1));
    }

    public void initView(GUIEditor gui) {

        this.jTree2.addTreeSelectionListener(new ElementSelectionListener(gui));
        jTree2.setCellRenderer(new NiftyTreeRender());
        DefaultMutableTreeNode root = (DefaultMutableTreeNode) this.jTree2.getModel().getRoot();
        root.removeAllChildren();
        root.setUserObject(gui);

        for (GScreen screen : gui.getGui().getScreens()) {
            DefaultMutableTreeNode screenNode = new DefaultMutableTreeNode(screen);
            addRecursive(screen, screenNode);
            root.add(screenNode);
            
        }
        for (int row = 0; row < jTree2.getRowCount(); row++) {
            jTree2.expandRow(row);
        }
      
        this.jTree2.updateUI();
    }

    private void addRecursive(GElement ele, DefaultMutableTreeNode parent) {
        for (GElement sele : ele.getElements()) {
            DefaultMutableTreeNode tmp = new DefaultMutableTreeNode(sele);
            parent.add(tmp);
            addRecursive(sele, tmp);
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPopupMenu1 = new javax.swing.JPopupMenu();
        HidePop = new javax.swing.JMenuItem();
        ShowPop = new javax.swing.JMenuItem();
        DelPop = new javax.swing.JMenuItem();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTree2 = new javax.swing.JTree();

        HidePop.setText("Hide");
        HidePop.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                HidePopActionPerformed(evt);
            }
        });
        jPopupMenu1.add(HidePop);

        ShowPop.setText("Show");
        ShowPop.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ShowPopActionPerformed(evt);
            }
        });
        jPopupMenu1.add(ShowPop);

        DelPop.setText("Remove");
        DelPop.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                DelPopActionPerformed(evt);
            }
        });
        jPopupMenu1.add(DelPop);

        setMinimumSize(new java.awt.Dimension(80, 20));
        setPreferredSize(new java.awt.Dimension(60, 60));

        javax.swing.tree.DefaultMutableTreeNode treeNode1 = new javax.swing.tree.DefaultMutableTreeNode("No-Gui");
        jTree2.setModel(new javax.swing.tree.DefaultTreeModel(treeNode1));
        jScrollPane2.setViewportView(jTree2);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 158, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 254, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void HidePopActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_HidePopActionPerformed
        this.currentGui.getElementEditor().setVisibile(false);
    }//GEN-LAST:event_HidePopActionPerformed

    private void ShowPopActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ShowPopActionPerformed
        this.currentGui.getElementEditor().setVisibile(true);
    }//GEN-LAST:event_ShowPopActionPerformed

    private void DelPopActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_DelPopActionPerformed
        this.currentGui.removeSelected();

    }//GEN-LAST:event_DelPopActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuItem DelPop;
    private javax.swing.JMenuItem HidePop;
    private javax.swing.JMenuItem ShowPop;
    private javax.swing.JPopupMenu jPopupMenu1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTree jTree2;
    // End of variables declaration//GEN-END:variables

    @Override
    public void update(Observable o, Object arg) {
        Action act = (Action) arg;
        if (act.getType() == Action.ADD) {
            if (!act.getGUIElement().getType().equals(Types.SCREEN)) {
                GElement e = act.getGUIElement().getParent();
                DefaultMutableTreeNode parent = this.searchNode(e);
                parent.add(new DefaultMutableTreeNode(act.getGUIElement()));
            } else {
                DefaultMutableTreeNode node = (DefaultMutableTreeNode) jTree2.getModel().getRoot();
                node.add(new DefaultMutableTreeNode(act.getGUIElement()));
            }
            for (int row = 0; row < jTree2.getRowCount(); row++) {
                jTree2.expandRow(row);
            }
            jTree2.updateUI();
        } else if (act.getType() == Action.DEL) {
            GElement ele = act.getGUIElement();
            this.searchNode(ele).removeFromParent();
            jTree2.updateUI();
        } else if (act.getType() == Action.MOV) {
            GElement ele = act.getGUIElement();
            DefaultMutableTreeNode node = this.searchNode(ele);
            DefaultMutableTreeNode parent = this.searchNode(ele.getParent());
            parent.add(node);
            for (int row = 0; row < jTree2.getRowCount(); row++) {
                jTree2.expandRow(row);
            }
            jTree2.updateUI();
        } else if (act.getType() == Action.NEW) {
            this.newGui(((GUIEditor) o));
        } else if (act.getType() == Action.UPDATE) {
            int i = currentGui.getElementEditor(act.getGUIElement()).getIndex();
            DefaultMutableTreeNode node = searchNode(act.getGUIElement());
            DefaultMutableTreeNode parent = (DefaultMutableTreeNode) node.getParent();
            parent.insert(node, i);
            jTree2.updateUI();
        } else if (act.getType() == Action.SEL){
            DefaultMutableTreeNode node = this.searchNode(act.getGUIElement());
            if(node != null){
                TreePath temp = new TreePath(node.getPath());
                jTree2.setSelectionPath(temp);
            }
            
        }
    }

    public DefaultMutableTreeNode searchNode(Object toFind) {
        DefaultMutableTreeNode node = null;
        Enumeration e = ((DefaultMutableTreeNode) jTree2.getModel().getRoot()).breadthFirstEnumeration();
        while (e.hasMoreElements()) {
            node = (DefaultMutableTreeNode) e.nextElement();
            if (toFind.equals(node.getUserObject())) {
                return node;
            }
        }
        return null;
    }

    public void newGui(GUIEditor toChange) {
        this.currentGui = toChange;
        this.initView(toChange);
    }
}
