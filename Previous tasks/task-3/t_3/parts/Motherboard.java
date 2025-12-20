package t_3.parts;

import t_3.interfaces.IProductPart;

public class Motherboard implements IProductPart {
    public Motherboard(){
        System.out.println("Материнская плата ноутбука создана");
    }

    @Override
    public String toString() {
        return "материнская плата";
    }
}
