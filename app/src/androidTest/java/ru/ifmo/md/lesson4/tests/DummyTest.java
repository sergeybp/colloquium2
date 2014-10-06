package ru.ifmo.md.lesson4.tests;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import ru.ifmo.md.lesson4.CalculationEngineFactory;
import ru.ifmo.md.lesson4.CalculationException;

//Sergey Budkov 2536

@Config(emulateSdk = 18)
@RunWith(RobolectricTestRunner.class)
public class DummyTest {
    @Before
    public void setup() {
        //do whatever is necessary before every test
    }


    @Test
    public void calculationTest() {
        try {
            Assert.assertEquals(18.0, CalculationEngineFactory.defaultEngine().calculate("5+6+7"));
            Assert.assertEquals(596*459/35.2+(486-962), CalculationEngineFactory.defaultEngine().calculate("596*459/35.2+(486-962)"));
            Assert.assertEquals(496.2+176.3/145.8, CalculationEngineFactory.defaultEngine().calculate("496.2+176.3/145.8"));
            Assert.assertEquals(.1+.2+.3+.4, CalculationEngineFactory.defaultEngine().calculate(".1+.2+.3+.4"));
            Assert.assertEquals(8.0, CalculationEngineFactory.defaultEngine().calculate("------8"));
            //all right
        } catch (CalculationException e){
            Assert.fail();
        }
    }


    @Test
    public void invalidationTests(){
        try{
            CalculationEngineFactory.defaultEngine().calculate("");
            CalculationEngineFactory.defaultEngine().calculate("+/");
            CalculationEngineFactory.defaultEngine().calculate("1.1.1");
            CalculationEngineFactory.defaultEngine().calculate("3/((3)");
            CalculationEngineFactory.defaultEngine().calculate("3**4");
            CalculationEngineFactory.defaultEngine().calculate("noDigits");
            Assert.fail();

        } catch (CalculationException e) {
            //all right
        }
    }

    @Test
    public void validationTests(){
        try{
            CalculationEngineFactory.defaultEngine().calculate("5+5");
            CalculationEngineFactory.defaultEngine().calculate("5+.1");
            CalculationEngineFactory.defaultEngine().calculate("--2");
            CalculationEngineFactory.defaultEngine().calculate("(5+5)/(2-1)");
            CalculationEngineFactory.defaultEngine().calculate("3*4/.47775");
            CalculationEngineFactory.defaultEngine().calculate("0.789*0.789/0.894");
            //all right
        } catch (CalculationException e) {
            Assert.fail();
        }
    }

    @Test
    public void testWhoppingComplex() {
        try {
            Assert.assertEquals(10d, CalculationEngineFactory.defaultEngine().calculate("5+5"));
        } catch (CalculationException e) {
            Assert.fail("Exception happened " + e);
        }
    }
}
