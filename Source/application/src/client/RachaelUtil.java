package client;

import org.opencv.core.Mat;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;

class RachaelUtil {

    public static final int CODE_CALL_REQUEST = 11;


    private final int NO_RESPONSE = 0;
    private final int ACCEPTED    = 1;
    private final int DECLINED    = 2;

    public static BufferedImage Mat2BufferedImage(Mat matrix){

        int channelType = BufferedImage.TYPE_BYTE_GRAY;
        if ( matrix.channels() > 1 ) {
            channelType = BufferedImage.TYPE_3BYTE_BGR;
        }
        int bufferSize = matrix.channels()*matrix.cols()*matrix.rows();
        byte [] b = new byte[bufferSize];
        matrix.get(0,0,b); // get all the pixels
        BufferedImage image = new BufferedImage(matrix.cols(),matrix.rows(), channelType);
        final byte[] targetPixels = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
        System.arraycopy(b, 0, targetPixels, 0, b.length);
        return image;

    }
}
