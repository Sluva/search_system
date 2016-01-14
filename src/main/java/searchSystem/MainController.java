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

    private InformationFactory informationFactory = new InformationFactory();

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private CheckSiteStatusController checkSiteStatusController;

    private final static Logger logger = Logger.getLogger(MainController.class);


    @RequestMapping(value = "/resultWindow", method = RequestMethod.GET)
    public String showImages(
            @RequestParam(value = "login", required = false, defaultValue = "") String login,
            @RequestParam(value = "input-min-followers", required = false, defaultValue = "") int minFollowers,
            @RequestParam(value = "input-max-followers", required = false, defaultValue = "") int maxFollowers,
            @RequestParam(value = "input-min-favourites", required = false, defaultValue = "") int minFavourites,
            @RequestParam(value = "input-max-favourites", required = false, defaultValue = "") int maxFavourites,
            @RequestParam(value = "input-min-friends", required = false, defaultValue = "") int minFriends,
            @RequestParam(value = "input-max-friends", required = false, defaultValue = "") int maxFriends,
            @RequestParam(value = "user-name", required = false, defaultValue = "") String userName,
            @RequestParam(value = "user-screen-name", required = false, defaultValue = "") String userScreenName,
            Map<String, Object> map) {
        if (!checkSiteStatusController.checkSiteStatus("https://twitter.com")) {
            map.put("errorMessage", "https://twitter.com does not respond, please try later");
            return "errorWindow";
        }

        User user;
        User[] users;

        try {
            user = informationFactory.check(login);
            users = informationFactory.getFollowers(login);

            if (minFollowers <= maxFollowers && minFollowers >= 0)
                users = informationFactory.filterByNumberFollowers(users, minFollowers, maxFollowers);

            if (minFavourites <= maxFavourites && minFavourites >= 0)
                users = informationFactory.filterByNumberFavourites(users, minFavourites, maxFavourites);

            if (minFriends <= maxFriends && minFriends >= 0)
                users = informationFactory.filterByNumberFriends(users, minFriends, maxFriends);

            if (!userName.trim().isEmpty())
                users = informationFactory.filterByUserName(users, userName);

            if (!userScreenName.trim().isEmpty())
                users = informationFactory.filterByUserScreenName(users, userScreenName);

        } catch (TwitterException /*| IOException*/ e) {
            logger.error(e);
            if (e.getErrorMessage().equals("Rate limit exceeded"))
                map.put("errorMessage", "Rate limit, please wait " + e.getRateLimitStatus().getSecondsUntilReset() / 60 + ":" + e.getRateLimitStatus().getSecondsUntilReset() % 60);
            else if (e.getErrorMessage().equals("Sorry, that page does not exist."))
                map.put("errorMessage", "login:\"" + login + "\" not found");
            else {
                map.put("errorMessage", "Sorry, error");
            }
            return "errorWindow";
        }

        map.put("followers", users);
        map.put("user", user);
        return "resultWindow";
    }

    @RequestMapping(value = "/mainWindow")
    public String redirect() {
        return "mainWindow";
    }

    @RequestMapping(value = "/")
    public String ef() {
        return "mainWindow";
    }

    @RequestMapping(value = "/testBackOff")
    public String checBackOff(Map<String, Object> map) {
        String url = "http://localhost:8089/status503";
        if (!checkSiteStatusController.checkSiteStatus(url))
            map.put("errorMessage", url + " does not respond, please try later");
        else
            map.put("errorMessage", "backOff don't work:(");

        return "errorWindow";
    }
    @RequestMapping(value = "/status503")
    @ResponseBody
    public ResponseEntity<String> testBackOf(){
        return new ResponseEntity<>(HttpStatus.SERVICE_UNAVAILABLE);
    }

    @RequestMapping(value = "/gitproperties")
    public String gitProperties(Map<String, Object> map){
        map.put("errorMessage", "commit id describe short "  + idDescribeShort + " git commit time = " + gitCommitTime);
        return "errorWindow";
    }




}