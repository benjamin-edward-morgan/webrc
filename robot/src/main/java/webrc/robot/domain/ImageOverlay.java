package webrc.robot.domain;

import java.awt.image.BufferedImage;

/**
 * Created with IntelliJ IDEA.
 * User: benjaminmorgan
 * Date: 4/12/14
 * Time: 7:55 PM
 * To change this template use File | Settings | File Templates.
 */
public class ImageOverlay {

    public BufferedImage image;
    public int x, y;

    public ImageOverlay(BufferedImage image, int x, int y) {
        this.image=image;
        this.x=x;
        this.y=y;
    }

}
