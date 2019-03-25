/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package parkingCar;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Arthur
 */
public class World {
    private int sizeX, sizeY;
    private List<Wall> wallList = new ArrayList<Wall>();
    private List<Car> carList = new ArrayList<Car>();
    
    public World(int sizeX, int sizeY, Car car) {
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        
        
    }

    public int getSizeX() {
        return sizeX;
    }

    public void setSizeX(int sizeX) {
        this.sizeX = sizeX;
    }

    public int getSizeY() {
        return sizeY;
    }

    public void setSizeY(int sizeY) {
        this.sizeY = sizeY;
    }

    public List<Car> getCarList() {
        return carList;
    }

    public void setCarList(List<Car> carList) {
        this.carList = carList;
    }
    

    public List<Wall> getWallList() {
        return wallList;
    }
    
}
