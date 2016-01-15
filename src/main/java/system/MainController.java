package system;

import system.controller.InformationFactory;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

/**
 * MainController.
 */
@Controller
@Configuration
@ComponentScan(basePackages = { "com.mkyong.*" })
@PropertySource("classpath:git.properties")
public class MainController {

    /**
     * idDescribeShort.
     */
    @Value("${git.commit.id.describe-short}")
    private String idDescribeShort;

    /**
     * gitCommitTime.
     */
    @Value("${git.commit.time}")
    private String gitCommitTime;

    /**
     * InformationFactory.
     */
    private InformationFactory informationFactory = new InformationFactory();

    /**
     * CheckSiteStatusController.
     */
    @Autowired
    private CheckSiteStatusController checkSiteStatusController;

    /**
     * Logger.
     */
    private final Logger logger = Logger.getLogger(MainController.class);

    /**
     * searchFollowers.
     * @param login loginUser.
     * @param minFollowers minimum amount of followers.
     * @param maxFollowers maximum amount of followers.
     * @param minFavourites minimum amount of favourites.
     * @param maxFavourites maximum amount of favourites.
     * @param minFriends minimum amount of friends.
     * @param maxFriends maximum amount of friends.
     * @param userName part of user name.
     * @param userScreenName part of screen user name.
     * @param map storage.
     * @return type of window.
     */
    @RequestMapping(value = "/resultWindow", method = RequestMethod.GET)
    public final String searchFollowers(
            @RequestParam(value = "login",
                    required = false, defaultValue = "") final String login,
            @RequestParam(value = "min-followers",
                    required = false, defaultValue = "-1")
            final int minFollowers,
            @RequestParam(value = "max-followers",
                    required = false, defaultValue = "-1")
            final int maxFollowers,
            @RequestParam(value = "min-favourites",
                    required = false, defaultValue = "-1")
            final int minFavourites,
            @RequestParam(value = "max-favourites",
                    required = false, defaultValue = "-1")
            final int maxFavourites,
            @RequestParam(value = "min-friends",
                    required = false, defaultValue = "-1")
            final int minFriends,
            @RequestParam(value = "max-friends",
                    required = false, defaultValue = "-1")
            final int maxFriends,
            @RequestParam(value = "user-name",
                    required = false, defaultValue = "")
            final String userName,
            @RequestParam(value = "user-screen-name",
                    required = false, defaultValue = "")
            final String userScreenName,
            final Map<String, Object> map) {
        if (!checkSiteStatusController.checkSiteStatus("https://twitter.com")) {
            map.put("errorMessage",
                    "https://twitter.com does not respond, please try later");
            return "errorWindow";
        }
System.out.print(minFollowers);
        User user;
        User[] users;

        try {
            user = informationFactory.check(login);
            users = informationFactory.getFollowers(login);

            if (minFollowers <= maxFollowers && maxFollowers >= 0) {
                users = informationFactory.filterByNumberFollowers(users,
                        minFollowers, maxFollowers);
            }

            if (minFavourites <= maxFavourites && maxFavourites >= 0) {
                users = informationFactory.filterByNumberFavourites(users,
                        minFavourites, maxFavourites);
            }

            if (minFriends <= maxFriends && maxFriends >= 0) {
                users = informationFactory.filterByNumberFriends(users,
                        minFriends, maxFriends);
            }

            if (!userName.trim().isEmpty()) {
                users = informationFactory.filterByUserName(users, userName);
            }

            if (!userScreenName.trim().isEmpty()) {
                users = informationFactory.filterByUserScreenName(users,
                        userScreenName);
            }

        } catch (TwitterException /*| IOException*/ e) {
            final int secondsInMinute = 60;
            logger.error(e);
            if (e.getErrorMessage().equals("Rate limit exceeded")) {
                map.put("errorMessage", "Rate limit, please wait "
                        + e.getRateLimitStatus().getSecondsUntilReset()
                        / secondsInMinute + ":"
                        + e.getRateLimitStatus().getSecondsUntilReset()
                        % secondsInMinute);
            } else if (e.getErrorMessage()
                    .equals("Sorry, that page does not exist.")) {
                map.put("errorMessage", "login:\"" + login + "\" not found");
            } else {
                map.put("errorMessage", "Sorry, error");
            }
            return "errorWindow";
        }

        map.put("followers", users);
        map.put("user", user);
        return "resultWindow";
    }

    /**
     * redirect.
     * @return window type.
     */
    @RequestMapping(value = "/mainWindow")
    public final String redirect() {
        return "mainWindow";
    }

    /**
     * ef.
     * @return window type.
     */
    @RequestMapping(value = "/")
    public final String ef() {
        return "mainWindow";
    }

    /**
     * testBackOf.
     * @param map errorMessage.
     * @return window type.
     */
    @RequestMapping(value = "/testBackOff")
    public final String checkBackOff(final Map<String, Object> map) {
        String url = "http://localhost:8089/status503";
        if (!checkSiteStatusController.checkSiteStatus(url)) {
            map.put("errorMessage", url
                    + " does not respond, please try later");
        } else {
            map.put("errorMessage", "backOff don't work:(");
        }

        return "errorWindow";
    }

    /**
     * error window.
     * @return error 503.
     */
    @RequestMapping(value = "/status503")
    @ResponseBody
    public final ResponseEntity<String> testBackOf() {
        return new ResponseEntity<>(HttpStatus.SERVICE_UNAVAILABLE);
    }

    /**
     * gitproperties.
     * @param map data.
     * @return type of window.
     */
    @RequestMapping(value = "/gitproperties")
    public final String gitProperties(final Map<String, Object> map) {
        map.put("errorMessage", "commit id describe short "
                + idDescribeShort + " git commit time = " + gitCommitTime);
        return "errorWindow";
    }
}
