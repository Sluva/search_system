package searchSystem;

import searchSystem.controller.InformationFactory;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.apache.log4j.Logger;

@SpringApplicationConfiguration(classes = {Application.class})
@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
public class AppTest {

    @Autowired
    private InformationFactory informationFactory;

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private CheckSiteStatusController checkSiteStatusController;

    private final static Logger logger = Logger.getLogger(AppTest.class);

    @Test
    public void checkBackOf() {

        String url = "http://localhost:8089/status503";
        Assert.assertFalse(checkSiteStatusController.checkSiteStatus(url));
    }
}
