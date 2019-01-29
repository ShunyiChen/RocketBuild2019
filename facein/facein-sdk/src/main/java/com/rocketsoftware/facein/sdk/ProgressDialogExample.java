package com.rocketsoftware.facein.sdk;

import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.geometry.Insets;
import javafx.scene.paint.Color;

import javafx.application.Application;
import javafx.concurrent.Task;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * 
 * @author chens
 *
 */
public class ProgressDialogExample extends Application {
	
	private String initMessage;
	
	public ProgressDialogExample() {
		this("");
	}
	
	public ProgressDialogExample(String initMessage) {
		this.initMessage = initMessage;
	}

    @Override
    public void start(Stage primaryStage) {
//        Button startButton = new Button("Start");
//        startButton.setOnAction(e -> {
//            ProgressForm pForm = new ProgressForm();
//
//            // In real life this task would do something useful and return 
//            // some meaningful result:
//            Task<Void> task = new Task<Void>() {
//                @Override
//                public Void call() throws InterruptedException {
////                    for (int i = 0; i < 10; i++) {
////                        updateProgress(i, 10);
////                        Thread.sleep(200);
////                    }
////                    updateProgress(10, 10);
//                	
////                	Thread.sleep(12000);
//                	
//                    return null ;
//                }
//            };
//
//            // binds progress of progress bars to progress of task:
//            pForm.activateProgressBar(task);
//
//            // in real life this method would get the result of the task
//            // and update the UI based on its value:
//            task.setOnSucceeded(event -> {
//                pForm.getDialogStage().close();
//                startButton.setDisable(false);
//            });
//
//            startButton.setDisable(true);
//            pForm.getDialogStage().show();
//
//            Thread thread = new Thread(task);
//            thread.start();
//        });
//
//        StackPane root = new StackPane(startButton);
//        Scene scene = new Scene(root, 350, 75);
//        primaryStage.setScene(scene);
//        primaryStage.show();
    }

    
    public void run(Callback callback) {
    	ProgressForm pForm = new ProgressForm(initMessage);

        // In real life this task would do something useful and return 
        // some meaningful result:
        Task<Void> task = new Task<Void>() {
            @Override
            public Void call() throws InterruptedException {
            	
            	callback.call();
            	
                return null ;
            }
        };

        // binds progress of progress bars to progress of task:
        pForm.activateProgressBar(task);

        // in real life this method would get the result of the task
        // and update the UI based on its value:
        task.setOnSucceeded(event -> {
            pForm.getDialogStage().close();
        });

        pForm.getDialogStage().show();

        Thread thread = new Thread(task);
        thread.start();
    }
    
    public static class ProgressForm {
        private final Stage dialogStage;
        private final ProgressBar pb = new ProgressBar();
        private final ProgressIndicator pin = new ProgressIndicator();

        private String msg;
        
        public ProgressForm(String msg) {
        	this.msg = msg;
            dialogStage = new Stage();
            dialogStage.setWidth(500);
            dialogStage.setHeight(70);
            dialogStage.initStyle(StageStyle.TRANSPARENT);
            dialogStage.setResizable(false);
            dialogStage.initModality(Modality.APPLICATION_MODAL);

            // PROGRESS BAR
            final Label label = new Label();
            label.setText("alerto");

//            pb.setProgress(-1F);
//            pin.setProgress(-1F);
            pb.setPrefWidth(500);
//            final HBox hb = new HBox();
            
            Label msgLabel = new Label();
            msgLabel.setText(msg);
            msgLabel.setPadding(new Insets(45,5,5,5));
//            new Insets(5,5,5,5);
            
            
            StackPane hb = new StackPane();
            final String cssDefault = "-fx-border-color: rgb(0,150,201);\n"
                    + "-fx-border-insets: 0;\n"
                    + "-fx-border-width: 1;\n"
                    + "-fx-background-color: rgba(255, 255, 255, 0.8);\n";
            hb.setStyle(cssDefault);
//            hb.setSpacing(5);
            hb.setAlignment(Pos.CENTER);
            hb.getChildren().addAll(pb, msgLabel);
            
            Scene scene = new Scene(hb);
            scene.setFill(Color.TRANSPARENT);
            dialogStage.setScene(scene);
        }

        public void activateProgressBar(final Task<?> task)  {
            pb.progressProperty().bind(task.progressProperty());
            pin.progressProperty().bind(task.progressProperty());
            dialogStage.show();
        }

        public Stage getDialogStage() {
            return dialogStage;
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}