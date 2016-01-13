package searchSystem;

import searchSystem.controller.InformationFactory;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import twitter4j.TwitterException;
import twitter4j.User;

import java.util.Map;


@Controller
@Configuration
@ComponentScan(basePackages = { "com.mkyong.*" })
@PropertySource("classpath:git.properties")
public class MainController {

    @Value("${git.commit.id.describe-short}")
    private String idDescribeShort;
    @Value("${git.commit.time}")
    private String gitCommitTime;

    @Autowired
    private InformationFactory informationFactory;

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private CheckSiteStatusController checkSiteStatusController;

    private final static Logger logger = Logger.getLogger(MainController.class);


    @RequestMapping(value = "/followers", method = RequestMethod.GET)
    public String showImages(
            @RequestParam(value = "login", required = false, defaultValue = "") String login,
            Map<String, Object> map) {
        if (!checkSiteStatusController.checkSiteStatus("https://twitter.com")) {
            map.put("errorMessage", "https://twitter.com does not respond, please try later");
            return "error";
        }
        User user;
        try {
            user = informationFactory.check(login);
            map.put("infos", informationFactory.getUserImages(login));
//            String bufferedImage = imageCreator.getImgUrl(images, width, height);
//            map.put("imgSrc", bufferedImage);

        } catch (TwitterException /*| IOException*/ e) {
            logger.error(e);
            if (e instanceof TwitterException) {
                if (((TwitterException) e).getErrorMessage().equals("Rate limit exceeded"))
                    map.put("errorMessage", "Rate limit, please wait " + ((TwitterException) e).getRateLimitStatus().getSecondsUntilReset() / 60 + ":" + ((TwitterException) e).getRateLimitStatus().getSecondsUntilReset() % 60);
                else if (((TwitterException) e).getErrorMessage().equals("Sorry, that page does not exist."))
                    map.put("errorMessage", "login:\"" + login + "\" not found");
                else {
                    map.put("errorMessage", "Sorry, error");
                }
            }
            return "error";
        }
        map.put("user", user);
        return "viewImage";
    }

    @RequestMapping(value = "/index")
    public String redirect() {
        return "index";
    }

    @RequestMapping(value = "/")
    public String ef() {
        return "index";
    }

    @RequestMapping(value = "/testBackOff")
    public String checBackOff(Map<String, Object> map) {
        String url = "http://localhost:8089/status503";
        if (!checkSiteStatusController.checkSiteStatus(url))
            map.put("errorMessage", url + " does not respond, please try later");
        else
            map.put("errorMessage", "backOff don't work:(");

        return "error";
    }
    @RequestMapping(value = "/status503")
    @ResponseBody
    public ResponseEntity<String> testBackOf(){
        return new ResponseEntity<String>(HttpStatus.SERVICE_UNAVAILABLE);
    }

    @RequestMapping(value = "/gitproperties")
    public String gitProperties(Map<String, Object> map){
        map.put("errorMessage", "commit id describe short "  + idDescribeShort + " git commit time = " + gitCommitTime);
        return "error";
    }




}