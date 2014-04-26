package webrc.robot.controller;

import webrc.messaging.Pubscriber;
import webrc.robot.domain.ImageOverlay;
import webrc.robot.util.FontRasters;

import javax.annotation.PostConstruct;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: benjaminmorgan
 * Date: 4/13/14
 * Time: 5:43 PM
 * To change this template use File | Settings | File Templates.
 */
public class ScreenController extends Pubscriber {


    private class TextOverlay {
        public int x,y;
        public int w=0,h=0;
        String text;

        public TextOverlay(int x, int y, String text) {
            this.x=x; this.y=y;
            this.text=text;
        }

        private int getX() {
            return x;
        }

        private int getY() {
            return y;
        }

        private int getW() {
            return w;
        }

        private int getH() {
            return h;
        }

        private String getText() {
            return text;
        }
    }

    public Map<String, TextOverlay> overlays = new HashMap<String, TextOverlay>();
    Font font = Font.decode("Monospaced").deriveFont(10.0f);


    public ScreenController() {
        overlays.put("ssid", new TextOverlay(0, 0, "ssid:"));
        overlays.put("ip", new TextOverlay(0, 12, "ip:"));
        overlays.put("bitrate", new TextOverlay(0, 24, "bitrate:"));
        overlays.put("signal", new TextOverlay(0, 36, "signal:"));
        overlays.put("rx", new TextOverlay(0,48, "rx:"));
        overlays.put("tx", new TextOverlay(64,48, "tx:"));
//        overlays.put("linkQuality", new TextOverlay(0, 48, "quality:"));
//        overlays.put("signal", new TextOverlay(0, 48, "signal:"));


    }



    @PostConstruct
    public void init() {
        for(String key : overlays.keySet())
            this.subscribe(key);
    }



    @Override
    protected void notify(Map<String, Object> values) {

        for(String key : values.keySet()) {
            if(overlays.containsKey(key)) {
                Object value = values.get(key);
                TextOverlay overlay = overlays.get(key);

                BufferedImage img = FontRasters.textImage(overlay.getText()+(value == null ? "?" : value), font);

                //if the image shrunk at all, scale it up to cover at least the entire older image
                if(img.getHeight() < overlay.getH() || img.getWidth() < overlay.getW()) {
                    BufferedImage newImg = new BufferedImage(Math.max(img.getWidth(), overlay.getW()), Math.max(img.getHeight(), overlay.getH()), BufferedImage.TYPE_BYTE_BINARY);

                    Graphics graphics = newImg.getGraphics();
                    graphics.drawImage(img, 0, 0, img.getWidth(), img.getHeight(), null);

                    img=newImg;
                }

                overlay.w=img.getWidth();
                overlay.h=img.getHeight();

                ImageOverlay imgOverlay = new ImageOverlay(img, overlay.getX(), overlay.getY());
                this.publishOne("screen", imgOverlay);
            }
        }
    }


}
