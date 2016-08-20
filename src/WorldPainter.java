import java.awt.*;

/**
 * Created by silasa on 8/20/16.
 */
class WorldPainter extends Component {
    private Julf main;

    public WorldPainter(Julf parrent) {
        main = parrent;
    }

    public Dimension getPreferredSize() {
        return new Dimension(640, 480);
    }

    public void paint(Graphics g) {
        Dimension size = getSize();
        double playerXPos = main.player.getPos()[0];
        double playerYPos = main.player.getPos()[1];
        double playerOrient = main.player.getOrientation()-Math.toRadians(main.conf.fov)/2;
        double lineAngle = Math.toRadians((double) main.conf.fov) / (double) size.width;

        int lines = 0;
//        double minRelVecX = Math.sin(Math.toRadians(main.conf.fov/2)+90) * main.conf.incLength;
        g.setColor(Color.white);
        while (lines < size.width) {
            int lineHeight = 0;
            boolean hasCollided = false;
            int collide = main.conf.emptyColor;

            double A = lineAngle*lines;
            double C = Math.toRadians(90-main.conf.fov/2);
            double B = Math.toRadians(180)-A-C;
            double b = main.conf.maxLength;
            double c = b/Math.sin(B)*Math.sin(C);
            double fishEyeCorrection = c/b;
//            System.out.println(fishEyeCorrection);

            double cAngle = playerOrient + lineAngle * (double) lines;
            double vecX = Math.sin(cAngle) * main.conf.incLength;
//            double relVecX = Math.sin(minRelVecX+lineAngle*lines);
            double vecY = Math.cos(cAngle) * main.conf.incLength;

            double lineLength = 0;
            double cX = playerXPos;
            double cY = playerYPos;

            int prevX = (int) Math.floor(cX);
            int prevY = (int) Math.floor(cY);

            while (lineLength < main.conf.maxLength) {
                lineLength += main.conf.incLength;
                cX += vecX;
                cY += vecY;
                int cXF = (int) Math.floor(cX);
                int cYF = (int) Math.floor(cY);
                if (cXF == prevX && cYF == prevY) {
                    continue;
                }
                prevX = cXF;
                prevY = cYF;
                collide = main.wm.getTile(cXF, cYF);
                if (collide != main.conf.emptyColor) {
                    hasCollided = true;
                    break;
                }
            }
            if (hasCollided) {
//                double fishEyeCorrection = (relVecX-minRelVecX)*main.conf.maxLength;
//                double fishEyeCorrection = Math.sin((lines-(size.width/2)*lineAngle+Math.toRadians(90)))-Math.sin((0-(size.width/2)*lineAngle+Math.toRadians(90)));
                double correctAngle = 1-lineAngle*Math.abs(lines-size.width/2);
//                System.out.println(correctAngle);
//                lineHeight = (int) Math.round((lineLength*Math.cos(correctAngle)) / main.conf.maxLength * (size.height / 2d - 10d));
                lineHeight = (int) Math.round((lineLength) / main.conf.maxLength * (size.height / 2d - 10d));
//                System.out.println(""+vecX+", "+minVecX+", "+(vecX-minVecX));
                Color cColor = new Color(collide);
                Color nColor = ColorUtils.stain(cColor, lineLength/main.conf.maxLength*0.75);
                g.setColor(nColor);
                g.drawLine(lines, lineHeight, lines, size.height - lineHeight);
            } else {
                lineHeight = size.height / 2 - 10;
                g.setColor(Color.darkGray);
                g.drawLine(lines, lineHeight, lines, size.height - lineHeight);
            }
            lines++;
        }
    }
}
