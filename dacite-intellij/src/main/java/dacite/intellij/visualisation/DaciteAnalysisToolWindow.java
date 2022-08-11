package dacite.intellij.visualisation;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.LogicalPosition;
import com.intellij.openapi.editor.markup.HighlighterTargetArea;
import com.intellij.openapi.editor.markup.RangeHighlighter;
import com.intellij.openapi.editor.markup.TextAttributes;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ProjectRootManager;
import com.intellij.openapi.util.TextRange;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.ui.JBColor;
import com.intellij.ui.components.JBScrollPane;
import dacite.intellij.DefUseData.DefUseData;
import groovy.lang.Tuple;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.*;

public class DaciteAnalysisToolWindow {

    private JButton button;
    private JPanel myToolWindowContent;
    private DefUseTree tree;
    private Project project;

    public DaciteAnalysisToolWindow(ToolWindow toolWindow) {
        button = new JButton("Highlight all");
        button.addActionListener(e -> highlightAll());
        myToolWindowContent = new JPanel();
        myToolWindowContent.setLayout(new BoxLayout(myToolWindowContent, BoxLayout.Y_AXIS));
        ClassNode top =
                new ClassNode("Class");
        createNodes(top);
        tree = new DefUseTree(top);
        tree.setEditable(true);
        DefUseCellEditor cellEditor = new DefUseCellEditor(tree);
        DefUseCellRenderer renderer = new DefUseCellRenderer();
        renderer.setLeafIcon(null);
        renderer.setIcon(AllIcons.Actions.GroupByClass);
        tree.setCellRenderer(renderer);
        tree.setCellEditor(cellEditor);
        cellEditor.addCellEditorListener(new CellEditorListener() {
            @Override
            public void editingStopped(ChangeEvent changeEvent) {
                DefUseData data = (DefUseData) changeEvent.getSource();
                String varName = data.getName();
                String def = data.getDefLocation();
                String use = data.getUseLocation();
                int useline = Integer.parseInt(use.substring(use.lastIndexOf(" ")+1))-1;
                int defline = Integer.parseInt(def.substring(def.lastIndexOf(" ")+1))-1;
                if(data.isChecked()){
                    jumpToFile(project, defline, useline, varName);
                } else {
                    System.out.println("not checked");
                    FileEditorManager manager = FileEditorManager.getInstance(project);
                    Editor textEditor = manager.getSelectedTextEditor();
                    RangeHighlighter[] highlighters = textEditor.getMarkupModel().getAllHighlighters();
                    int count = 0;
                    for (RangeHighlighter high : highlighters) {
                        if (count == 2) {
                            break;
                        }
                        Tuple<Integer> defOffsets = getOffSets(textEditor, defline, varName);
                        int startOffsetDef = defOffsets.get(0);
                        int endOffsetDef = defOffsets.get(1);
                        if (high.getStartOffset() == startOffsetDef && high.getEndOffset() == endOffsetDef) {
                            System.out.println("remove def Highlights");
                            textEditor.getMarkupModel().removeHighlighter(high);
                            count++;
                            continue;
                        }
                        Tuple<Integer> useOffsets = getOffSets(textEditor, useline, varName);
                        int startOffsetUse = useOffsets.get(0);
                        int endOffsetUse = useOffsets.get(1);
                        if (high.getStartOffset() == startOffsetUse && high.getEndOffset() == endOffsetUse) {
                            textEditor.getMarkupModel().removeHighlighter(high);
                            count++;
                        }
                    }
                }
            }

            @Override
            public void editingCanceled(ChangeEvent changeEvent) {

            }
        });
        JScrollPane treeView = new JBScrollPane(tree);
        myToolWindowContent.add(treeView, BorderLayout.CENTER);
        myToolWindowContent.add(button);
    }

    public JPanel getContent() {
        return myToolWindowContent;
    }

    public void jumpToFile(Project project, int defLine, int useLine, String varName){
        VirtualFile[] vFiles = ProjectRootManager.getInstance(project).getContentRoots();
        VirtualFile vf = LocalFileSystem.getInstance().findFileByPath(vFiles[0].getPath()+"/src/main/java/Increment.java");
        if(vf == null){
            System.out.println("noFile");
            System.out.println("update");
        } else{
            FileEditorManager manager = FileEditorManager.getInstance(project);
            manager.openFile(vf, true);
            Editor textEditor = manager.getSelectedTextEditor();
            LogicalPosition position = new LogicalPosition(defLine,0);
            textEditor.getCaretModel().moveToLogicalPosition(position);
            Tuple<Integer> defOffsets = getOffSets(textEditor, defLine, varName);
            int startOffsetDef = defOffsets.get(0);
            int endOffsetDef = defOffsets.get(1);
            textEditor.getMarkupModel().addRangeHighlighter(startOffsetDef, endOffsetDef, 9999, new TextAttributes(null, JBColor.CYAN, null, null, Font.PLAIN), HighlighterTargetArea.EXACT_RANGE);

            LogicalPosition usePosition = new LogicalPosition(useLine,0);
            textEditor.getCaretModel().moveToLogicalPosition(usePosition);
            Tuple<Integer> useOffsets = getOffSets(textEditor, useLine, varName);
            int startOffsetUse = useOffsets.get(0);
            int endOffsetUse = useOffsets.get(1);
            textEditor.getMarkupModel().addRangeHighlighter(startOffsetUse, endOffsetUse, 9999, new TextAttributes(null, JBColor.MAGENTA, null, null, Font.PLAIN), HighlighterTargetArea.EXACT_RANGE);
        }
    }

    public void highlightAll(){
        DefaultMutableTreeNode root = (DefaultMutableTreeNode) tree.getModel().getRoot();
        iterateTree(root);

    }

    public void iterateTree(DefaultMutableTreeNode node){
        for (int i = 0; i < node.getChildCount(); i++) {
            DefaultMutableTreeNode childNode = (DefaultMutableTreeNode) node.getChildAt(i);
            if (childNode.getChildCount() > 0) {
                iterateTree(childNode);
            } else if(childNode instanceof DefUseNode){
                DefUseData[] data = ((DefUseNode) childNode).getUserObject();
                System.out.println("found DefUSeNode");
                for(int k=0; k<data.length;k++){
                    if(!data[k].isChecked()){
                        data[k].setChecked(true);
                    }
                }
            }

        }
    }

    public Tuple<Integer> getOffSets(Editor textEditor, int line, String varName){
        TextRange defRange = new TextRange(textEditor.getDocument().getLineStartOffset(line),textEditor.getDocument().getLineEndOffset(line));
        String lineTextDef = textEditor.getDocument().getText(defRange);
        int index = lineTextDef.indexOf(varName);
        int startOffsetDef = textEditor.getDocument().getLineStartOffset(line) + index;
        int endOffsetDef = startOffsetDef + varName.length();
        return new Tuple<>(startOffsetDef, endOffsetDef);
    }

    /*public void setTableData() {
        model.addRow("x", "Class.method line 2", "Class.method line 3");
        model.addRow("y", "Class.method line 3", "Class.method line 4");
    }*/
    public void setProject(Project project){
        this.project = project;
    }

    private void createNodes(DefaultMutableTreeNode top) {
        DefUseData[] data = new DefUseData[2];
        DefUseData defuse1 = new DefUseData("x", "Class.method line 2", "Class.method line 3");
        DefUseData defuse2 = new DefUseData("y", "Class.method line 3", "Class.method line 4");
        data[0] = defuse1;
        data[1] = defuse2;
        MethodNode method = new MethodNode("method");
        DefUseNode defUseNode = new DefUseNode(data);
        method.add(defUseNode);
        top.add(method);
        method = new MethodNode("test");
        top.add(method);
    }
}