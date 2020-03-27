package pl.mzuchnik.springpracadomowa3.exception;

public class CarNotFoundException extends RuntimeException{

    public CarNotFoundException()
    {
        super("Could not find any car");
    }


    public CarNotFoundException(long id) {
        super("Could not find car with id: " + id);
    }
}
