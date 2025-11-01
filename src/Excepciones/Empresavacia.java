package Excepciones;

public class Empresavacia extends RuntimeException {
    public Empresavacia(String message) {
        super(message);
    }
}
