package Assembly;

import Product.GlassesProduct;
import Product.IProduct;
import ProductParts.Arches;
import ProductParts.Body;
import ProductParts.Lenses;
import Steps.StepAddArches;
import Steps.StepAddBody;
import Steps.StepAddLenses;

public class AssemblyLine implements IAssemblyLine {

    private Arches arches;
    private Body body;
    private Lenses lenses;


    public AssemblyLine(StepAddBody stepAddBody, StepAddLenses stepAddLenses, StepAddArches stepAddArches) {
        body = (Body) stepAddBody.buildProductPart();
        lenses = (Lenses) stepAddLenses.buildProductPart();
        arches = (Arches) stepAddArches.buildProductPart();
    }

    @Override
    public IProduct assemblyProduct(IProduct iProduct) {
        System.out.println("начало сборки продукта");
        iProduct.installFirstPart(body);
        iProduct.installSecondPart(lenses);
        iProduct.installThirdPart(arches);
        System.out.println("конец сборки продукта");
        return iProduct;
    }
}
