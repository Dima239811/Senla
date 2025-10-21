package Steps;
import ProductParts.IProductPart;
import ProductParts.Lenses;


public class StepAddLenses implements ILineStep{
    @Override
    public IProductPart buildProductPart() {
        System.out.println("шаг установки линз");
        return new Lenses();
    }
}
