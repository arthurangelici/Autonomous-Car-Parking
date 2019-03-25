package parkingCar;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Arthur
 */
public abstract class Agent {
    
    private Car car;
    
    public abstract void drive();
 

    public Car getCar() {
        return car;
    }

    public void setCar(Car car) {
        this.car = car;
    }
    
    
    
}