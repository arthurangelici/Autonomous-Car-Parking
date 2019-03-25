package parkingCar;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.LinkedList;
import javafx.animation.AnimationTimer;
import javafx.animation.RotateTransition;
import javafx.scene.Group;
import javafx.scene.input.KeyCode;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Arthur
 */
public class SimulatedCar extends Car {

    private Group parent;
    private MultiplePressedKeysEventHandler keyHandler;
    LinkedList replay = new LinkedList();
    Graphics g;
    Line line1;
    Line line2;
    Line line3;
    Line line4;
    int i =0;

    private int xa, xb, xc, xd, ya, yb, yc, yd;

    public SimulatedCar(Group group, double x, double y, double w, double r) {

        super(x, y, w, r);
        
        this.parent = group;
        keyHandler = new MultiplePressedKeysEventHandler();

        layout();
        line1 = new Line(xa, ya, xb, yb);
        line2 = new Line(xa, ya, xc, yc);
        line3 = new Line(xc, yc, xd, yd);
        line4 = new Line(xb, yb, xd, yd);
        line1.setFocusTraversable(true);
        line1.setOnKeyPressed(keyHandler);
        line1.setOnKeyReleased(keyHandler);
        line2.setFocusTraversable(true);
        line2.setOnKeyPressed(keyHandler);
        line2.setOnKeyReleased(keyHandler);
        line3.setFocusTraversable(true);
        line3.setOnKeyPressed(keyHandler);
        line3.setOnKeyReleased(keyHandler);
        line4.setFocusTraversable(true);
        line4.setOnKeyPressed(keyHandler);
        line4.setOnKeyReleased(keyHandler);

        parent.getChildren().add(line1);
        parent.getChildren().add(line2);
        parent.getChildren().add(line3);
        parent.getChildren().add(line4);

        new AnimationTimer() {
            public void handle(long currentNanoTime) {
                handleKeysPressed();
                //System.out.println(Math.toDegrees(getPhi()));

                layout();
                group.getChildren().remove(line1);
                group.getChildren().remove(line2);
                group.getChildren().remove(line3);
                group.getChildren().remove(line4);
                line1 = new Line(xa, ya, xb, yb);
                line2 = new Line(xa, ya, xc, yc);
                line3 = new Line(xc, yc, xd, yd);
                line4 = new Line(xb, yb, xd, yd);

                line1.setFocusTraversable(true);
                line1.setOnKeyPressed(keyHandler);
                line1.setOnKeyReleased(keyHandler);
                line2.setFocusTraversable(true);
                line2.setOnKeyPressed(keyHandler);
                line2.setOnKeyReleased(keyHandler);
                line3.setFocusTraversable(true);
                line3.setOnKeyPressed(keyHandler);
                line3.setOnKeyReleased(keyHandler);
                line4.setFocusTraversable(true);
                line4.setOnKeyPressed(keyHandler);
                line4.setOnKeyReleased(keyHandler);
                parent.getChildren().add(line1);
                parent.getChildren().add(line2);
                parent.getChildren().add(line3);
                parent.getChildren().add(line4);
                
                addPoints();
                showReplay();
                
                

            }
        }.start();
    }

    @Override
    void move(Direction direction) {

        switch (direction) {
            case UP: {
                this.setPhi(this.getPhi() + this.getTheta());
                this.setX(this.getX() - (1 * Math.cos(this.getPhi())));
                this.setY(this.getY() + (1 * Math.sin(this.getPhi())));
                break;
            }
            case DOWN: {
                this.setPhi(this.getPhi() + this.getTheta());
                this.setX(this.getX() + (1 * Math.cos(this.getPhi())));
                this.setY(this.getY() - (1 * Math.sin(this.getPhi())));
                break;

            }
            case LEFT: {
                if (this.getTheta() <= Math.toRadians(30)) {

                    this.setTheta(this.getTheta() + 1);

                    System.out.println(getTheta());

                }
                break;
            }
            case RIGHT: {
                if (this.getTheta() >= Math.toRadians(-30)) {
                    this.setTheta(this.getTheta() - 1);

                }
                break;
            }
        }

    }
    
    public void handleKeysPressed() {
        if (keyHandler.isPressed(KeyCode.LEFT)) {
            move(Direction.LEFT);

        }

        if (keyHandler.isPressed(KeyCode.RIGHT)) {
            move(Direction.RIGHT);

        }

        if (keyHandler.isPressed(KeyCode.UP)) {
            move(Direction.UP);

        }

        if (keyHandler.isPressed(KeyCode.DOWN)) {
            move(Direction.DOWN);

        }
    }
    
    public void addPoints(){
        ArrayList carPoints = new ArrayList<>();
        
        carPoints.add(xa);
        carPoints.add(xb);
        carPoints.add(xc);
        carPoints.add(xd);
        
        carPoints.add(ya);
        carPoints.add(yb);
        carPoints.add(yc);
        carPoints.add(yd);
          
        replay.add(carPoints);
        
    }
    
    @Override
    public void showReplay(){
       
        int xA, xB, xC, xD, yA, yB, yC, yD;
        
        for(Object e: replay) {
            ArrayList it  = (ArrayList) e;
        
            
                System.out.println("oi");
                
                xA =(int) it.get(0);
                xB =(int) it.get(1);
                xC =(int) it.get(2);
                xD =(int) it.get(3);
                yA =(int) it.get(4);
                yB =(int) it.get(5);
                yC =(int) it.get(6);
                yD =(int) it.get(7);
                
                
                parent.getChildren().add(new Line(xA, yA, xB, yB));
                parent.getChildren().add(new Line(xA, yA, xC, yC));
                parent.getChildren().add(new Line(xC, yC, xD, yD));
                parent.getChildren().add(new Line(xB, yB, xD, yD));
            
            
        }
        
    }

    public void layout() {

        xd = (int) (getX() + getR() * Math.cos(Math.PI - getPhi()) + ((getW() / 2) * Math.cos((Math.PI / 2) - getPhi())));
        yd = (int) (getY() + getR() * Math.sin(Math.PI - getPhi()) + ((getW() / 2) * Math.sin((Math.PI / 2) - getPhi())));
        xc = (int) (getX() + getR() * Math.cos(Math.PI - getPhi()) - ((getW() / 2) * Math.cos((Math.PI / 2) - getPhi())));
        yc = (int) (getY() + getR() * Math.sin(Math.PI - getPhi()) - ((getW() / 2) * Math.sin((Math.PI / 2) - getPhi())));
        xb = (int) (getX() + ((getW() / 2) * Math.cos((Math.PI / 2) - getPhi())));
        yb = (int) (getY() + ((getW() / 2) * Math.sin((Math.PI / 2) - getPhi())));
        xa = (int) (getX() - (getW() / 2) * Math.cos((Math.PI / 2) - getPhi()));
        ya = (int) (getY() - (getW() / 2) * Math.sin((Math.PI / 2) - getPhi()));

    }

}
