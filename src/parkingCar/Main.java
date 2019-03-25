/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package parkingCar;

import java.io.IOException;
import java.util.Random;
import javafx.animation.RotateTransition;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 *
 * @author Arthur
 */
public class Main extends Application {
    
    private int WINDOW_WIDTH = 1000;
    private int WINDOW_HEIGHT = 1000;
    private Group root = new Group();
    private Stage primaryStage;
    private Random random = new Random();

    private Timeline level_timeline;
    
    @Override
    public void start(Stage primaryStage) throws IOException, InterruptedException {
        
      primaryStage.setResizable(false);
        Scene scene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);

        this.primaryStage = primaryStage;
        primaryStage.setScene(scene);
        primaryStage.show();

        starter();
    }

    
    private void starter() throws IOException, InterruptedException {
         startGame();
    }
    
     private void startGame() throws IOException, InterruptedException
    {
        
       HumanAgent humanAgent = new HumanAgent();
       SimulatedCar simCar = new SimulatedCar(root, 500,300, 30, 50);
       SimulatedWorld simWorl = new SimulatedWorld(1000, 1000, simCar, root);
      
       
       
       
        NeuralAgent neuralAgent = new NeuralAgent(3,simCar);
        //FuzzyAgent fuzzyAgent = new FuzzyAgent(simCar);
        //Thread thread = new Thread(fuzzyAgent);
        //thread.start();
    
//        System.out.println("Training starting");
           neuralAgent.trainNetwork();
//
//        System.out.println("Testing network");
            neuralAgent.testNetwork();
      
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

    
}
