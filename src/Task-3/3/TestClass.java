import Assembly.AssemblyLine;
import Product.GlassesProduct;
import Product.IProduct;
import Steps.StepAddArches;
import Steps.StepAddBody;
import Steps.StepAddLenses;

public class TestClass {
    public static void main(String[] args) {
        StepAddBody stepAddBody = new StepAddBody();
        StepAddLenses stepAddLenses = new StepAddLenses();
        StepAddArches stepAddArches = new StepAddArches();

        System.out.println("создание сборочного конвейера");
        AssemblyLine assemblyLine = new AssemblyLine(stepAddBody, stepAddLenses, stepAddArches);
        IProduct glassesProduct = new GlassesProduct();

        glassesProduct = assemblyLine.assemblyProduct(glassesProduct);
    }
}
