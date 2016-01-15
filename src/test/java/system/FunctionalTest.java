package system;

import system.controller.InformationFactory;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import twitter4j.TwitterException;


@SpringApplicationConfiguration(classes = {Application.class})
@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
public class FunctionalTest {

    private InformationFactory informationFactory = new InformationFactory();

    @Test
    public void checkUserName() {
        try {
            Assert.assertTrue(informationFactory.check("byte") != null);
        } catch (TwitterException e) {
            e.printStackTrace();
        }
    }
}
