package subObjects;

import com.codename1.charts.util.ColorUtil;
import com.codename1.ui.Graphics;
import com.codename1.ui.Transform;
import com.codename1.ui.geom.Point;
import com.mycompany.a3.SubObject;

public class DroneArm extends SubObject {
    private Point center;
    private int myColor;

    public DroneArm(int color) {
        this.myColor = color;
    }

    public void draw(Graphics g, Point pCmpRelPrnt, Point pCmpRelScrn) {
        g.setColor(myColor);
        Transform gXform = Transform.makeIdentity();
        g.getTransform(gXform);
        Transform copy = gXform.copy();
        gXform.translate(pCmpRelScrn.getX(), pCmpRelScrn.getY());
        gXform.translate(getTranslate().getTranslateX(),
                getTranslate().getTranslateY());
        gXform.concatenate(getRotation());
        gXform.scale(getScale().getScaleX(), getScale().getScaleY());
        gXform.translate(-pCmpRelScrn.getX(), -pCmpRelScrn.getY());
        g.setTransform(gXform);
        // draw the lines as before
        g.fillRect(pCmpRelPrnt.getX(),
                pCmpRelPrnt.getY(), 5, 20);
        g.setTransform(copy);
    }
}
