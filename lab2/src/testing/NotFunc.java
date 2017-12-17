package testing;

public interface NotFunc {
    default void foo() {
        System.out.println("NotFunc");
    }
}
