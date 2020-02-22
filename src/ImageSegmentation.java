import java.awt.*;
import java.awt.image.*;
import java.io.*;
import java.util.Arrays;
import javax.swing.*;

public class ImageSegmentation {
    JFrame frame;
    JLabel lbIm1;
    BufferedImage imgOne;
    int width = 512;
    int height = 512;
    int h1;
    int h2;

    /** Read Image RGB
     *  Reads the image of given width and height at the given imgPath into the provided BufferedImage.
     */
    private void readImageRGB(int width, int height, String[] args, BufferedImage img)
    {
        try
        {
            int frameLength = width*height*3;

            File file = new File(args[0]);
            RandomAccessFile raf = new RandomAccessFile(file, "r");
            raf.seek(0);

            byte[] bytes = new byte[(int) (long) frameLength];

            raf.read(bytes);
            boolean hsv_flag = h2 - h1 == 359;
            if (!hsv_flag) {
                System.out.println("Segmenting image with from range [" + h1 + " - " + h2 + "]");
            }

            int ind = 0;
            for(int y = 0; y < height; y++)
            {
                for(int x = 0; x < width; x++)
                {
                    int r = bytes[ind] & 0xff;
                    int g = bytes[ind+height*width] & 0xff;
                    int b = bytes[ind+height*width*2] & 0xff;

                    int pix = 0xff000000 | ((r & 0xff) << 16) | ((g & 0xff) << 8) | (b & 0xff);
                    float[] hsv = rgbToHSV(r, g, b);
                    if (!hsv_flag) {
                        if (hsv[0] < (float) h1 || hsv[0] > (float) h2) {
                            Color c = new Color(pix);
                            int red = (int)(c.getRed() * 0.299);
                            int green = (int)(c.getGreen() * 0.587);
                            int blue = (int)(c.getBlue() *0.114);
                            Color newColor = new Color(red+green+blue, red+green+blue,red+green+blue);
                            img.setRGB(x,y,newColor.getRGB());
                        } else {
                            img.setRGB(x, y, pix);
                        }
                    } else {
                        img.setRGB(x, y, pix);
                    }
                    ind++;
                }
            }
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Convert rgb to hsv color space
     * @param r red value [0 - 1]
     * @param g green value [0 - 1]
     * @param b blue value [0 - 1]
     * @return hsv array of length 3
     */
    private float[] rgbToHSV(float r, float g, float b) {
        float[] hsv = new float[3];
        float min = Math.min(Math.min(r, g), b);
        float max = Math.max(Math.max(r, g), b);
        hsv[2] = max;

        float delta = max - min;

        if (max != 0)
            hsv[1] = delta/max;
        else {
            hsv[1] = 0;
            hsv[0] = -1;
            return hsv;
        }
        if (r == max)
            hsv[0] = (g - b)/delta;
        else if (g == max)
            hsv[0] = 2 + (b - r)/delta;
        else
            hsv[0] = 4 + (r - g)/delta;
        hsv[0] *= 60;
        if (hsv[0] < 0)
            hsv[0] += 360;
        return hsv;
    }

    public void showIms(String[] args){
        // Initialize h1 nd h2
        h1 = Integer.parseInt(args[1]);
        h2 = Integer.parseInt(args[2]);


        // Read in the specified image
        imgOne = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        readImageRGB(width, height, args, imgOne);

        // Use label to display the image
        frame = new JFrame();
        GridBagLayout gLayout = new GridBagLayout();
        frame.getContentPane().setLayout(gLayout);

        lbIm1 = new JLabel(new ImageIcon(imgOne));

        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = GridBagConstraints.CENTER;
        c.weightx = 0.5;
        c.gridx = 0;
        c.gridy = 1;
        frame.getContentPane().add(lbIm1, c);

        frame.pack();
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        ImageSegmentation ren = new ImageSegmentation();
        ren.showIms(args);
    }
}
