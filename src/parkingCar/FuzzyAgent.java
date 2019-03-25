/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package parkingCar;

import java.io.IOException;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.sourceforge.jFuzzyLogic.FIS;
import net.sourceforge.jFuzzyLogic.FunctionBlock;
import net.sourceforge.jFuzzyLogic.plot.JFuzzyChart;

/**
 *
 * @author Arthur
 */
public class FuzzyAgent implements Runnable{

    Car car;
    
    FunctionBlock functionBlock;
    private LinkedList<Double> valuesQueue = new LinkedList<>();
        

    FuzzyAgent(Car car) {
        this.car = car;
        

    }

    public void run() {
        String filename = "fuzzyCar.fcl";
        FIS fis = FIS.load(filename, true);

        if (fis == null) {
            System.err.println("Can't load file: '" + filename + "'");
            System.exit(1);
        }
        
        functionBlock = fis.getFunctionBlock(null);
        // Show
        //JFuzzyChart.get().chart(functionBlock);
        

        while (car.getY()>=0) {

            FunctionBlock fb = fis.getFunctionBlock(null);
            
            //System.out.println(car.getX());
          //  System.out.println(car.getPhi());
            //Set inputs
            fb.setVariable("posit", car.getX());
          //  System.out.println(car.getX());
            fb.setVariable("orient", (Math.toDegrees(car.getPhi())));
          //  System.out.println(car.getPhi());

            // Evaluate
            fb.evaluate();
            

            //Show output variable's chart
            //fb.getVariable("av").defuzzify();

            //Print ruleSet
            //System.out.println(fb);
            //System.out.println("AV: " + fb.getVariable("av").getValue());
            
            car.setTheta(fb.getVariable("av").getValue());
            
            valuesQueue.add(car.getX());
            valuesQueue.add(car.getPhi());
            valuesQueue.add(car.getTheta());
            
           // System.out.println("Fuzzy inputs: CarX ->"+car.getX()+"   CarPhi ->"+car.getPhi()+"   Output fuzzy: CarTheta ->"+car.getTheta());
            car.move(Direction.DOWN);
            

        }
       //car.showReplay();
        
        //System.out.println(valuesQueue.get(4));
         //NeuralAgent predictor = new NeuralAgent(3,this,car);
//        try {
//            predictor.prepareDataLearning();
//        } catch (IOException ex) {
//            Logger.getLogger(FuzzyAgent.class.getName()).log(Level.SEVERE, null, ex);
//        }
        
        
      
        
    }

    public LinkedList<Double> getValuesQueue() {
        return valuesQueue;
    }

    public void setValuesQueue(LinkedList<Double> valuesQueue) {
        this.valuesQueue = valuesQueue;
    }
}
