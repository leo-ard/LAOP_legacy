package org.lrima.network.exceptions;

public class WrongNumberOfInputException extends Exception {

    private int numberOfInputs, goodNumberOfInputs;

    public WrongNumberOfInputException(int numberOfInputs, int goodNumberOfInputs){
        this.numberOfInputs = numberOfInputs;
        this.goodNumberOfInputs = goodNumberOfInputs;
    }

    @Override
    public String getMessage() {
        return "Wrong number of inputs ! You have " + this.numberOfInputs + ". You should have: " + goodNumberOfInputs;
    }
}
