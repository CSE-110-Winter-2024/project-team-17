package edu.ucsd.cse110.secards.lib.util.errors;

public class NotImplementedException extends RuntimeException {
    public NotImplementedException(String name) {
        super(name + " is not yet implemented.");
    }
}
