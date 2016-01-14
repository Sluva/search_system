package searchSystem;

import searchSystem.Application;
import searchSystem.controller.InformationFactory;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import twitter4j.TwitterException;


@SpringApplicationConfiguration(classes = {Application.class})
@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
public class FunctionalTest {

    private InformationFactory informationFactory = new InformationFactory();

    @Autowired
    private ApplicationContext applicationContext;
    @Test
    public void checkUserName() {
        try {
            Assert.assertTrue(informationFactory.check("byte") != null);
        } catch (TwitterException e) {
            e.printStackTrace();
        }
    }
}
