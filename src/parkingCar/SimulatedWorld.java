/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package parkingCar;

import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;

/**
 *
 * @author Arthur
 */
public class SimulatedWorld extends World {

    
    private Group parent;
    private int parkX;
    private int parkY;

    private Circle range;
    private Line linePark;
    private Line linePark2;
    private Line linePark3;
    
    Line line1;
    Line line2;
    Line line3;
    Line line4;

    private int radius = 3;

   

    public Group getParent() {
        return parent;
    }

    public void setParent(Group parent) {
        this.parent = parent;
    }

    public int getParkX() {
        return parkX;
    }

    public void setParkX(int parkX) {
        this.parkX = parkX;
    }

    public int getParkY() {
        return parkY;
    }

    public void setParkY(int parkY) {
        this.parkY = parkY;
    }

    public Circle getRange() {
        return range;
    }

    public void setRange(Circle range) {
        this.range = range;
    }

    public Line getLine() {
        return linePark;
    }

    public void setLine(Line line) {
        this.linePark = line;
    }

    public Line getLine2() {
        return linePark2;
    }

    public void setLine2(Line line2) {
        this.linePark2 = line2;
    }

    public Line getLine3() {
        return linePark3;
    }

    public void setLine3(Line line3) {
        this.linePark3 = line3;
    }

    public SimulatedWorld(int sizeX, int sizeY, Car car, Group parent) {
        super(sizeX, sizeY, car);

        this.parkX = (500);
        this.parkY = (0);
        this.parent = parent;
        
        ImageView background = new ImageView();
        background.setImage(new Image(getClass().getResourceAsStream("fundo.jpg")));
        parent.getChildren().add(background);

        range = new Circle();
        range.setCenterX(parkX);
        range.setCenterY(parkY);
        range.setFill(Color.BLACK);
        range.setRadius(radius);

        parent.getChildren().add(range);

        linePark = new Line(parkX - 20, parkY, parkX - 20, parkY + 40);
        linePark.setStroke(Color.YELLOW);
        linePark.setFill(Color.YELLOW);

        parent.getChildren().add(linePark);

        linePark3 = new Line(parkX - 20, parkY, parkX + 20, parkY);
        linePark3.setStroke(Color.YELLOW);
        linePark3.setFill(Color.YELLOW);
        parent.getChildren().add(linePark3);

        linePark2 = new Line(parkX + 20, parkY, parkX + 20, parkY + 40);
        linePark2.setStroke(Color.YELLOW);
        linePark2.setFill(Color.YELLOW);
        parent.getChildren().add(linePark2);
        
        
    }

    void addWall(int x1, int y1, int x2, int y2) {
        Wall wall = new Wall(x1, y1, x2, y2);
        getWallList().add(wall);
    }

    void addCar(Car car) {
        getCarList().add(car);
    }

}
