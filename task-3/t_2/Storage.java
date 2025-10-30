package t_2;

import java.util.LinkedList;

abstract class Product{
    private String name;
    private double weight;
    private int amount;

    Product(String n, double w, int a){
        this.name = n;
        this.weight = w;
        this.amount = a;
    }

    public double getWeight() {
        return weight;
    }

    public String getName() {
        return name;
    }

    public int getAmount(){
        return amount;
    }

    @Override
    public abstract String toString();
}

class ElectricProducts extends Product{

    private int power;

    ElectricProducts(String n, double w, int a, int p) {
        super(n, w, a);
        this.power = p;
    }

    @Override
    public String toString() {
        return "Электроника: " + getName() + " (" + getWeight() + " кг), мощность (Вт): " + power;
    }
}

class FoodProduct extends Product{

    private int shelfLife;

    FoodProduct(String n, double w, int a, int sl) {
        super(n, w, a);
        this.shelfLife = sl;
    }

    @Override
    public String toString() {
        return "Продукт: " + getName() + " (" + getName() + " кг), срок годности: " + shelfLife + " дн.";
    }
}

class FurnitureProduct extends Product{
    private String material;
    FurnitureProduct(String n, double w, int a, String m){
        super(n, w, a);
        this.material = m;
    }

    @Override
    public String toString() {
        return "Мебель: " + getName() + " (" + getWeight() + " кг), материал: " + material;
    }
}

class FuncionsStorage{
    private int maxStorageCapacity;
    private int currentStorageCapacity;
    private LinkedList<Product> productsList = new LinkedList<>();

    FuncionsStorage(int storageCapacity){
        this.maxStorageCapacity = storageCapacity;
    }

    public boolean addProduct(Product product){
        if(currentStorageCapacity <= maxStorageCapacity){
            productsList.add(product);
            currentStorageCapacity += product.getAmount();
            return true;
        }
        else{
            System.out.println("Превышен лимит склада!");
            return false;
        }
    }

    public double getTotalWeight(){
        return productsList.stream().mapToDouble(Product::getWeight).sum();
    }

    public void showContents() {
        System.out.println("📦 Содержимое склада:");
        for (Product p : productsList) {
            System.out.println(" - " + p);
        }
        System.out.printf("Общий вес: %.2f кг\n", getTotalWeight());
    }

}

public class Storage {
    public static void main(String[] args) {
        FuncionsStorage storage = new FuncionsStorage(5);

        storage.addProduct(new FurnitureProduct("Стол", 2.3, 2, "ДСП"));
        storage.addProduct(new ElectricProducts("Смартфон Iphone 15", 0.6, 3, 10));
        storage.addProduct(new FoodProduct("Молоко", 0.5, 1,14));

        storage.showContents();
    }
}