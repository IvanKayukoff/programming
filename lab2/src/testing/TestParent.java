package testing;

public class TestParent {
    public static void main(String[] args) {
        Child child = new Child();
        Child.run((Parent) null);
    }
}
