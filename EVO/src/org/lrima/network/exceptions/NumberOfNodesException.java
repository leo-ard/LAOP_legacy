package org.lrima.network.exceptions;

public class NumberOfNodesException extends Exception {

    private int numberOfNodes = 0;

    public NumberOfNodesException(int numberOfNodes){
        this.numberOfNodes = numberOfNodes;
    }

    @Override
    public String getMessage() {
        return "caca";
    }
}
