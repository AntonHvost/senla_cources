package t_3.steps;

import t_3.interfaces.ILineStep;
import t_3.interfaces.IProductPart;
import t_3.parts.Monitor;

public class MonitorBuilder implements ILineStep{
    @Override
    public IProductPart buildProductPart() {
        System.out.println("Идёт установка экрана...");
        return new Monitor();
    }
}
