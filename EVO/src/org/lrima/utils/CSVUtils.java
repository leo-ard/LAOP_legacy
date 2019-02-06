package org.lrima.utils;

import org.lrima.simulation.Generation;
import org.lrima.simulation.Simulation;
import org.lrima.simulation.SimulationBatch;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CSVUtils {

    private static final char DEFAULT_SEPARATOR = ',';

    public static void writeLine(Writer w, List<String> values) throws IOException {
        writeLine(w, values, DEFAULT_SEPARATOR, '"');
    }

    public static void writeLine(Writer w, List<String> values, char separators) throws IOException {
        writeLine(w, values, separators, ' ');
    }

    //https://tools.ietf.org/html/rfc4180
    private static String followCVSformat(String value) {

        String result = value;
        if (result.contains("\"")) {
            result = result.replace("\"", "\"\"");
        }
        return result;

    }

    public static void writeLine(Writer w, List<String> values, char separators, char customQuote) throws IOException {

        boolean first = true;

        //default customQuote is empty

        if (separators == ' ') {
            separators = DEFAULT_SEPARATOR;
        }

        StringBuilder sb = new StringBuilder();
        for (String value : values) {
            if (!first) {
                sb.append(separators);
            }
            if (customQuote == ' ') {
                sb.append(followCVSformat(value));
            } else {
                sb.append(customQuote).append(followCVSformat(value)).append(customQuote);
            }

            first = false;
        }
        sb.append("\n");
        w.append(sb.toString());


    }

    public static void toCSV(ArrayList<SimulationBatch> simulationBatches)  {
        String filename = "data/test";
        File file;
        int i = 0;
        do{
            file = new File(filename + i + ".csv");
            i++;
        }while(file.exists());

        System.out.println("Printing in CSV file at " + file.getAbsolutePath());

        try {
            FileWriter fileWriter = new FileWriter(file.getAbsolutePath());

            for(SimulationBatch simulationBatch : simulationBatches){
                CSVUtils.writeLine(fileWriter, Arrays.asList("Algorithm", simulationBatch.getAlgorithmModel().getName()));

                for(int simulationCount = 0; simulationCount < simulationBatch.getSimulations().length; simulationCount++){
                    Simulation simulation = simulationBatch.getSimulations()[simulationCount];

                    CSVUtils.writeLine(fileWriter, Arrays.asList("Simulation count", simulationCount+""));

                    for(Generation gen : simulation.getGenerationList()){
                        CSVUtils.writeLine(fileWriter, toString(gen.getAllFitnesses()) , ',','"');
                    }



                }
            }

            fileWriter.flush();
            fileWriter.close();





        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    private static List<String> toString(ArrayList<Double> doubleList) {
        List<String> stringList = new ArrayList<String>();

        doubleList.forEach(d -> stringList.add(d.toString()));

        return stringList;
    }


}
