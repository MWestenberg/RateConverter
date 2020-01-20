import apiclient.drivers.FixerIODriver;
import model.RateConverter;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.Assert.*;


public class ConverterTest {

    private static final Logger LOG = LoggerFactory.getLogger(ConverterTest.class);

    @Test
    public void initialise_convert_with_default_driver()
    {
        RateConverter conv = new RateConverter();
        assertNotEquals(conv.getAllCurrencies().size(), 0);
        assertTrue(conv.getRate("EUR").getRate() > 0.0);
        assertFalse(conv.getRate("USD").getCurrency().isEmpty());
    }

    @Test
    public void initialise_convert_with_typed_driver()
    {
        RateConverter conv = new RateConverter(FixerIODriver.class);
        assertNotEquals(conv.getAllCurrencies().size(), 0);
        assertTrue(conv.getRate("EUR").getRate() > 0.0);
        assertFalse(conv.getRate("USD").getCurrency().isEmpty());
    }

    @Test
    public void can_convert_basic_currency()
    {
        RateConverter converter = new RateConverter();
        assertTrue(converter.convert("EUR","USD", 1.0) > 0);
        LOG.info("1 EURO -> USD: " + converter.convert("EUR","USD", 1.0));
        assertTrue(converter.convert("EUR","USD", 12.5) > 0);
        LOG.info("1 USD -> EURO: " + converter.convert("USD","EUR", 1.0));
        assertTrue(converter.convert("USD","GBP", 5.24) > 0);
        LOG.info("1 USD -> GPB: " + converter.convert("USD","GPB", 1.0));
    }

}
