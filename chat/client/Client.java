package chat.client;

// GUI
import java.awt.*;
import javax.swing.*;

import java.net.*;
import java.io.*;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.UnknownHostException;

import chat.UDPConnector;

import org.bytedeco.javacpp.*;
import org.bytedeco.javacv.*;
import static org.bytedeco.javacpp.opencv_core.*;
import static org.bytedeco.javacpp.opencv_imgproc.*;

public class Client extends UDPConnector {
	private static int port = 4436;
	
	private String addrServer = "localhost";
	private int portServer = 4435;
	
	private int sent = 0;
	
	public Client() {
		super(port);
	}

	@Override
	public synchronized void handleError(Exception e) {
		System.out.println(e.getMessage());
		
	}

	@Override
	public synchronized void handlePacketReceived(DatagramPacket packet) {
		String msg = new String(packet.getData());
		
		System.out.println("From Server: " + msg);
	}

	@Override
	public synchronized DatagramPacket createPacketToSend() {
		byte[] msg = new String("ping").getBytes();
		
		if (sent < 1) {
			sent++;
			
			InetAddress addr = null;
			try {
				addr = InetAddress.getByName(addrServer);
			} catch (UnknownHostException e) {
				e.printStackTrace();
			}
			
			return new DatagramPacket(msg, msg.length, addr, portServer);
		}
		
		return null;
	}
	
	public static void main(String[] args) throws Exception  {
		Client client = new Client();
		client.start("");
        
        //***********************************************
        URL url = new URL("https://media.giphy.com/media/aTtX6MTPTTefe/giphy.gif");
        Icon icon = new ImageIcon(url);                  
        JLabel label = new JLabel(icon);                 
                                                         
        JFrame f = new JFrame("Animation");              
        f.getContentPane().add(label);                   
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.pack();                                        
        f.setLocationRelativeTo(null);                   
        f.setVisible(true);                              
        //***********************************************
        
        //-----------------------------------------------------------------------------------------------------------------------------------------
        OpenCVFrameGrabber grabber = new OpenCVFrameGrabber(0);
        OpenCVFrameConverter.ToIplImage converter = new OpenCVFrameConverter.ToIplImage();
        grabber.start();

        IplImage frame = converter.convert(grabber.grab());
        IplImage image = null;
        IplImage prevImage = null;
        IplImage diff = null;

        CanvasFrame canvasFrame = new CanvasFrame("Some Title");
        canvasFrame.setCanvasSize(frame.width(), frame.height());

        CvMemStorage storage = CvMemStorage.create();

        while (canvasFrame.isVisible() && (frame = converter.convert(grabber.grab())) != null) {
            cvClearMemStorage(storage);

            cvSmooth(frame, frame, CV_GAUSSIAN, 9, 9, 2, 2);
            if (image == null) {
                image = IplImage.create(frame.width(), frame.height(), IPL_DEPTH_8U, 1);
                cvCvtColor(frame, image, CV_RGB2GRAY);
            } else {
                prevImage = IplImage.create(frame.width(), frame.height(), IPL_DEPTH_8U, 1);
                prevImage = image;
                image = IplImage.create(frame.width(), frame.height(), IPL_DEPTH_8U, 1);
                cvCvtColor(frame, image, CV_RGB2GRAY);
            }

            if (diff == null) {
                diff = IplImage.create(frame.width(), frame.height(), IPL_DEPTH_8U, 1);
            }

            if (prevImage != null) {
                // perform ABS difference
                cvAbsDiff(image, prevImage, diff);
                // do some threshold for wipe away useless details
                cvThreshold(diff, diff, 64, 255, CV_THRESH_BINARY);

                canvasFrame.showImage(converter.convert(diff));

                // recognize contours
                CvSeq contour = new CvSeq(null);
                cvFindContours(diff, storage, contour, Loader.sizeof(CvContour.class), CV_RETR_LIST, CV_CHAIN_APPROX_SIMPLE);

                while (contour != null && !contour.isNull()) {
                    if (contour.elem_size() > 0) {
                        CvBox2D box = cvMinAreaRect2(contour, storage);
                        // test intersection
                        if (box != null) {
                            CvPoint2D32f center = box.center();
                            CvSize2D32f size = box.size();
/*                            for (int i = 0; i < sa.length; i++) {
                                if ((Math.abs(center.x - (sa[i].offsetX + sa[i].width / 2))) < ((size.width / 2) + (sa[i].width / 2)) &&
                                    (Math.abs(center.y - (sa[i].offsetY + sa[i].height / 2))) < ((size.height / 2) + (sa[i].height / 2))) {

                                    if (!alarmedZones.containsKey(i)) {
                                        alarmedZones.put(i, true);
                                        activeAlarms.put(i, 1);
                                    } else {
                                        activeAlarms.remove(i);
                                        activeAlarms.put(i, 1);
                                    }
                                    System.out.println("Motion Detected in the area no: " + i +
                                            " Located at points: (" + sa[i].x + ", " + sa[i].y+ ") -"
                                            + " (" + (sa[i].x +sa[i].width) + ", "
                                            + (sa[i].y+sa[i].height) + ")");
                                }
                            }
*/
                        }
                    }
                    contour = contour.h_next();
                }
            }
        }
        grabber.stop();
        canvasFrame.dispose();
        //-----------------------------------------------------------------------------------------------------------------------------------------
    }
}
