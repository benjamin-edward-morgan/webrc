package webrc.util;

/**
 * Created with IntelliJ IDEA.
 * User: benjaminmorgan
 * Date: 3/1/14
 * Time: 7:09 PM
 * To change this template use File | Settings | File Templates.
 */
public class Conversion {


    public static Float toFloat(Object o) {
        if (o instanceof Float)
            return (Float) o;
        else if (o instanceof Double) {
            return ((Double) o).floatValue();
        } else if (o instanceof Integer) {
            return ((Integer) o).floatValue();
        } else {
            throw new RuntimeException("expected float, double or integer but recieved: " + o);
        }
    }
}
