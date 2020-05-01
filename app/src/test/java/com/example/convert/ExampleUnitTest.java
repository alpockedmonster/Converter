package com.example.convert;

import org.junit.Test;

import static org.junit.Assert.*;


/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void sumString_isCorrect() {
        MainActivity mainA = new MainActivity();
        String adr = "https://free.currconv.com/api/v7/convert?q=RUB_RUB&compact=ultra&apiKey=e4e5e3d4047fa6dff94d/";
        String currency = mainA.sumString("RUB", "RUB");
        assertEquals(adr, currency);
    }
    @Test
    public void parseVal_isCorrect() {
        MainActivity mainA = new MainActivity();
        String value = mainA.parseVal("10.0", "10.0");
        assertEquals("100.0", value);
    }
}