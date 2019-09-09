public class PriceInstance {

    private String productId;
    private String productName;
    private int priceValue;
    public enum PriceType {KG, UNIT, L, NOT_SPECIFIED}
    private  PriceType priceType;
    static final int MISSING_PRICE = -1;
    static final String MISSING_PRODUCT_NAME = "NO DATA";


    PriceInstance(String productId, String productName, int priceValue, PriceType priceType){
        this.productId = productId;
        this.productName = productName;
        this.priceValue = priceValue;
        this.priceType = priceType;
    }

    int getPriceValue() {
        return priceValue;
    }

    String getProductId() {
        return productId;
    }

    String getProductName() {
        return productName;
    }

    PriceType getPriceType() {
        return priceType;
    }

    @Override
    public String toString(){
        return "productId: " + getProductId() + System.lineSeparator() +
                "productName: " + getProductName() + System.lineSeparator() +
                "priceValue: " + getPriceValue() + System.lineSeparator() +
                "priceType: " + getPriceType() + System.lineSeparator();
    }
}
