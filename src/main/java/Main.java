import model.RateConverter;

public class Main {

    public static void main(String[] args) {
        RateConverter converter= new RateConverter();
        System.out.println("1 EURO -> USD: " + converter.convert("EUR","USD", 1.0));
        System.out.println("1 USD -> EURO: " + converter.convert("USD","EUR", 1.0));
        System.out.println("1 USD -> GPB: " + converter.convert("USD","GBP", 1.0));
        System.out.println("1 BTC -> EURO: " + converter.convert("BTC","EUR", 1.0));
    }
}
