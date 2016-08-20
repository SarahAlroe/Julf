import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by silasa on 8/20/16.
 */
class WorldPainter extends Component {
    private Julf main;
    private Config config;
    private BufferedImage wallTexture;

    public WorldPainter(Julf parrent) {
        main = parrent;
        config = Config.getInstance();
        wallTexture = main.loadImage("bricks");
    }

    public Dimension getPreferredSize() {
        return new Dimension(640, 480);
    }

    public void paint(Graphics g) {
        Dimension size = getSize();
        double playerXPos = main.player.getPos()[0];
        double playerYPos = main.player.getPos()[1];
        int fov = config.getFov();
        double playerOrient = main.player.getOrientation() - Math.toRadians(fov) / 2;
        double lineAngle = Math.toRadians((double) fov) / (double) size.width;

        int lines = 0;
//        double minRelVecX = Math.sin(Math.toRadians(main.conf.fov/2)+90) * main.conf.incLength;
        g.setColor(Color.darkGray);
        g.fillRect(0,0,size.width,size.height/2);
        g.setColor(Color.lightGray);
        g.fillRect(0,size.height/2,size.width,size.height);
        while (lines < size.width) {
            int lineHeight = 0;
            boolean hasCollided = false;
            int collide = config.getEmptyColor();
            double maxLength = config.getMaxLength();

            double A = lineAngle * lines;
            double C = Math.toRadians(90 - fov / 2);
            double B = Math.toRadians(180) - A - C;
            double b = maxLength;
            double c = b / Math.sin(B) * Math.sin(C);
            double newMaxLength = b+c;
            double fishEyeCorrection = c / b;
//            System.out.println(fishEyeCorrection);

            double cAngle = playerOrient + lineAngle * (double) lines;
            double incLength = config.getIncLength();
            double vecX = Math.sin(cAngle) * incLength;
//            double relVecX = Math.sin(minRelVecX+lineAngle*lines);
            double vecY = Math.cos(cAngle) * incLength;

            double lineLength = 0;
            double cX = playerXPos;
            double cY = playerYPos;

            int prevX = (int) Math.floor(cX);
            int prevY = (int) Math.floor(cY);

            while (lineLength < newMaxLength) {
                lineLength += incLength;
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
                if (collide != config.getEmptyColor()) {
                    hasCollided = true;
                    break;
                }
            }
            if (hasCollided) {
//                double fishEyeCorrection = (relVecX-minRelVecX)*main.conf.maxLength;
//                double fishEyeCorrection = Math.sin((lines-(size.width/2)*lineAngle+Math.toRadians(90)))-Math.sin((0-(size.width/2)*lineAngle+Math.toRadians(90)));
                double correctAngle = 1 - lineAngle * Math.abs(lines - size.width / 2);
//                System.out.println(correctAngle);
//                lineHeight = (int) Math.round((lineLength*Math.cos(correctAngle)) / main.conf.maxLength * (size.height / 2d - 10d));

                double verticalAngle = Math.atan(5d/(2d*lineLength));
                lineHeight = (int) (size.height/Math.tan(Math.toRadians(fov))*Math.tan(verticalAngle));
                int roofLineHeight = (size.height-lineHeight)/2;
                //lineHeight = (int) Math.round((lineLength) / newMaxLength * (size.height / 2d - 10d));
//                System.out.println(""+vecX+", "+minVecX+", "+(vecX-minVecX));
                Color cColor = new Color(collide);
                Color nColor = ColorUtils.stain(cColor, lineLength / newMaxLength * 0.75);
                g.setColor(nColor);
//                g.drawLine(lines, lineHeight, lines, size.height - lineHeight);
                int textureCoord = (int) Math.round((cX+cY)* wallTexture.getWidth())% wallTexture.getWidth();
                g.drawImage(wallTexture,lines,roofLineHeight,lines+1,size.height-roofLineHeight,textureCoord,0,textureCoord+1, wallTexture.getHeight(),this);
            } else {
                lineHeight = size.height / 2 - 10;
                g.setColor(Color.darkGray);
                g.drawLine(lines, lineHeight, lines, size.height - lineHeight);
            }
            lines++;
        }
    }
}
