/**
 * 
 */
package views;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
import org.opencv.objdetect.Objdetect;
import org.opencv.videoio.VideoCapture;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.Event;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;

/**
 * @author Eva Rubio
 *
 */
public class AttendanceTrackerViewController implements Initializable {
	@FXML
	private Text dateText;
	@FXML
	private ImageView currentFrame;
	@FXML
	private Button cameraButton;
	@FXML
	private Button takePicButton;
	@FXML
	private ImageView picToShow;

	//@FXML private CheckBox haarClassifier;
	//@FXML private CheckBox lbpClassifier;

	@FXML
	private CheckBox grayScale;

	@FXML
	private CheckBox logoCheckBox;

	@FXML
	private Button savePicButton;
	// the logo to be loaded
	private Mat logo;
	
	//public static String basePath=System.getProperty("user.dir");
	public static String classifierPath="resources/lbpcascades/lbpcascade_frontalface.xml";


	/**
	 * The timer for acquiring the video stream. For Video cameras, there is usually
	 * a limit of how many frames they can digitalize per second. This timer
	 * variables sets the frame rate. After being initialized (ex. at 30) it will
	 * open a background task every 33 milliseconds.
	 */
	private ScheduledExecutorService timer;

	/**
	 * 'VideoCapture capture'
	 * 
	 * It is the OpenCV Object that actually performs the VideoCapture capture.
	 * 
	 * Essentially, all the functionalities that are required for Video
	 * Manippulation, are integrated in the VideoCapture Class.
	 */
	private VideoCapture capture; // The OpenCV Object that performs the VideoCapture capture.

	/**
	 * A Flag to change the button's behavior.
	 */
	private boolean cameraActive;
	private boolean takePicClicked = false;
	private boolean isFaceDetected;

	/**
	 * Face Cascade Classifier.
	 * 
	 */
	//private CascadeClassifier faceCascade;
	/**
	 * Sets the Minimum size/(area¿?) of the Face to be DETECTED. --> It is needed
	 * in the actual DETECTION Function. Later on, we set the minimum size to be the
	 * 20% of the frame's height. ^^Set in the method: 'detectAndDisplay()'
	 */
	//private int absoluteFaceSize;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		/*
		 * Instantiating the VideoCapture Object to Capture a Webcam Stream. (using its
		 * constructor). The Camera index is set to 1, representing the built-in camera.
		 */
		this.capture = new VideoCapture(1);
		

		// set a fixed width for the frame
		// currentFrame.setFitWidth(600);
		// preserve image ratio
		currentFrame.setPreserveRatio(true);

	}

	/**
	 * The action triggered by pushing the button on the GUI.
	 */
	@FXML
	protected void startCamera() {
		// set a fixed width for the frame
		// this.currentFrame.setFitWidth(600);
		// preserve image ratio
		// this.currentFrame.setPreserveRatio(true);
		if (!this.cameraActive) {
			System.out.println(" camera active");

			// Sisable setting checkboxes
			//this.haarClassifier.setDisable(true);
			//this.lbpClassifier.setDisable(true);
			this.takePicButton.setDisable(false);

			// Start the Video Capture
			// the param is the camera id to use... weird.
			this.capture.open(0);

			/**
			 * .isOpened() -- Checks to see if the Video Stream is available or not. (check
			 * if the Capture is open) i.e. Checks whether it is instantiated.
			 * 
			 * Checks to see if the binding of the class to a Video Source (the Samera in
			 * our case) was successful or not.
			 * 
			 * In case things go WRONG while using the 'read()' method, that is, the camera
			 * gets disconnected, then the method returns FALSE.
			 * 
			 * Returns TRUE if Video Capturing has been initialized already. If the previous
			 * call to VideoCapture constructor or VideoCapture::open() succeeded, the
			 * method returns TRUE.
			 */
			if (this.capture.isOpened()) {
				this.cameraActive = true;
				// this.takePicClicked = true;

				// Grabs a frame every 33 ms (30 frames/sec).
				// it creates a background task to do so.
				Runnable frameGrabber = new Runnable() {

					@Override
					public void run() {
						// effectively grab and process a single frame
						// calls 'capture.read(frame)'
						Mat frame = grabFrame();
						// convert the frame into image and show it
						// returns an Image
						// --- calls 'matToBufferedImage()'
						Image imageToShow = Utils.mat2Image(frame);
						updateImageView(currentFrame, imageToShow);

						// DISPLAY PICTURE ON RIGHT SIDE OF WINDOW.

						if (takePicClicked) {
							// if(isFaceDetected==false) {
							// //savePicButton.setDisable(false);
							// Alert errorAlert = new Alert(AlertType.ERROR);
							// errorAlert.setTitle("Error Dialog");
							// errorAlert.setHeaderText("No face detected.");
							// errorAlert.setContentText("There appears to be a problem while storing the
							// picture. Please contact IT Support.");
							//
							// errorAlert.showAndWait();
							//
							// } else {
							Utils.onFXThread(picToShow.imageProperty(), imageToShow);
							takePicClicked = false;
							// savePicButton
							savePicButton.setDisable(false);

							savePicButton.setOnAction(e -> saveImgToFile(imageToShow, isFaceDetected));
							// }

							// if(savePicButton.isPressed()) {
							//
							// File outputFile = new File("resources/savedPics/");
							// BufferedImage bImage = SwingFXUtils.fromFXImage(imageToShow, null);
							// try {
							// ImageIO.write(bImage, "png", outputFile);
							// } catch (IOException e) {
							// throw new RuntimeException(e);
							// }
							// }
						}
					}
				};

				this.timer = Executors.newSingleThreadScheduledExecutor();
				this.timer.scheduleAtFixedRate(frameGrabber, 0, 33, TimeUnit.MILLISECONDS);

				// update the button content
				this.cameraButton.setText("Stop Camera");
			} else {
				// log the error
				System.err.println("Failed to open the camera connection...");
			}
		} else {
			// the camera is NOT active at this moment.
			this.cameraActive = false;
			this.takePicButton.setDisable(true);

			// update again the button content
			this.cameraButton.setText("Start Camera");

			// enable classifiers checkboxes
			//this.haarClassifier.setDisable(false);
			//this.lbpClassifier.setDisable(false);

			// stop the timer
			this.stopAcquisition();
		}
	}

	// http://www.java2s.com/Tutorials/Java/JavaFX_How_to/Image/Save_an_Image_to_a_file.html
	// https://code.makery.ch/blog/javafx-dialogs-official/
	public static void saveImgToFile(Image imageToSave, boolean canwesave) {
		Alert errorAlert = new Alert(AlertType.ERROR);
		errorAlert.setTitle("Error Dialog");
		String folder = "resources/studentPics/";
		String extension = ".jpg";

		if (canwesave == false) {
			// savePicButton.setDisable(false);
			// Alert errorAlert = new Alert(AlertType.ERROR);
			// errorAlert.setTitle("Error Dialog");
			errorAlert.setHeaderText("No face detected.");
			errorAlert.setContentText(
					"Please position yourself so that the video feed captures your entire face. For more information check the guideless.");

			errorAlert.showAndWait();
			return;

		} else {
			String studentName = nameInputDialog();
			String fullfileName = folder + studentName + extension;

			// File outputFile = new File("resources/picture.jpg");
			File outputFile = new File(fullfileName);
			if (studentName.isEmpty()) {
				errorAlert.setHeaderText("Empty name introduced.");
				errorAlert.setContentText("Please introduce a valid name.");

				errorAlert.showAndWait();
				return;

			}
			// BufferedImage bImage = SwingFXUtils.fromFXImage(imageToSave, null);

			BufferedImage bImage = SwingFXUtils.fromFXImage(imageToSave, null);

			try {
				ImageIO.write(bImage, "png", outputFile);

				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("Information Dialog");
				alert.setHeaderText(null);
				alert.setContentText("Your picture has been succesfully saved!");

				alert.showAndWait();

			} catch (IOException e) {
				System.err.println(e.getMessage());
				System.out.println(e);
				StackTraceElement l = new Exception().getStackTrace()[0];
				System.out.println(l.getClassName() + "/" + l.getMethodName() + ":" + l.getLineNumber());
				// Alert errorAlert = new Alert(AlertType.ERROR);
				// errorAlert.setTitle("Error Dialog");
				errorAlert.setHeaderText("Error while saving image.");
				errorAlert.setContentText(
						"There appeares to be a problem while storing the picture. Please contact IT Support.");

				errorAlert.showAndWait();
				throw new RuntimeException(e);
			}
		}
	}

	/**
	 * The action triggered by pushing the button on the GUI.
	 */
	@FXML
	protected void takePicFromVideo() {
		this.takePicClicked = true;
	}

	/**
	 * Displays the logo when the checkbox is checked AND the Stream is ON.
	 * 
	 * This method LOADS the image whenever the 'logoCheckBox' is selected
	 * (checked). To load the image, we use the basic OpenCV function 'imread()'
	 * Which returns a Mat Object, and takes the path of the image, and a flag (> 0
	 * RGB, =0 grayscale, <0 with the alpha channel).
	 * 
	 * The 'imread()' method is supplied to get access to images through files.
	 * 
	 * Imgcodecs.imread (name of the file) and check whether dataAddr() from the
	 * read image is different from zero to make sure the image has been loaded
	 * correctly, that is, the filename has been typed correctly and its format is
	 * supported.
	 */
	@FXML
	protected void loadLogo() throws Exception {
		String logoFileName = "resources/ec-logo-imgs/rsz_endicott-logo.png";

		if (logoCheckBox.isSelected()) {

			// this.logo = Imgcodecs.imread("resources/Poli.png");
			// this.logo = Imgcodecs.imread("resources/redhead.jpg");
			this.logo = Imgcodecs.imread(logoFileName);
			if (this.logo.dataAddr() == 0) {
				throw new Exception("Couldn't open logo file " + logoFileName);
			}
		}
	}

	/*
	 * The Class Mat - Represents an n-dimensional dense numerical single-channel or
	 * multi-channel array. It can be used to store real or complex-valued vectors
	 * and matrices, grayscale or color images, voxel volumes, vector fields, point
	 * clouds, tensors, histograms.
	 */
	/**
	 * Get a Frame from the Opened Video Stream (if any). The frames of the video
	 * are just simple images. Thus, we need to extract them from the VideoCapture
	 * object, and put them insside a Mat object.
	 * 
	 * As the video streams are sequential, we get the frames 1 after another by the
	 * read() or the overloaded >> operator.
	 * 
	 * @return the {@link Image} to show
	 */
	private Mat grabFrame() {
		// initialize everything.
		// Mat matrix = new Mat();
		Mat frame = new Mat();
		
		/**
		 * .isOpened() -- Checks to see if the video stream is available or not. (check
		 * if the capture is open) i.e. checks whether it is instantiated.
		 * 
		 * Checks is the binding of the class to a video source (the camera in our case)
		 * was successful or not.
		 * 
		 * In case things go WRONG while using the read() method, that is, the camera
		 * gets disconnected, then the method returns FALSE.
		 * 
		 * Returns TRUE if video capturing has been initialized already. If the previous
		 * call to VideoCapture constructor or VideoCapture::open() succeeded, the
		 * method returns TRUE.
		 */
		if (this.capture.isOpened()) {
			try {

				/*
				 * .read() -- to retrieve EACH captured frame in a loop.
				 * 
				 * this method combines the VideoCapture grab() and retrieve() methods. - The
				 * grab() method only captures the next frame, which is fast, - The retrieve()
				 * method decodes and returns the captured frame.
				 * 
				 * read the current frame. getting each frame, one after the other, from the
				 * stream.
				 * 
				 */
				this.capture.read(frame);

				// if the frame is NOT EMPTY, process it
				if (!frame.empty()) {
					// face DETECTION
					//this.detectAndDisplay(frame);
					detectAndDisplay(frame);


					// add the logooooo
					if (logoCheckBox.isSelected() && this.logo != null) {

						Rect roi = new Rect(frame.cols() - logo.cols(), frame.rows() - logo.rows(), logo.cols(),
								logo.rows());

						Mat imageROI = frame.submat(roi);

						// add the logo: method #1
						Core.addWeighted(imageROI, 1.0, logo, 0.7, 0.0, imageROI);

						// add the logo: method #2
						// logo.copyTo(imageROI, logo);

					}
				}
			} catch (Exception e) {
				// log the (full) error
				System.err.println("Exception during the image elaboration: " + e);
				System.err.println(e.getMessage());
				System.out.println(e);
				StackTraceElement l = new Exception().getStackTrace()[0];
				System.out.println(l.getClassName() + "/" + l.getMethodName() + ":" + l.getLineNumber());
			}
		}

		return frame;
	}

	/**
	 * Method for FACE DETECTION and TRACKING. Implements the classifiers for the
	 * detection.
	 * 
	 * Imgproc.cvtColor(...) -- Converts/transforms the image (the frame) from BGR
	 * to Grayscale format. Converts an image from one color space to another. The
	 * function converts an input image from one color space to another. In case of
	 * a transformation to-from RGB color space, the order of the channels should be
	 * specified explicitly (RGB or BGR). Notethat the default color format in
	 * OpenCV is often referred to as RGB but it is actually BGR (thebytes are
	 * reversed)
	 * 
	 * @param frame it looks for faces in this frame
	 */
	private void detectAndDisplay(Mat frame) throws IOException{
		MatOfRect faces = new MatOfRect();
		Mat grayFrame = new Mat();

		int absoluteFaceSize = 0;
		CascadeClassifier faceCascade=new CascadeClassifier();

		faceCascade.load(classifierPath);
		/*
		 * playing around. when color, identifies faces, when gray, it does not.
		 * if(grayScale.isSelected()) {
		 * 
		 * Imgproc.cvtColor(frame, frame, Imgproc.COLOR_BGR2GRAY);
		 * Imgproc.equalizeHist(frame, frame);
		 * 
		 * } else { Imgproc.cvtColor(frame, grayFrame, Imgproc.COLOR_BGR2GRAY);
		 * Imgproc.equalizeHist(grayFrame, grayFrame); }
		 */

		/* Converts the frame in gray scale.
		 * This controls the number of channels displayed on the screen. converts the
		 * frame to gray scale format. The arguments are: a source image (frame) a
		 * destination image (frame) in which we will save the converted image. the type
		 * of transformation to perform.
		 */

		Imgproc.cvtColor(frame, grayFrame, Imgproc.COLOR_BGR2GRAY);

		// equalize the frame histogram to improve the result
		Imgproc.equalizeHist(grayFrame, grayFrame);

		/*
		 * Setting minimum size of the face to be detected. compute minimum face size
		 * (20% of the frame height)
		 * compute minimum face size (2% of the frame height, in our case)
		 */
		
			int height = grayFrame.rows();
			if (Math.round(height * 0.2f) > 0) {
				absoluteFaceSize = Math.round(height * 0.2f);
			}
		

		// THE FACE DETECTION STARTS
		// ------------------------------------------------------------------------------

		/*
		 * 'detectMultiScale' - Detects objects of different sizes in the input image. -
		 * returns: the detected objects as a List of Rectangles.
		 * 
		 * The result/output of the detection will be in the 'faces' argument given to
		 * the function.
		 */
		faceCascade.detectMultiScale(grayFrame, faces, 1.1, 2, 0 | Objdetect.CASCADE_SCALE_IMAGE,
				new Size(absoluteFaceSize, absoluteFaceSize), new Size(height,height));

		/*
		 * Putting the result in an Array of Rectangles. (each rectangle in 'faces' is a
		 * face)
		 */
		Rect[] facesArray = faces.toArray();
		//System.out.println("Number of faces detected = " + facesArray.length);

		/*
		 * Draw the Rects on the Frame, so we can display the detected faces. - color:
		 * Green with Transparent background. - '.tl()' & '.br()' --> top-left &
		 * bottom-right. Represent the 2 opposite vertexes. - '3': thickness of the
		 * rectangle's border.
		 */
		for (int i = 0; i < facesArray.length; i++)
			Imgproc.rectangle(frame, facesArray[i].tl(), facesArray[i].br(), new Scalar(0, 255, 0), 3);

		if (facesArray.length == 0) {
			this.isFaceDetected = false;
			 //System.out.println("facesArray.length should be 0 : "+ facesArray.length);
			return;
		}

		this.isFaceDetected = true;
		// System.out.println("facesArray.length there should be at least 1 face : "+ facesArray.length);

	}

	/**
	 * STOP the acquisition from the camera and release ALL the resources.
	 */
	private void stopAcquisition() {

		if (this.timer != null && !this.timer.isShutdown()) {
			try {

				// stop the timer
				this.timer.shutdown();
				this.timer.awaitTermination(33, TimeUnit.MILLISECONDS);
			} catch (InterruptedException e) {
				// log any exception
				System.err.println("Exception in stopping the frame capture, trying to release the camera now... " + e);
				System.err.println(e.getMessage());
				System.out.println(e);
				StackTraceElement l = new Exception().getStackTrace()[0];
				System.out.println(l.getClassName() + "/" + l.getMethodName() + ":" + l.getLineNumber());
			}
		}

		// Checks is the binding of the class to a video source (the camera in our case)
		// was successful or not.
		if (this.capture.isOpened()) {
			// release the camera
			// Although Closing the video is Automatic when the object's descructor is
			// called,
			// this method allows us to close it before.
			this.capture.release();
		}
	}

	/**
	 * Update the {@link ImageView} in the JavaFX main thread
	 * 
	 * @param view  the {@link ImageView} to update
	 * @param image the {@link Image} to show
	 */
	private void updateImageView(ImageView view, Image image) {
		Utils.onFXThread(view.imageProperty(), image);
	}

	/**
	 * On application close, STOP the acquisition from the camera.
	 */
	protected void setClosed() {

		this.stopAcquisition();
	}

	public static String nameInputDialog() {
		TextInputDialog dialog = new TextInputDialog("name");
		dialog.setTitle("Name Input Dialog");
		dialog.setHeaderText("Student Identification");
		dialog.setContentText("Please enter your name:");

		// Traditional way to get the response value.
		Optional<String> result = dialog.showAndWait();
		if (result.isPresent()) {
			// System.out.println("Your name: " + result.get());
			return result.get();
		}
		return "";
	}
	/**
	 * The action triggered by selecting the Haar Classifier checkbox. It loads the
	 * trained set to be used for frontal face detection.
	 */
	/*
	 * @FXML protected void haarSelected(Event event) { // check whether the lpb
	 * checkbox is selected and deselect it if (this.lbpClassifier.isSelected())
	 * this.lbpClassifier.setSelected(false);
	 * 
	 * // loading the HAAR Classifier from the resource folder
	 * this.checkboxSelection(
	 * "resources/haarcascades/haarcascade_frontalface_alt.xml"); }
	 * 
	 *//**
	 * The action triggered by selecting the LBP Classifier checkbox. It loads the
	 * trained set to be used for frontal face detection.
	 */
	/*
	 * @FXML protected void lbpSelected(Event event) { // check whether the haar
	 * checkbox is selected and deselect it if (this.haarClassifier.isSelected())
	 * this.haarClassifier.setSelected(false);
	 * 
	 * // loading the IBP Classifier from the resource folder
	 * this.checkboxSelection("resources/lbpcascades/lbpcascade_frontalface.xml"); }
	 * 
	 *//**
	 * Method for loading a classifier trained set from disk
	 * 
	 * @param classifierPath the path on disk where a classifier trained set is
	 *                       located
	 *//*
	 * private void checkboxSelection(String classifierPath) { // load the
	 * classifier this.faceCascade.load(classifierPath);
	 * 
	 * // Making the video capture start this.cameraButton.setDisable(false);
	 * this.takePicButton.setDisable(false); }
	 * 
	 */
}
