package apiclient.model;

public class Rate {

    private String currency;
    private double rate;
    private String baseCurrency;

    public Rate() {
        this("", "",0.0);
    }

    public Rate(String baseCurrency) {
        this(baseCurrency, "",0.0);
    }

    public Rate(String baseCurrency, String currency, double rate) {
        this.currency = currency;
        this.rate = rate;
        this.baseCurrency = baseCurrency;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    public String getBaseCurrency() {
        return baseCurrency;
    }

    public void setBaseCurrency(String baseCurrency) {
        this.baseCurrency = baseCurrency;
    }



}
