import java.util.LinkedHashMap;
import java.util.Map;

public final class CurrencyConverter {
    private static final Map<String,Double> rates=new LinkedHashMap<>();
    static {
        rates.put("USD",1.0); rates.put("EUR",0.92); rates.put("GBP",0.79);
        rates.put("INR",83.10); rates.put("JPY",149.50); rates.put("AUD",1.52);
    }
    private CurrencyConverter(){}
    public static String[] currencies(){return rates.keySet().toArray(new String[0]);}
    public static double convert(double amount,String from,String to){
        return amount/rates.get(from)*rates.get(to);
    }
}
