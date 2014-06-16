package webrc.robot.control;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class Control {

    Logger log = LoggerFactory.getLogger(this.getClass());
    Logger blackbox = LoggerFactory.getLogger("blackbox");

    /**
     * changes some value (a motor or something)
     *
     * @param value
     */
    public abstract void set(String key, Object value);

}
