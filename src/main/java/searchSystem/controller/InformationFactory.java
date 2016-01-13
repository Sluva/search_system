package searchSystem.controller;

import searchSystem.controller.impl.Twitter4jParser;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import twitter4j.TwitterException;
import twitter4j.User;

import java.util.*;

public class InformationFactory {

    static final double MAX_PERCENT = 0.3;
    @Autowired
    private Twitter4jParser parser;
    private final static Logger logger = Logger.getLogger(InformationFactory.class);


    public String getUserImages(String login) throws TwitterException {

        List<Long> userId = parser.getFollowers(login);
        Iterator<Long> iter = userId.iterator();
        String info = "";
        while(iter.hasNext()) {
            System.out.println("added");
            User user = parser.getUser(iter.next());
            info += "Name: " + user.getName() + ",   Date of create : " + user.getCreatedAt() + ",   Count of followers : " + user.getFollowersCount()+" ............................................................................................................................................................................................................................................................................................................................................. ";
        }
        return info;
    }

    public User check(String login) throws TwitterException {
        return parser.getUser(login);
    }
}


