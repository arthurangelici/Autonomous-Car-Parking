/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package parkingCar;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.data.DataSet;
import org.neuroph.core.data.DataSetRow;
import org.neuroph.core.events.LearningEvent;
import org.neuroph.core.events.LearningEventListener;
import org.neuroph.core.learning.SupervisedLearning;
import org.neuroph.nnet.MultiLayerPerceptron;
import org.neuroph.nnet.learning.BackPropagation;

/**
 *
 * @author Arthur
 */
public class NeuralAgent implements Runnable {

    FuzzyAgent fuzzyAgent;
    Car car;
    private int slidingWindowSize; // n numeros anteriores
    private double maxX = 0;
    private double minX = Double.MAX_VALUE;
    private double maxPhi = 0;
    private double minPhi = Double.MAX_VALUE;
    private double maxTheta = 0;
    private double minTheta = Double.MAX_VALUE;
    private String rawDataFilePath = "Input/raw.csv";
    private String testDataFilePath = "Input/testingAgosto2016.csv";
    private String learningDataFilePath = "Input/learningData.csv";
    private String testingDataFilePath = "Input/testData.csv";
    private String neuralNetworkModelFilePath = "stockPredictor.nnet";
    private File file = new File(rawDataFilePath);

    public NeuralAgent(int slidingWindowSize, Car car) {
        //this.rawDataFilePath = rawDataFilePath;
        this.slidingWindowSize = slidingWindowSize;
        this.car = car;
        //this.fuzzyAgent = fuzzyAgent;

    }

    public File singletonPrepareDate() throws IOException {

        if (!file.exists()) {
            file.createNewFile();
            return file;

        } else {
            return file;
        }

    }

    public void prepareData() throws IOException {

        FileWriter fw = new FileWriter(singletonPrepareDate().getAbsoluteFile(), true);
        BufferedWriter writer = new BufferedWriter(fw);

        LinkedList<Double> valuesQueue = fuzzyAgent.getValuesQueue();

        try {

            while (!valuesQueue.isEmpty()) {
                writer.write(String.valueOf(valuesQueue.get(0)));
                writer.write(",");
                writer.write(String.valueOf(valuesQueue.get(1)));
                writer.write(",");
                writer.write(String.valueOf(valuesQueue.get(2)));
                writer.write(",");

                writer.newLine();
                valuesQueue.removeFirst();
                valuesQueue.removeFirst();
                valuesQueue.removeFirst();
            }
        } finally {
            writer.close();

        }

    }

    public void prepareDataLearning() throws FileNotFoundException, IOException {

        BufferedReader reader = new BufferedReader(new FileReader(rawDataFilePath));

        try {
            String line;
            while ((line = reader.readLine()) != null) {

                StringBuilder aux = new StringBuilder(line);
                String[] tokens = aux.toString().split(",");

                double crtValueX = Double.valueOf(tokens[0]);
                double crtValuePhi = Double.valueOf(tokens[1]);
                double crtValueTheta = Double.valueOf(tokens[2]);

                //System.out.println(crtValueX + " " + crtValuePhi + " " + crtValueTheta);
                if (crtValueX > maxX) {
                    maxX = crtValueX;
                }
                if (crtValuePhi > maxPhi) {
                    maxPhi = crtValuePhi;
                }

                if (crtValueTheta > maxTheta) {
                    maxTheta = crtValueTheta;
                }

                if (crtValueX < minX) {
                    minX = crtValueX;
                }
                if (crtValuePhi < minPhi) {
                    minPhi = crtValuePhi;
                }

                if (crtValueTheta < minTheta) {
                    minTheta = crtValueTheta;
                }

            }
        } finally {

            reader.close();

        }

        BufferedReader reader2 = new BufferedReader(new FileReader(rawDataFilePath));
        BufferedWriter writer2 = new BufferedWriter(new FileWriter(learningDataFilePath));
        LinkedList<Double> valuesQueue2 = new LinkedList<>();

        try {
            String line;
            while ((line = reader2.readLine()) != null) {
                StringBuilder aux = new StringBuilder(line);
                String[] tokens = aux.toString().split(",");

                double crtValueX = Double.valueOf(tokens[0]);
                double crtValuePhi = Double.valueOf(tokens[1]);
                double crtValueTheta = Double.valueOf(tokens[2]);
                double normalizedValue = normalizeValueX(crtValueX);
                double normalizedValue2 = normalizeValuePhi(crtValuePhi);
                double normalizedValue3 = normalizeValueTheta(crtValueTheta);

                //System.out.println(normalizedValue+" "+normalizedValue2+" "+normalizedValue3);
                valuesQueue2.add(normalizedValue);
                valuesQueue2.add(normalizedValue2);
                valuesQueue2.add(normalizedValue3);


                if (valuesQueue2.size() == slidingWindowSize) {
                    String valueLine = valuesQueue2.toString().replaceAll("\\[|\\]", "");
                    System.out.println(valueLine);
                    writer2.write(valueLine);
                    writer2.newLine();
                    valuesQueue2.removeFirst();
                    valuesQueue2.removeFirst();
                    valuesQueue2.removeFirst();

                }
            }
        } finally {

            reader2.close();
            writer2.close();

        }

    }

    double normalizeValueX(double input) {
        return (input - minX) / (maxX - minX) * 0.8 + 0.1;  // pega só a parte mais precisa da funcao para ficar mais facil o treinamento inicial
    }

    double normalizeValuePhi(double input) {
        return (input - minPhi) / (maxPhi - minPhi) * 0.8 + 0.1;
    }

    double normalizeValueTheta(double input) {
        return (input - minTheta) / (maxTheta - minTheta) * 0.8 + 0.1;
    }

    double deNormalizeValueX(double input) {
        return minX + (input - 0.1) * (maxX - minX) / 0.8;
    }

    double deNormalizeValuePhi(double input) {
        return minPhi + (input - 0.1) * (maxPhi - minPhi) / 0.8;
    }

    double deNormalizeValueTheta(double input) {
        return minTheta + (input - 0.1) * (maxTheta - minTheta) / 0.8;
    }

    public void trainNetwork() throws IOException {
        NeuralNetwork<BackPropagation> neuralNetwork = new MultiLayerPerceptron(slidingWindowSize-1, 2 * slidingWindowSize + 1, 1);
        int maxIterations = 1000;
        double learningRate = 0.5;
        double maxError = 0.00001;
        SupervisedLearning learningRule = neuralNetwork.getLearningRule();
        learningRule.setMaxError(maxError);
        learningRule.setLearningRate(learningRate);
        learningRule.setMaxIterations(maxIterations);
        learningRule.addListener(new LearningEventListener() {
            public void handleLearningEvent(LearningEvent learningEvent) {
                SupervisedLearning rule = (SupervisedLearning) learningEvent.getSource();
                System.out.println("Network error for iteration" + rule.getCurrentIteration() + rule.getTotalNetworkError());
            }
        });

        DataSet trainingSet = loadTrainingData(learningDataFilePath);
        neuralNetwork.learn(trainingSet);
        neuralNetwork.save(neuralNetworkModelFilePath);
    }

    public DataSet loadTrainingData(String filePath) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        DataSet trainingSet = new DataSet(slidingWindowSize-1, 1);

        try {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] tokens = line.split(",");

                double trainValues[] = new double[slidingWindowSize-1];
                for (int i = 0; i < slidingWindowSize - 1; i++) {
                    trainValues[i] = Double.valueOf(tokens[i]);
                }
                double expectedValue[] = new double[1];
                expectedValue[0] = Double.valueOf(tokens[slidingWindowSize - 1]);

                trainingSet.addRow(new DataSetRow(trainValues, expectedValue));
            }
        } finally {
            reader.close();
        }
        return trainingSet;
    }

    void testNetwork() throws FileNotFoundException, IOException {

        //NeuralNetwork neuralNetwork = NeuralNetwork.createFromFile(neuralNetworkModelFilePath);
        BufferedReader reader = new BufferedReader(new FileReader(rawDataFilePath));

        try {
            String line;
            while ((line = reader.readLine()) != null) {

                StringBuilder aux = new StringBuilder(line);
                String[] tokens = aux.toString().split(",");

                double crtValueX = Double.valueOf(tokens[0]);
                double crtValuePhi = Double.valueOf(tokens[1]);
                double crtValueTheta = Double.valueOf(tokens[2]);

                //System.out.println(crtValueX + " " + crtValuePhi + " " + crtValueTheta);
                if (crtValueX > maxX) {
                    maxX = crtValueX;
                }
                if (crtValuePhi > maxPhi) {
                    maxPhi = crtValuePhi;
                }

                if (crtValueTheta > maxTheta) {
                    maxTheta = crtValueTheta;
                }

                if (crtValueX < minX) {
                    minX = crtValueX;
                }
                if (crtValuePhi < minPhi) {
                    minPhi = crtValuePhi;
                }

                if (crtValueTheta < minTheta) {
                    minTheta = crtValueTheta;
                }

            }
        } finally {

            reader.close();

        }

        BufferedWriter writer = new BufferedWriter(new FileWriter(testDataFilePath));

        LinkedList<Double> valuesQueue = fuzzyAgent.getValuesQueue();

        try {
            while (!valuesQueue.isEmpty()) {
                writer.write(String.valueOf(valuesQueue.get(0)));
                writer.write(",");
                writer.write(String.valueOf(valuesQueue.get(1)));
                writer.write(",");
                writer.write(String.valueOf(valuesQueue.get(2)));
                writer.write(",");

                writer.newLine();
                valuesQueue.removeFirst();
                valuesQueue.removeFirst();
                valuesQueue.removeFirst();
            }
        } finally {
            writer.close();
        }

        BufferedReader reader2 = new BufferedReader(new FileReader(testDataFilePath));
        BufferedWriter writer2 = new BufferedWriter(new FileWriter(testingDataFilePath));
        LinkedList<Double> valuesQueue2 = new LinkedList<>();

        try {
            String line;
            while ((line = reader2.readLine()) != null) {
                StringBuilder aux = new StringBuilder(line);
                String[] tokens = aux.toString().split(",");

                double crtValueX = Double.valueOf(tokens[0]);
                double crtValuePhi = Double.valueOf(tokens[1]);
                double crtValueTheta = Double.valueOf(tokens[2]);
                double normalizedValue = normalizeValueX(crtValueX);
                double normalizedValue2 = normalizeValuePhi(crtValuePhi);
                double normalizedValue3 = normalizeValueTheta(crtValueTheta);

                System.out.println(normalizedValue + " " + normalizedValue2 + " " + normalizedValue3);
                valuesQueue2.add(normalizedValue);
                valuesQueue2.add(normalizedValue2);
                valuesQueue2.add(normalizedValue3);


                if (valuesQueue2.size() == slidingWindowSize) {
                    String valueLine = valuesQueue2.toString().replaceAll("\\[|\\]", "");
                    //System.out.println(valueLine);
                    writer2.write(valueLine);
                    writer2.newLine();
                    valuesQueue2.removeFirst();
                    valuesQueue2.removeFirst();
                    valuesQueue2.removeFirst();

                }
            }
        } finally {

            reader2.close();
            writer2.close();

        }
    }

    public void calcularErro() throws FileNotFoundException, IOException {
        NeuralNetwork neuralNetwork = NeuralNetwork.createFromFile(neuralNetworkModelFilePath);

        BufferedReader reader = new BufferedReader(new FileReader(rawDataFilePath));

        try {
            String line;
            while ((line = reader.readLine()) != null) {

                StringBuilder aux = new StringBuilder(line);
                String[] tokens = aux.toString().split(",");

                double crtValueX = Double.valueOf(tokens[0]);
                double crtValuePhi = Double.valueOf(tokens[1]);
                double crtValueTheta = Double.valueOf(tokens[2]);

                //System.out.println(crtValueX + " " + crtValuePhi + " " + crtValueTheta);
                if (crtValueX > maxX) {
                    maxX = crtValueX;
                }
                if (crtValuePhi > maxPhi) {
                    maxPhi = crtValuePhi;
                }

                if (crtValueTheta > maxTheta) {
                    maxTheta = crtValueTheta;
                }

                if (crtValueX < minX) {
                    minX = crtValueX;
                }
                if (crtValuePhi < minPhi) {
                    minPhi = crtValuePhi;
                }

                if (crtValueTheta < minTheta) {
                    minTheta = crtValueTheta;
                }

            }
        } finally {

            reader.close();

        }

        BufferedReader reader2 = new BufferedReader(new FileReader(testingDataFilePath));
        double trainValues[] = new double[slidingWindowSize-1];
        double expectedValue = 0;
        double[] networkOutput;
        double erromax = 0;
        double somaErro = 0;
        int counter = 0;

        try {
            String line;
            while ((line = reader2.readLine()) != null) {

                String[] tokens = line.split(",");

                for (int i = 0; i < slidingWindowSize-1; i++) {
                    trainValues[i] = Double.valueOf(tokens[i]);
                }

                expectedValue = Double.valueOf(tokens[slidingWindowSize - 1]);
                neuralNetwork.setInput(trainValues);
                neuralNetwork.calculate();
                networkOutput = neuralNetwork.getOutput();
                System.out.println(networkOutput.length);

                System.out.println(Arrays.toString(trainValues));
                System.out.println("Expected value :" + deNormalizeValueTheta(expectedValue));
                System.out.println("Predicted value:" + deNormalizeValueTheta(networkOutput[0]));
                double erro = Math.abs((deNormalizeValueTheta(networkOutput[0]) - deNormalizeValueTheta(expectedValue)) / deNormalizeValueTheta(expectedValue));
                System.out.println("Erro = " + erro);
                if (Math.abs(erro) > erromax) {
                    erromax = erro;
                }
                counter++;
                somaErro = somaErro + Math.abs(erro);

            }
        } finally {
            reader.close();
        }

    }

    public void run() {

//        try {
//            trainNetwork();
//        } catch (IOException ex) {
//            Logger.getLogger(NeuralAgent.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        
//     

        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader(rawDataFilePath));

            try {
                String line;
                try {
                    while ((line = reader.readLine()) != null) {

                        StringBuilder aux = new StringBuilder(line);
                        String[] tokens = aux.toString().split(",");

                        double crtValueX = Double.valueOf(tokens[0]);
                        double crtValuePhi = Double.valueOf(tokens[1]);
                        double crtValueTheta = Double.valueOf(tokens[2]);

                        //System.out.println(crtValueX + " " + crtValuePhi + " " + crtValueTheta);
                        if (crtValueX > maxX) {
                            maxX = crtValueX;
                        }
                        if (crtValuePhi > maxPhi) {
                            maxPhi = crtValuePhi;
                        }

                        if (crtValueTheta > maxTheta) {
                            maxTheta = crtValueTheta;
                        }

                        if (crtValueX < minX) {
                            minX = crtValueX;
                        }
                        if (crtValuePhi < minPhi) {
                            minPhi = crtValuePhi;
                        }

                        if (crtValueTheta < minTheta) {
                            minTheta = crtValueTheta;
                        }

                    }
                } catch (IOException ex) {
                    Logger.getLogger(NeuralAgent.class.getName()).log(Level.SEVERE, null, ex);
                }
            } finally {

                try {
                    reader.close();
                } catch (IOException ex) {
                    Logger.getLogger(NeuralAgent.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(NeuralAgent.class.getName()).log(Level.SEVERE, null, ex);
        }

        //calcularErro();
        NeuralNetwork neuralNetwork = NeuralNetwork.createFromFile(neuralNetworkModelFilePath);

        while (car.getY() >= 0) {

            double trainValues[] = new double[slidingWindowSize-1];
            trainValues[0] = normalizeValueX(car.getX());
            //System.out.println(normalizeValueX(100));
            trainValues[1] = normalizeValuePhi(car.getPhi());
            double[] networkOutput;
            neuralNetwork.setInput(trainValues);
            neuralNetwork.calculate();
            networkOutput = neuralNetwork.getOutput();
            System.out.println("Train Values input: CarX ->"+car.getX()+"   CarPhi ->"+car.getPhi()+"   Output neural: CarTheta ->"+networkOutput[0]);

            try {
                //System.out.println();

                new Thread().sleep(5);
            } catch (InterruptedException ex) {
                Logger.getLogger(NeuralAgent.class.getName()).log(Level.SEVERE, null, ex);
            }
//            
            car.setTheta(Math.toDegrees(deNormalizeValueTheta(networkOutput[0])));
            
            
            car.move(Direction.DOWN);
            
            
        }
        
        //car.showReplay();
       
    }
}



