package Steps;
import ProductParts.Body;
import ProductParts.IProductPart;

public class StepAddBody implements ILineStep{
    @Override
    public IProductPart buildProductPart() {
        System.out.println("шаг установки корпуса");
        Body body = new Body();
        return body;
    }
}
