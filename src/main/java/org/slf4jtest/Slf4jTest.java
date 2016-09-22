package org.slf4jtest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by watson zhang on 16/5/17.
 */
public class Slf4jTest {
    Logger logger = LoggerFactory.getLogger(Slf4jTest.class);
    Integer t;
    Integer oldT;


    public void setTemp(Integer temp){
        oldT = t;
        t = temp;
        logger.error("temp set to {} Old temp is {}", t, oldT);
        if(temp.intValue() > 50){
            logger.info("temp has risen above 50 degrees!");
        }
    }

    public static void main(String[] args){
        Slf4jTest slf4jTest = new Slf4jTest();
        //System.setProperty("log4j.configuration", "log4j.properties");

        slf4jTest.logger.debug("test start...");

        slf4jTest.logger.error("test error...");
        slf4jTest.setTemp(1);
        slf4jTest.setTemp(55);
        slf4jTest.logger.error("test");
        return;
    }
}
