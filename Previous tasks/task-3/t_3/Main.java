package t_3;

import t_3.interfaces.IAssemblyLine;
import t_3.interfaces.IProduct;

import t_3.assembler.LaptopAssembler;
import t_3.product.Laptop;
import t_3.steps.CaseBuilder;
import t_3.steps.MonitorBuilder;
import t_3.steps.MotherboardBuilder;

public class Main {
    public static void main(String[] args) {
        IAssemblyLine assemblyLine = new LaptopAssembler(new CaseBuilder(), new MotherboardBuilder(), new MonitorBuilder());
        IProduct newLaptop = new Laptop();
        newLaptop = assemblyLine.assembleProduct(newLaptop);

        System.out.println(newLaptop);
    }
}
