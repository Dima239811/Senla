package Assembly;
import Product.IProduct;

public interface IAssemblyLine {
    IProduct assemblyProduct(IProduct iProduct);
}
