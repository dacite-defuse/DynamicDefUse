package dacite.core.analysis;

/**
 * Class necessary to collect a list of parameters for a method invocation.
 */
public class ParameterCollector {
    private static Object[] parameter;
    private static int index;

    public ParameterCollector(int length){
        parameter = new Object[length];
        index = length;
    }

    public static void setParameter(int length){
        parameter = new Object[length];
        index = length;
    }

    public static void push(Object o){
        index = index -1;
        parameter[index]=o;
    }

    public static Object[] getParameters(){
        return parameter;
    }
}
