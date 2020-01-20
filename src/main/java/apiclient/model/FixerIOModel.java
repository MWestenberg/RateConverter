package apiclient.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.*;

public class FixerIOModel implements Serializable, ApiModel {


    private final static Logger LOG = LoggerFactory.getLogger(FixerIOModel.class);
    private boolean success  = false;
    private int timestamp;
    private String base;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-mm-dd")
    private Date date;
    Map<String, Double> rates;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public int getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(int timestamp) {
        this.timestamp = timestamp;
    }

    public String getBase() {
        return base;
    }

    public void setBase(String base) {
        this.base = base;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Map<String, Double> getRates() {
        return rates;
    }

    public void setRates(Map<String, Double> rates) {
        this.rates = rates;
    }


    //==== Below methods from interface ===//

    @Override
    public boolean hasRate(String currency)
    {
        return rates.containsKey(currency);
    }

    @Override
    public boolean isExpired(int hours) {
        Calendar yesterday = Calendar.getInstance();
        yesterday.add(Calendar.HOUR, (-1 * hours));

        Calendar cacheTime = Calendar.getInstance();
        cacheTime.setTime(date);


        if (cacheTime.before(yesterday))
        {
            LOG.info("Cache is out of date. Attemptig to call ApiClient for update");
            return true;
        }

        return false;
    }

    @Override
    public List<String> getAllCurrency() {
        List<String> currencies = new ArrayList<>();
        this.rates.forEach((k, v) -> currencies.add(k));
        return currencies;
    }

    @Override
    public Rate getRate(String currency) {
        return hasRate(currency)?  new Rate(base, currency, rates.get(currency)) : new Rate();
    }

    @Override
    public List<Rate> getAllRates() {
        List<Rate> rateList = new ArrayList<>();
        this.rates.forEach((k, v) -> rateList.add(new Rate(base, k, v)));


        return rateList;
    }

}
