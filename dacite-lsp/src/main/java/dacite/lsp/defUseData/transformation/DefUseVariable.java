package dacite.lsp.defUseData.transformation;

import java.awt.Color;
import java.util.Objects;

import jakarta.xml.bind.annotation.XmlElement;

public class DefUseVariable {
  private int linenumber;
  private String method;

  private int instruction;
  private int variableIndex;
  private String variableName;

  private String objectName;

  // Additional attributes used after parsing XML
  private DefUseVariableRole role;
  private String color;
  private DefUseChain chain;
  private boolean editorHighlight;

  @XmlElement
  public int getLinenumber() {
    return linenumber;
  }

  public void setLinenumber(int linenumber) {
    this.linenumber = linenumber;
  }

  @XmlElement
  public String getMethod() {
    return method;
  }

  public void setMethod(String method) {
    this.method = method;
  }

  @XmlElement
  public int getVariableIndex() {
    return variableIndex;
  }

  public void setVariableIndex(int variableIndex) {
    this.variableIndex = variableIndex;
  }

  @XmlElement
  public int getInstruction() {
    return instruction;
  }

  public void setInstruction(int instruction) {
    this.instruction = instruction;
  }

  @XmlElement
  public String getVariableName() {
    return variableName;
  }

  public void setVariableName(String variableName) {
    this.variableName = variableName;
  }

  @XmlElement
  public String getObjectName() {
    return objectName;
  }

  public void setObjectName(String name) {
    this.objectName = name;
  }

  public DefUseVariableRole getRole() {
    return role;
  }

  public void setRole(DefUseVariableRole role) {
    this.role = role;
  }

  public String getColor() {
    return color;
  }

  public void setColor(String color) {
    this.color = color;
  }

  public DefUseChain getChain() {
    return chain;
  }

  public void setChain(DefUseChain chain) {
    this.chain = chain;
  }

  public boolean isEditorHighlight() {
    return editorHighlight;
  }

  public void setEditorHighlight(boolean editorHighlight) {
    this.editorHighlight = editorHighlight;
  }

  // Helper methods
  public String getMethodName() {
    return this.method.substring(this.method.lastIndexOf(".") + 1);
  }

  public String getClassName() {
    return this.method.substring(this.method.lastIndexOf("/") + 1, this.method.lastIndexOf("."));
  }

  public String getPackageName() {
    return method.substring(0, Math.max(method.lastIndexOf("/"), 0)).replace("/", ".");
  }

  public boolean matchesPackageClass(String packageClass) {
    return (getPackageName().replace(".", "/") + "/" + getClassName()).equals(packageClass);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    DefUseVariable that = (DefUseVariable) o;
    return linenumber == that.linenumber && instruction == that.instruction && variableIndex == that.variableIndex
        && Objects.equals(method, that.method) && Objects.equals(variableName, that.variableName) && role == that.role;
  }

  @Override
  public int hashCode() {
    return Objects.hash(linenumber, method, instruction, variableIndex, variableName, role);
  }

  @Override
  public String toString(){
    return "name: " + variableName + ", ln: "+linenumber + ", method: "+ method + " ins "+instruction+", isEditorHighlight: "+isEditorHighlight();
  }
}
