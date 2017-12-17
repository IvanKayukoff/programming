package testing;

import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;


public class Tests {

    static int count(int x) {
        return x * x - x;
    }

    public static void main(String[] args) {
        List<Integer> list = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 0);
        int res = sumAll(list, new Func() {
            @Override
            public int count(int x) {
                return x;
            }
        });
        System.out.println(res);
        res = sumAll(list, x -> x);
        System.out.println(res);
        System.out.println(checkLinkWith(5, Tests::count));
        System.out.println(checkLinkWith(5, x -> x * x - 5));

    }

    static int sumAll(List<Integer> numbers, Func func) {
        int result = 0;
        for (int number: numbers) {
            if (func.count(number) > 0) {
                result += number;
            }
        }
        return result;
    }

    static int checkLinkWith(int x, Func func) {
        return func.count(x);
    }
}
