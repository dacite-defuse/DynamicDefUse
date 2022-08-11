package dacite.intellij.visualisation;

import dacite.intellij.DefUseData.DefUseData;

import javax.swing.tree.DefaultMutableTreeNode;

public class MethodNode extends DefaultMutableTreeNode {
    /**
     * @param resource
     */
    public MethodNode(String resource) {
        super(resource);
    }

    @Override
    public void setUserObject(Object userObject) {
        if(userObject instanceof String) {
            super.setUserObject(userObject);
        }
    }

    @Override
    public String getUserObject() {
        return (String) super.getUserObject();
    }

}