/**
 * 
 */
package views;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import static org.opencv.imgproc.Imgproc.*;
import static org.opencv.imgcodecs.Imgcodecs.imread;

import javax.imageio.ImageIO;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfInt;
import org.opencv.core.MatOfRect;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.face.FisherFaceRecognizer;
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
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.control.Label;
import javafx.event.ActionEvent;

/** 
 * We need to pre-load part of this scene. Specifically: the section name and faculty teaching it.
 * @author Eva Rubio
 *
 */
public class AttendanceTrackerViewController implements Initializable {
	@FXML private ImageView currentFrame;
	@FXML private Button cameraButton;
	@FXML private Button takePicButton;
	@FXML private ImageView picToShow;
	//@FXML private CheckBox haarClassifier;
	//@FXML private CheckBox lbpClassifier;
	@FXML private CheckBox grayScale;
	@FXML private CheckBox logoCheckBox;
	@FXML private Button savePicButton;
	// the school logo to be loaded
	private Mat logo;

	//public static String basePath=System.getProperty("user.dir");
	public static String classifierPath="resources/lbpcascades/lbpcascade_frontalface.xml";

	//public static String csvFilePath="resources/TrainingEva.txt";
	public static String csvFilePath="resources/TrainingEva.txt";
	//public String folder = "resources/studentPics/";
	//public String extension = ".jpg";



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
	@FXML Label dateLabel;
	@FXML Button uploadImageButton;
	private File imageFile;
	private boolean imageFileChanged;

	/**
	 * Sets the Minimum size/(area¿?) of the Face to be DETECTED. --> It is needed
	 * in the actual DETECTION Function. Later on, we set the minimum size to be the
	 * 20% of the frame's height. ^^Set in the method: 'detectAndDisplay()'
	 */
	//private int absoluteFaceSize;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		/* Instantiating the VideoCapture Object to Capture a Webcam Stream. 
		 * (using its constructor). 
		 * The Camera index is set to 1, representing the built-in camera.  */
		this.capture = new VideoCapture(1);

		// set a fixed width for the frame
		// currentFrame.setFitWidth(600);
		// preserve image ratio
		currentFrame.setPreserveRatio(true);

	}

	/**
	 * The action triggered by pushing the button on the GUI.
	 */
	@FXML protected void startCamera() {
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

							savePicButton.setOnAction(e -> saveImgToFile(imageToShow, isFaceDetected, frame));
							//saveImage(frame,"resources/studentPics/veamos.jpg");
							
							
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
	public static void saveImgToFile(Image imageToSave, boolean canwesave, Mat frame) {
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
			String fullfileNameGray = folder + studentName + "GRAY"+ extension;

			// File outputFile = new File("resources/picture.jpg");
			File outputFile = new File(fullfileName);
			//File outputGRAYFile = new File(fullfileNameGray);
			if (studentName.isEmpty()) {
				errorAlert.setHeaderText("Empty name introduced.");
				errorAlert.setContentText("Please introduce a valid name.");

				errorAlert.showAndWait();
				return;
			}
			
			// BufferedImage bImage = SwingFXUtils.fromFXImage(imageToSave, null);

			BufferedImage bImage = SwingFXUtils.fromFXImage(imageToSave, null);
			saveImage(frame, fullfileNameGray);
			/*
			 * // Creating the empty destination matrix Mat grayFrame = new Mat(); //
			 * Converting the image to gray scale and // saving it in the dst matrix
			 * Imgproc.cvtColor(frame, grayFrame, Imgproc.COLOR_RGB2GRAY); // Writing the
			 * image Imgcodecs.imwrite(fullfileNameGray, grayFrame);
			 * System.out.println("The image is successfully to Grayscale");
			 */
	        

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
	@FXML protected void loadLogo() throws Exception {
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
	/*
	 * Loads image from disk.
	 *  loads the given image as a Mat object, which is a matrix representation.
	 * https://www.baeldung.com/java-opencv
	 * */
	public static Mat loadImage(String imagePath) {
		Imgcodecs imageCodecs = new Imgcodecs();
		return imageCodecs.imread(imagePath);
	}

	/*
	 * Saves the previously loaded image, we can use the imwrite() method of the Imgcodecs class.
	 * 
	 * width= 92
	 * height= 112
	 * Expected 10304 pixels
	 * 
	 * https://www.baeldung.com/java-opencv
	 * */
	public static void saveImage(Mat imageMatrix, String targetPath) {
		Mat grayFrame = new Mat();
		Mat mat_resized= new Mat();
		// Get the height from the first image. We'll need this
	    // later in code to reshape the images to their original
	    // size AND we need to reshape incoming faces to this size:
		Size scaleSize = new Size(91,112);
		
		Imgproc.cvtColor(imageMatrix, grayFrame, Imgproc.COLOR_BGR2GRAY);
		//resize(faceIn, faceOut, Size(92, 112));
		// equalize the frame histogram to improve the result
		Imgproc.equalizeHist(grayFrame, grayFrame);
		resize(grayFrame, mat_resized,scaleSize, 0,0, INTER_LINEAR);
		Imgcodecs imgcodecs = new Imgcodecs();
		imgcodecs.imwrite(targetPath, mat_resized);
	}


	private void trainRecognitionModel() {
		/* The .csv TRAINING file is read, and two ArrayList(s) are created. 
		 * One for the matrix of images, and 
		 * another for their corresponding labels*/
		ArrayList<Mat> images = new ArrayList<>();
		ArrayList<Integer> labels = new ArrayList<>();
		Utils.readCSV(csvFilePath, images, labels);



		images.remove(images.size()-1);
		labels.remove(labels.size()-1);


		MatOfInt labelsMat = new MatOfInt();
		// converts the arrayList into a matrix of mat ints
		labelsMat.fromList(labels);

		FisherFaceRecognizer ffr = FisherFaceRecognizer.create(80);

		ffr.train(images, labelsMat);


		ffr.save("mytraineddata");
	}

	/**
	 * Gets the detected face and compares it to the TRAINING DATA SET.
	 *  
	 * @param detectedFace	the face that was just detected from the stream.
	 * 
	 * */
	public double[] recognition(Mat detectedFace) {

		int[] predictedLabel = new int[1];
		//The lower the value, the better the prediction.
		double[] outConf = new double[1];
		int result = -1;

		FisherFaceRecognizer ffr = FisherFaceRecognizer.create(80);
		ffr.read("mytraineddata");

		ffr.predict(detectedFace, predictedLabel, outConf);

		result = predictedLabel[0];


		System.out.println("***Predicted label is " + predictedLabel[0] + ".***");

		System.out.println("***Actual label is " + detectedFace + ".***");
		System.out.println("***Confidence value is "+ outConf[0] + ".***");

		return new double[] {result, outConf[0]};

	}

	@FXML public void uploadImageButtonPushed(ActionEvent event) {
		//get the Stage to open a new window/Stage    --> this gives us access to the actual window.
				Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();

				//Instantiate a FileChooser object
				FileChooser fileChooser = new FileChooser();
				fileChooser.setTitle("Open Image");

				/*
				 * To make sure the user only selects the correct image format we
				 *  filter to only select be allowed to select .jpg and .png formats*/
				FileChooser.ExtensionFilter jpgFilter = new FileChooser.ExtensionFilter("image File (*.jpg)", "*.jpg");
				FileChooser.ExtensionFilter pngFilter = new FileChooser.ExtensionFilter("image File (*.png)", "*.png");
				// the list is initially empty so we add them all to it. 
				fileChooser.getExtensionFilters().addAll(jpgFilter, pngFilter);

				//Set to the user's picture directory or user directory if not available
				String userDirectoryString = System.getProperty("user.home")+"\\Pictures";
				File userDirectory = new File(userDirectoryString);

				//if you cannot navigate to the pictures directory, go to the user home
				if (!userDirectory.canRead())
					userDirectory = new File(System.getProperty("user.home"));

				fileChooser.setInitialDirectory(userDirectory);

				//open the file dialog window - giving the user the ability to select their desired one. 
				File tmpImageFile = fileChooser.showOpenDialog(stage);

				if (tmpImageFile != null) {
					imageFile = tmpImageFile;

					//update the ImageView with the new user-selected image
					if (imageFile.isFile()) {

						try {

							BufferedImage bufferedImage = ImageIO.read(imageFile);
							Image img = SwingFXUtils.toFXImage(bufferedImage, null);
							picToShow.setImage(img);

							imageFileChanged = true;
							this.takePicClicked = true;

						} catch (IOException e) {

							System.err.println(e.getMessage());
							System.out.println(e);
							StackTraceElement l = new Exception().getStackTrace()[0];
							System.out.println(l.getClassName()+"/"+l.getMethodName()+":"+l.getLineNumber());
						}
					}
				}
	}

}
