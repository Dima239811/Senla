package Product;
import ProductParts.IProductPart;

public interface IProduct {
    void installFirstPart(IProductPart iProductPart);
    void installSecondPart(IProductPart iProductPart);
    void installThirdPart(IProductPart iProductPart);
}
