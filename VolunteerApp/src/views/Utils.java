/**
 * 
 */
package views;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;

import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;

import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritablePixelFormat;

/**
 * @author erubi
 *
 *
 * https://stackoverflow.com/questions/25223553/how-can-i-create-an-utility-class
 */
public final class Utils {
	
	
	/**
	 * Convert a Mat object (OpenCV) in the corresponding Image for JavaFX.
	 * translates an OpenCV Mat to a JavaFX Image.
	 * 
	 * The class SwingFXUtils:
	 * 		 provides utility methods for converting data types between Swing/AWT and JavaFX formats.
	 *
	 * @param frame
	 *            the {@link Mat} representing the current frame. ( the BufferedImage object to be converted to then be displayed)
	 *            
	 * @return the {@link Image} to show. an Image object representing a snapshot of the current pixels in the BufferedImage.
	 */
	public static Image mat2Image(Mat frame)
	{
		try
		{
			// Snapshots the specified BufferedImage and stores a copy of its pixels into a JavaFX Image object, 
			//creating a new object if needed. The returned Image will be a static snapshot of the state of the pixels 
			//in the BufferedImage at the time the method completes. 
			//Further changes to the BufferedImage will not be reflected in the Image.
			return SwingFXUtils.toFXImage(matToBufferedImage(frame), null);
		}
		catch (Exception e)
		{
			System.err.println("Cannot convert the Mat object: " + e);
			return null;
		}
	}

	/**
	 * Generic method for putting element running on a non-JavaFX thread on the
	 * JavaFX thread, to properly update the UI.
	 * As java doesnt allow us to perform an update of a GUI element
	 * 		in a thread that DIFFERS from the main thread, we need to:
	 * 		get the new frame in a SECOND THREAD and refresh our ImageView in the main thread.
	 * 
	 * @param property
	 *            a {@link ObjectProperty}
	 * @param value
	 *            the value to set for the given {@link ObjectProperty}
	 */
	public static <T> void onFXThread(final ObjectProperty<T> property, final T value)
	{
		Platform.runLater(() -> {
			property.set(value);
		});

		/*https://opencv-java-tutorials.readthedocs.io/en/latest/03-first-javafx-application-with-opencv.html
		 * The above code is the same as:
		 Platform.runLater(new Runnable() {
		 	@Override public void run(){ currentFrame.setImage(imageToShow);}}
		 */
	}

	/**
	 * Support for the {@link mat2image()} method.
	 * 
	 * In order to put the captured frame into the ImageView we need to 
	 * convert the Mat object into an Image. 
	 * Thus, we must create a buffer to store the Mat object.
	 * 
	 * It will convert an OpenCV Mat object to a BufferedImage gui.
	 * 		This conversion is made through the creation of: a byte array, which will store matrix contents.
	 * 
	 * @param original      the {@link Mat} object (matrix) in BGR or grayscale
	 * @return the corresponding {@link BufferedImage}
	 */
	private static BufferedImage matToBufferedImage(Mat original) {
		/*
		 * Similar to code in pg.30 & 36S.
		 * 
		 * */
		
		// init
		BufferedImage image = null;

		//First, we find out the dimensions of the image.
		int width = original.width(); 
		int height = original.height(); 
		int channels = original.channels();
		
		/*
		 * The appropriate size for the 'byte array' is allocated through the multiplication 
		 * of the number of channels by the number of columns and rows
*/
		int bufferSize = width * height * channels;


		


		/*
		 * 
		 * Then, we build a byte array with that ^^ size.
		 * We use the 'get(row, col, byte[])' method to copy the matrix contents 
		 * 		in our recently created byte array.
		 * i.e. It puts all the elements into the byte array.
		 * */
		
		byte[] sourcePixelsBuffer = new byte[bufferSize];
		// get all the pixels:
		original.get(0, 0, sourcePixelsBuffer);

		if (original.channels() > 1) {
			image = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
		
		} else {
			image = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);
		}
		
		// Raster Graphics = also called bitmap graphics.
		final byte[] targetPixels = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();

		//It is then filled with a fast system call to the 'System.arraycopy' method.
		System.arraycopy(sourcePixelsBuffer, 0, targetPixels, 0, sourcePixelsBuffer.length);

		return image;
	}
	
	public static Mat imageToMat(Image image) {
	    int width = (int) image.getWidth();
	    int height = (int) image.getHeight();
	    byte[] buffer = new byte[width * height * 4];

	    PixelReader reader = image.getPixelReader();
	    WritablePixelFormat<ByteBuffer> format = WritablePixelFormat.getByteBgraInstance();
	    reader.getPixels(0, 0, width, height, format, buffer, 0, width * 4);

	    Mat mat = new Mat(height, width, CvType.CV_8UC4);
	    mat.put(0, 0, buffer);
	    return mat;
	}
	
	
	public static void readCSV(String csvFilePath2, ArrayList<Mat> images, ArrayList<Integer> labels)  {
		BufferedReader br;
		try {
			br = new BufferedReader(new FileReader(csvFilePath2));

			String line;

			while((line = br.readLine()) != null){
				String[] tokens = line.split("\\;");
				
				Mat readImage = Imgcodecs.imread(tokens[0], 0);
				images.add(readImage);
				labels.add(Integer.parseInt(tokens[1]));
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
