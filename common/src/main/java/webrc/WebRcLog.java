package webrc;

public class WebRcLog {

    Object object;

    private WebRcLog(Object object) {
        this.object = object;
    }

    public static WebRcLog getLog(Object o) {
        return new WebRcLog(o);
    }

    public void log(String s) {
        System.out.println(object.toString() + "\n" + s);
    }

    public void log(Exception e) {
        System.out.println(object.toString());
        e.printStackTrace(System.out);
    }
}
