package com.rocketsoftware.facein.sdk;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.WritableRaster;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.URL;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import javafx.application.Platform;

import javax.imageio.ImageIO;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.HttpClientBuilder;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
import org.opencv.objdetect.Objdetect;
import org.opencv.videoio.VideoCapture;

import com.rocketsoftware.facein.ocrsdk.OCR;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.scene.control.Alert;

/**
 * 
 * @author chens
 *
 */
public class FaceDetectionController {

	private final String USER_AGENT = "Mozilla/5.0";
	
	@FXML
	private Label txtPassportNo;
	
	// FXML buttons
	@FXML
	private Button cameraButton;

	@FXML
	private Button clippingButton; // Compare faces button
	// the FXML area for showing the current frame
	@FXML
	private ImageView originalFrame;
	// checkboxes for enabling/disabling a classifier
	@FXML
	private CheckBox haarClassifier;
	@FXML
	private CheckBox lbpClassifier;

	// a timer for acquiring the video stream
	private ScheduledExecutorService timer;
	// the OpenCV object that performs the video capture
	private VideoCapture capture;
	// a flag to change the button behavior
	private boolean cameraActive;

	// face cascade classifier
	private CascadeClassifier faceCascade;
	private int absoluteFaceSize;
	
	private Mat frame = null;
	private Rect[] facesArray = null;
	private String passportNo = "";
	private InputStream is = null;
	private String url = "http://localhost:8090/api/v1/files/";
	private CascadeClassifier mEyeDetector = new CascadeClassifier("C:\\Users\\chens\\Documents\\development\\rocket\\RocketBuild2019\\facein\\facein-sdk\\resources\\haarcascades\\haarcascade_eye_tree_eyeglasses.xml");
	private Scalar EYE_RECT_COLOR = new Scalar(255, 123, 12);
	
	/**
	 * Init the controller, at start time
	 */
	protected void init() {
		this.capture = new VideoCapture();
		this.faceCascade = new CascadeClassifier();
		this.absoluteFaceSize = 0;

		// set a fixed width for the frame
		originalFrame.setFitWidth(600);
		// preserve image ratio
		originalFrame.setPreserveRatio(true);
	}

	private void putFile(InputStream is, String fileName) throws ClientProtocolException, IOException {
		// make sure cookies is turn on
		CookieHandler.setDefault(new CookieManager());
		HttpClientBuilder builder = HttpClientBuilder.create();
		HttpClient httpclient = builder.build();
		HttpPost httppost = new HttpPost(url+passportNo);
		// add header
		httppost.setHeader("User-Agent", USER_AGENT);
//		File file = new File("C:/Demo/temp/aa.png");
		MultipartEntityBuilder meb = MultipartEntityBuilder.create();
//		meb.addBinaryBody("file", file, ContentType.MULTIPART_FORM_DATA, file.getName());
		meb.addBinaryBody("file", is, ContentType.MULTIPART_FORM_DATA, fileName);
		
		
		HttpEntity entity = meb.build();
		httppost.setEntity(entity);
		System.out.println("executing request " + httppost.getRequestLine());

		HttpResponse response = httpclient.execute(httppost);
		System.out.println("Response Code : " + response.getStatusLine().getStatusCode());
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		StringBuffer result = new StringBuffer();
		String line = "";
		while ((line = rd.readLine()) != null) {
			result.append(line);
		}
		System.out.println(result.toString());

//		restartCamera();

		showAlert(result.toString());
	}

	private void restartCamera() {
		Platform.runLater(new Runnable() {
			public void run() {
				startCamera();
			}
		});
	}

	/**
	 * Calling from main thread:
	 */
	public void showAlert(String result) {
//	    Alert alert = new Alert(Alert.AlertType.INFORMATION);
//	    alert.setTitle("Message Here...");
//	    alert.setHeaderText("Comparison results");
//	    alert.setContentText(result);
//	    alert.showAndWait();
		
		
		Callback callback = new Callback() {

			@Override
			public void call() {
				restartCamera();
			}
		};
		
		Platform.runLater(new Runnable() {
			public void run() {

				Alert alert = new Alert(Alert.AlertType.INFORMATION);
				alert.setTitle("Message...");
				alert.setHeaderText("Look, face matching results:");
				alert.setContentText(result);
				alert.showAndWait();
				
				callback.call();
				
			}
		});
	}

	@FXML
	public void choose() {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Open Resource File");
		fileChooser.getExtensionFilters().addAll(
				new ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif"),
				new ExtensionFilter("All Files", "*.*"));
		File selectedFile = fileChooser.showOpenDialog(null);
		if (selectedFile != null) {
			
			Callback callback = new Callback() {

				@Override
				public void call() {
					OCR ocrsdk = new OCR();
					FileInputStream fis;
					try {
						fis = new FileInputStream(selectedFile);
						passportNo = ocrsdk.passportNo(fis);
						fis.close();
					} catch (FileNotFoundException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
					Platform.runLater(new Runnable(){

						@Override
						public void run() {
							txtPassportNo.setText(passportNo);
						}
					});
					System.out.println("passportNo="+passportNo);
				}
			};
			
			ProgressDialog pd = new ProgressDialog("Analyze passport information");
			pd.run(callback);
		}
	}

	@FXML
	public void clipping() {
		if(passportNo.isEmpty()) {
			Alert alert = new Alert(Alert.AlertType.WARNING);
			alert.setTitle("Message Here...");
			alert.setHeaderText("");
			alert.setContentText("No passportNo found!");
			alert.showAndWait();
			return;
		}

		// grab a frame every 33 ms (30 frames/sec)
		Runnable frameGrabber = new Runnable() {

			@Override
			public void run() {
				// effectively grab and process a single frame
				// Reading the next video frame from the camera
//				Mat matrix = new Mat();
				capture.read(frame);

				// If camera is opened
				if (capture.isOpened()) {

					// If there is next video frame
					if (capture.read(frame)) {

						// Creating BuffredImage from the matrix
						BufferedImage image = new BufferedImage(frame.width(), frame.height(),
								BufferedImage.TYPE_3BYTE_BGR);
						WritableRaster raster = image.getRaster();
						DataBufferByte dataBuffer = (DataBufferByte) raster.getDataBuffer();
						byte[] data = dataBuffer.getData();
						frame.get(0, 0, data);
						boolean catched = false;
						
						for (int i = 0; i < facesArray.length; i++) {
//							BufferedImage subImage = image.getSubimage(facesArray[i].x, facesArray[i].y,
//									facesArray[i].width, facesArray[i].height);
							int x = (int) p1.x;
							int y = (int) p1.y;
							int w = (int) (p2.x - p1.x);
							int h = (int) (p2.y - p1.y);
							BufferedImage subImage = image.getSubimage(x, y, w, h);
							
							ByteArrayOutputStream output = new ByteArrayOutputStream();  
							try {
								ImageIO.write(subImage, "png", output);
							} catch (IOException e) {
								e.printStackTrace();
							}
							is = new ByteArrayInputStream(output.toByteArray());  
							catched = true;
							System.out.println("Done!!!!");
							break;
						}
						if (catched) {
							// Stop camera
							// the camera is not active at this point
							cameraActive = false;
							// update again the button content
							// stop the timer
							stopAcquisition();

							Callback callback = new Callback() {

								@Override
								public void call() {
									try {
										putFile(is, "aa.png");
									} catch (ClientProtocolException e) {
										e.printStackTrace();
									} catch (IOException e) {
										e.printStackTrace();
									}
								}
							};
							ProgressDialog p = new ProgressDialog("Comparing two faces...");
							p.run(callback);

						} else {
							Alert alert = new Alert(Alert.AlertType.WARNING);
							alert.setTitle("Message Here...");
							alert.setHeaderText("");
							alert.setContentText("No face detected!");
							alert.showAndWait();
						}

					}
				}
			}
		};
		frameGrabber.run();

	}

	/**
	 * The action triggered by pushing the button on the GUI
	 */
	@FXML
	protected void startCamera() {
		if (!this.cameraActive) {
			// disable setting checkboxes
			this.haarClassifier.setDisable(true);
			this.lbpClassifier.setDisable(true);
			// update the button content
			cameraButton.setText("Stop Camera");
			ProgressDialog progress = new ProgressDialog("Starting the camera...");

			Callback callback = new Callback() {

				@Override
				public void call() {
					// start the video capture
					capture.open(0);

					// is the video stream available?
					if (capture.isOpened()) {
						cameraActive = true;

						// grab a frame every 33 ms (30 frames/sec)
						Runnable frameGrabber = new Runnable() {

							@Override
							public void run() {
								// effectively grab and process a single frame
								frame = grabFrame();
								// convert and show the frame
								Image imageToShow = Utils.mat2Image(frame);
								updateImageView(originalFrame, imageToShow);
							}
						};

						timer = Executors.newSingleThreadScheduledExecutor();
						timer.scheduleAtFixedRate(frameGrabber, 0, 33, TimeUnit.MILLISECONDS);

					} else {
						// log the error
						System.err.println("Failed to open the camera connection...");
					}
				}
			};

			progress.run(callback);

		} else {
			// the camera is not active at this point
			this.cameraActive = false;
			// update again the button content
			this.cameraButton.setText("Start Camera");
			// enable classifiers checkboxes
			this.haarClassifier.setDisable(false);
			this.lbpClassifier.setDisable(false);

			// stop the timer
			this.stopAcquisition();
		}
	}

	/**
	 * Get a frame from the opened video stream (if any)
	 * 
	 * @return the {@link Image} to show
	 */
	private Mat grabFrame() {
		Mat frame = new Mat();

		// check if the capture is open
		if (this.capture.isOpened()) {
			try {
				// read the current frame
				this.capture.read(frame);

				// if the frame is not empty, process it
				if (!frame.empty()) {
					// face detection
					this.detectAndDisplay(frame);
				}

			} catch (Exception e) {
				// log the (full) error
				System.err.println("Exception during the image elaboration: " + e);
			}
		}

		return frame;
	}
	
	Point p1 = new Point();
	Point p2 = new Point();

	/**
	 * Method for face detection and tracking
	 * 
	 * @param frame it looks for faces in this frame
	 */
	private void detectAndDisplay(Mat frame) {
		MatOfRect faces = new MatOfRect();
		Mat grayFrame = new Mat();

		// convert the frame in gray scale
		Imgproc.cvtColor(frame, grayFrame, Imgproc.COLOR_BGR2GRAY);
		// equalize the frame histogram to improve the result
		Imgproc.equalizeHist(grayFrame, grayFrame);

		// compute minimum face size (20% of the frame height, in our case)
		if (this.absoluteFaceSize == 0) {
			int height = grayFrame.rows();
			if (Math.round(height * 0.2f) > 0) {
				this.absoluteFaceSize = Math.round(height * 0.2f);
			}
		}

		// detect faces
		this.faceCascade.detectMultiScale(grayFrame, faces, 1.1, 2, 0 | Objdetect.CASCADE_SCALE_IMAGE,
				new Size(this.absoluteFaceSize, this.absoluteFaceSize), new Size());
		
		// each rectangle in faces is a face: draw them!
		// draw green rectangle
		facesArray = faces.toArray();
		for (int i = 0; i < facesArray.length; i++) {
			p1.x = facesArray[i].tl().x - 33;
			p1.y = facesArray[i].tl().y - 33;
			p2.x = facesArray[i].br().x + 33;
			p2.y = facesArray[i].br().y + 33;
			
//			Imgproc.rectangle(frame, facesArray[i].tl(), facesArray[i].br(), new Scalar(0, 255, 0), 3);
			Imgproc.rectangle(frame, p1, p2, new Scalar(0, 255, 0), 3);
		}
		
		// 识别眼睛
//		MatOfRect eyes = new MatOfRect();
//		if (facesArray.length > 0) {
//			Rect roi = new Rect((int) facesArray[0].tl().x, (int) (facesArray[0].tl().y), facesArray[0].width,
//					(int) (facesArray[0].height));// imran
//			// taking inputs from nustrat opencv example
//			// imran check above, using tl of x and tl of y.other wise it will give runtime
//			// errors
//			Mat cropped = new Mat();
//			// cropped = mGray.submat(facesArray[0]);//imran yuppie!, this did the
//			// trick!...everything else was failing
//			// refer to opencv 2.4 tut pdf
//			cropped = grayFrame.submat(roi);
//			// cropped.copyTo(mGray.submat(roi));
//			if (mEyeDetector != null) {
//				mEyeDetector.detectMultiScale(cropped, eyes, 1.15, 2, Objdetect.CASCADE_FIND_BIGGEST_OBJECT | Objdetect.CASCADE_SCALE_IMAGE, new Size(30, 30),new Size());
//			}
//		} else {
//			System.out.println("mEyeDetector is NULL");
//		}
//		Rect[] eyesArray;
//		eyesArray = eyes.toArray();
//		System.out.println("Eyes Count：" + eyesArray.length);
//		Point x1 = new Point();
//		for (int i = 0; i < eyesArray.length; i++) {
//			x1.x = facesArray[0].x + eyesArray[i].x + eyesArray[i].width * 0.5;
//			x1.y = facesArray[0].y + eyesArray[i].y + eyesArray[i].height * 0.5;
//			
//			int Radius = (int) ((eyesArray[i].width + eyesArray[i].height) * 0.25);
//			Imgproc.circle(frame, x1, Radius, EYE_RECT_COLOR, 3);
//		}
		
		this.frame = frame;
	}
	
	double oldX = 0;

	/**
	 * The action triggered by selecting the Haar Classifier checkbox. It loads the
	 * trained set to be used for frontal face detection.
	 */
	@FXML
	protected void haarSelected(Event event) {
		// check whether the lpb checkbox is selected and deselect it
		if (this.lbpClassifier.isSelected())
			this.lbpClassifier.setSelected(false);
//		this.checkboxSelection("resources/haarcascades/haarcascade_frontalface_alt.xml");
		this.checkboxSelection("C:\\Users\\chens\\Documents\\development\\rocket\\RocketBuild2019\\facein\\facein-sdk\\resources\\haarcascades\\haarcascade_frontalface_alt.xml");
	}

	/**
	 * The action triggered by selecting the LBP Classifier checkbox. It loads the
	 * trained set to be used for frontal face detection.
	 */
	@FXML
	protected void lbpSelected(Event event) {
		// check whether the haar checkbox is selected and deselect it
		if (this.haarClassifier.isSelected())
			this.haarClassifier.setSelected(false);
//		this.checkboxSelection("resources/lbpcascades/lbpcascade_frontalface.xml");
		this.checkboxSelection("C:\\Users\\chens\\Documents\\development\\rocket\\RocketBuild2019\\facein\\facein-sdk\\resources\\lbpcascades\\lbpcascade_frontalface.xml");
	}

	/**
	 * Method for loading a classifier trained set from disk
	 * 
	 * @param classifierPath the path on disk where a classifier trained set is
	 *                       located
	 */
	private void checkboxSelection(String classifierPath) {
		// load the classifier(s)
		this.faceCascade.load(classifierPath);

		// now the video capture can start
		this.cameraButton.setDisable(false);
		this.clippingButton.setDisable(false);
	}

	/**
	 * Stop the acquisition from the camera and release all the resources
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
			}
		}

		if (this.capture.isOpened()) {
			// release the camera
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
	 * On application close, stop the acquisition from the camera
	 */
	protected void setClosed() {
		this.stopAcquisition();
	}

}
