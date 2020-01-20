package apiclient.model;

import java.util.List;

public interface ApiModel {

    boolean isExpired(int hours);
    boolean hasRate(String currency);
    List<String> getAllCurrency();
    List<Rate> getAllRates();
    Rate getRate(String currency);
}
