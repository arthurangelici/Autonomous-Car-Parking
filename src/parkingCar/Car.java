package parkingCar;

import javafx.scene.Group;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Arthur
 */
public abstract class Car {
    
    private double r;
    private double x;
    private double y;
    private double w;
    private double phi = Math.toRadians(-90);
    private double theta = 0;
    private boolean parked = false;
    
    
    abstract void move(Direction direction);
    abstract void showReplay();

    public Car(double x, double y, double w, double r) {
        this.x = x;
        this.y = y;
        this.r = r;
        this.w = w;
        
    }

    
    public double getW() {
        return w;
    }

    public void setW(double w) {
        this.w = w;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getR() {
        return r;
    }

    public void setR(double r) {
        this.r = r;
    }

    public double getPhi() {
        return phi;
    }

    public void setPhi(double phi) {
        this.phi = phi;
    }

    public double getTheta() {
        return theta;
    }

    public void setTheta(double theta) {
        this.theta = Math.toRadians(theta);
    }

    public boolean isParked() {
        return parked;
    }

    public void setParked(boolean parked) {
        this.parked = parked;
    }

   
    
    
}
