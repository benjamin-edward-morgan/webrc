package webrc.robot.util;

import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

/**
 * Created with IntelliJ IDEA.
 * User: benjaminmorgan
 * Date: 4/8/14
 * Time: 5:39 PM
 * To change this template use File | Settings | File Templates.
 */
public class FontRasters {

    public static byte getImageByte(BufferedImage img, int x, int y) {
        int b = 0x00;
        for (int i = y + 8 - 1; i >= y; i--) {
            b = b << 1;
            if (i >= 0 && i < img.getHeight()) {
                b |= convertPixel(img.getRGB(x, i));
            }
        }
        return (byte) b;
    }

    public static int convertPixel(int rgb) {
        return (((rgb >> 0) & 0xff) > 0 ||
                ((rgb >> 8) & 0xff) > 0 ||
                ((rgb >> 16) & 0xff) > 0
                ? 0x01 :
                0x00
        );
    }

    public static int getStartSeg(int x) {
        return x;
    }

    public static int getEndSeg(int x, int w) {
        return x + w;
    }

    public static int getStartPage(int y) {
        return y / 8;
    }

    public static int getEndPage(int y, int h) {
        return (y + h) / 8;
    }

    //converts an image RGB int
    //to either 0 or 1
    public int toBw(int c) {
        return c > 0 ? 1 : 0;
    }

    public static BufferedImage textImage(String s, Font font) {
        BufferedImage img = new BufferedImage(1, 1, BufferedImage.TYPE_BYTE_BINARY);
        Graphics2D graphics = (Graphics2D) img.getGraphics();
        graphics.setFont(font);

        graphics.setColor(Color.white);
        FontRenderContext fontContext = graphics.getFontRenderContext();

        Rectangle2D bounds = font.getStringBounds(s, fontContext);
        img = new BufferedImage((int) Math.ceil(bounds.getWidth()), (int) Math.ceil(bounds.getHeight()), BufferedImage.TYPE_BYTE_BINARY);
        graphics = (Graphics2D) img.getGraphics();
        graphics.setFont(font);
        graphics.drawString(s, -(int) bounds.getX(), -(int) bounds.getY());

        return img;
    }


}
