package t_3.product;

import t_3.interfaces.IProduct;
import t_3.interfaces.IProductPart;

public class Laptop implements IProduct {
    private IProductPart casePart;
    private IProductPart motherboardPart;
    private IProductPart monitorPart;

    @Override
    public void installFirstPart(IProductPart part1) {
        this.casePart = part1;
        System.out.println("Корпус успешно установлен!");
    }

    @Override
    public void installSecondPart(IProductPart part2) {
        this.motherboardPart = part2;
        System.out.println("Материнская плата успешно установлена!");
    }

    @Override
    public void installThirdPart(IProductPart part3) {
        this.monitorPart = part3;
        System.out.println("Монитор успешно установлен!");
    }

    @Override
    public String toString() {
        return "Ноутбук собран из: {" + casePart +
                ", " + motherboardPart +
                ", " + monitorPart +
                '}';
    }
}
