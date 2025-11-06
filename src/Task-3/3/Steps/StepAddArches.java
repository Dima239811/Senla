package Steps;
import ProductParts.Arches;
import ProductParts.IProductPart;

public class StepAddArches implements ILineStep{
    @Override
    public IProductPart buildProductPart() {
        System.out.println("шаг установки дужек");
        return new Arches();
    }
}
