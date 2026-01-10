package t_1;
import java.net.Inet4Address;
import java.util.Random;

public class RandInt{
    public static int conv_str_to_int(String str){
        return Integer.parseInt(String.valueOf(str.charAt(0)));
    }

    public static void main(String[] args) {
        Random rnd = new Random();
        String num1 = new String(String.valueOf(rnd.nextInt(900-100+1)));
        String num2 = new String(String.valueOf(rnd.nextInt(900-100+1)));
        String num3 = new String(String.valueOf(rnd.nextInt(900-100+1)));

        System.out.println("Первое число: " + num1 + " Второе число: " + num2 + " Третье число: " + num3 + "\n");

        int sum_first_nums = conv_str_to_int(num1) + conv_str_to_int(num2) + conv_str_to_int(num3);

        System.out.println("Сумма первых цифр из чисел: " + sum_first_nums);

    }
}