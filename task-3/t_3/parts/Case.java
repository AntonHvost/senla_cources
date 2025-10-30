package t_3.parts;

import t_3.interfaces.IProductPart;

public class Case implements IProductPart{
    public Case(){
        System.out.println("Корпус ноутбука создан");
    }

    @Override
    public String toString() {
        return "Корпус";
    }
}
