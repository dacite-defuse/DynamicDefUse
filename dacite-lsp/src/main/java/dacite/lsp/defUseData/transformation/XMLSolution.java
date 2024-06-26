package dacite.lsp.defUseData.transformation;

import de.wwu.mulib.search.trees.ThrowablePathSolution;
import de.wwu.mulib.search.trees.PathSolution;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.util.HashMap;
import java.util.Map;

@XmlRootElement(name="xmlsolution")
public class XMLSolution {

    @XmlElement
    public boolean exceptional;
    @XmlElement
    public Object returnValue;

    @XmlElement
    //@XmlJavaTypeAdapter(XMLLabelAdapter.class)
    public Map<String, Object> labels;

    public XMLSolution(){

    }

    public XMLSolution(boolean exceptional, Object returnValue, Map<String, Object> labels){
        this.exceptional = exceptional;
        this.returnValue = returnValue;
        this.labels = labels;
    }

    public void setSolution(PathSolution solution){
        this.exceptional = solution instanceof ThrowablePathSolution;
        this.returnValue = solution.getSolution().returnValue;
        labels = new HashMap<>();
        for(Map.Entry<String,Object> entry : solution.getSolution().labels.getIdToLabel().entrySet()){
            String key = entry.getKey();
            Object obj = entry.getValue();
            labels.put(key, obj);
        }
    }

    @Override
    public boolean equals(Object obj){
        if(!(obj instanceof XMLSolution)){
            return false;
        } else {
          XMLSolution s = (XMLSolution) obj;
          if(s.exceptional == this.exceptional && s.returnValue == this.returnValue){
              for(String key :this.labels.keySet()){
                  if(this.labels.get(key) != s.labels.get(key)){
                      return false;
                  }
              }
              return true;
          } else {
              return false;
          }
        }
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
