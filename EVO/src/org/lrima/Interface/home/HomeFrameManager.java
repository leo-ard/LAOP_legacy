package org.lrima.Interface.home;


import org.lrima.Interface.home.pages.HomePanel;
import org.lrima.network.interfaces.NeuralNetworkModel;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;

public class HomeFrameManager {
    public static final Dimension screenDimension = Toolkit.getDefaultToolkit().getScreenSize();
    public static final int FRAME_WIDTH = screenDimension.width / 3;
    public static final int FRAME_HEIGHT = (int)(screenDimension.height / 1.5);


    private ArrayList<PagePanel> history = new ArrayList<>();
    private int currentIndex;
    private JFrame mainFrame;

    public HomeFrameManager(){
        mainFrame = new JFrame("Welcome to EVO");
        mainFrame.setLayout(new BorderLayout());
        mainFrame.setResizable(true);
        mainFrame.setBounds(new Rectangle(0, 0, FRAME_WIDTH, FRAME_HEIGHT));
        mainFrame.setLocationRelativeTo(null);
        mainFrame.setVisible(true);

        mainFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        currentIndex = -1;
        next(new HomePanel(this));
    }

    public void next(PagePanel page){
        currentIndex++;

        //Remove all pages that came after the one that we are now going to
        while(history.size() > this.currentIndex){
            history.remove(history.size()-1);
        }

        history.add(page);
        setPanelByIndex();
    }

    private void setPanelByIndex(){
        mainFrame.setContentPane(history.get(currentIndex));
        mainFrame.setTitle(history.get(currentIndex).getName());
    }

    public void back() {
        currentIndex--;
        setPanelByIndex();
    }

    public void close(){
        mainFrame.dispose();
    }

    /**
     * Save the list of models to a file so that you can reinstantiate the after clicking the
     * last simulation button
     * @param models an array containing the models to save
     */
    public void addModelsToSaved(ArrayList<NeuralNetworkModel> models){
        ArrayList<Object> objects = new ArrayList<>();
        objects.addAll(models);


        File file = new File("last.simulation");
        try {
            if (file.createNewFile()) {
                System.out.println("File 'last.simulation' was created");
            }
            else{
                //Resets the file
                PrintWriter writer = new PrintWriter(file);
                writer.print("");
                writer.close();
            }
        }catch (IOException e){
            System.err.println("Error when creating last.simulation file");
        }

        try {
            FileOutputStream fos = new FileOutputStream(file);
            ObjectOutputStream oos = new ObjectOutputStream(fos);

            oos.writeObject(models);

            oos.close();
            fos.close();

        }catch(Exception e){
            System.err.println("There was an error while saving the file !");
            e.printStackTrace();
        }
    }

    /**
     * Retreive the saved models from the file 'last.simulation'
     * @return an ArrayList containing the saved models
     */
    public ArrayList<NeuralNetworkModel> getSavedModels(){
        try{
            FileInputStream fis = new FileInputStream(new File("last.simulation"));
            ObjectInputStream ois = new ObjectInputStream(fis);

            return (ArrayList<NeuralNetworkModel>) ois.readObject();

        } catch (FileNotFoundException e) {
            System.err.println("The file 'last.simulation' doesn't exist !");
        } catch (IOException e){
            System.err.println("There was an error while loading the file !");
        } catch (ClassNotFoundException e) {
            System.err.println("The file contains the wrong type of object !");
        }

        return null;
    }

}
