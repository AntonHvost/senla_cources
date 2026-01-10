package t_3.parts;

import t_3.interfaces.IProductPart;
public class Monitor implements IProductPart{
    public Monitor(){
        System.out.println("Экран ноутбука создан");
    }

    @Override
    public String toString() {
        return "экран";
    }
}
