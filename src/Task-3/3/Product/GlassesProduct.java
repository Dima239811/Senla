
package Product;
import ProductParts.IProductPart;

public class GlassesProduct implements IProduct{

    @Override
    public void installFirstPart(IProductPart iProductPart) {
        System.out.println("установлена первая часть - корпус");
    }

    @Override
    public void installSecondPart(IProductPart iProductPart) {
        System.out.println("установлена вторая часть - линзы");
    }

    @Override
    public void installThirdPart(IProductPart iProductPart) {
        System.out.println("установлена третья часть - дужки");
    }
}
