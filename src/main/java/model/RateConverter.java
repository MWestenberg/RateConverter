package model;

import apiclient.drivers.ApiDriver;
import apiclient.drivers.FixerIODriver;
import apiclient.model.ApiModel;
import apiclient.model.Rate;
import config.AppConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class RateConverter {

   private static final AppConfig CONFIG = AppConfig.init();
   private static final Logger LOG = LoggerFactory.getLogger(RateConverter.class);
   private ApiModel model;
   private ApiDriver driver;


    public RateConverter() {
        setDefaultDriver();
    }

    public RateConverter(Class<?> driverClass) {

       if (ApiDriver.class.isAssignableFrom(driverClass))
       {
           try {
               this.driver = (ApiDriver)driverClass.getDeclaredConstructor().newInstance();
               setModel();
           } catch (Exception e) {
               LOG.error("The ApiDriver class "+driverClass.getName() + " cannot be accessed");
               LOG.warn("Falling back to default driver");
               setDefaultDriver();
           }
       }
       else
       {
           LOG.error(driverClass.getName() + "is not of type ApiDriverClass");
       }
   }

   private void setDefaultDriver() {
       LOG.info("Using default driver");
       try {
           this.driver = new FixerIODriver();
           setModel();
       } catch (Exception e) {
           LOG.error("The default ApiDriver class  cannot be accessed");
       }
   }

   private void setModel()
   {
       this.model = driver.getApiModel();
   }

    public List<String> getAllCurrencies() {
        return model.getAllCurrency();
    }

    public List<Rate> getAvailableRates() {
       return model.getAllRates();
    }

    public Rate getRate(String currency) {
        return model.getRate(currency);
    }

    public double convert(String from, String to, double value)
    {
        Rate fromRate = this.getRate(from);
        Rate toRate = this.getRate(to);

        if (rateCheck(fromRate, from) && rateCheck(toRate, to))
        {
            return (value / fromRate.getRate()) * toRate.getRate();
        }

        return 0.0;
    }

    public boolean canConvert(String currency) {

        return model.hasRate(currency);
    }

    private boolean rateCheck(Rate rate, String currency)
    {

        if (rate == null || rate.getRate() == 0.0)
        {
            LOG.warn("Rate " + currency+ " is unknown or has a conversion rate of 0.");
            return false;
        }
        return true;

    }

}
