package testing;

@FunctionalInterface
public interface Func {
    int count(int x);

    default void foo() { }
}


