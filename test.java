public boolean concateneTest() {
    MyString classATester = new MyString();
    String a = "salut les ";
    String b = "zeros";
    String resultatAttendu = "salut les zeros";
        String resultatObtenu = classATester.concatene(a, b);
        if (resultatAttendu.compareTo(resultatObtenu) == 0) {
         return true;
        }
        else {
         return false;
        }
}

public interface Calculator {

    int multiply(int a, int b);
    int divide(int a, int b);
    int add(int a, int b);
    int substract(int a, int b);

}

import static org.junit.Assert.*;

import org.junit.Test;

public class CalculatorImplTest {

    @Test
    public final void testMultiply() {
        fail("Not yet implemented"); // TODO
    }

    @Test
    public final void testDivide() {
        fail("Not yet implemented"); // TODO
    }

    @Test
    public final void testAdd() {
        fail("Not yet implemented"); // TODO
    }

    @Test
    public final void testSubstract() {
        fail("Not yet implemented"); // TODO
    }

}
