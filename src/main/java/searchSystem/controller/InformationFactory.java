package searchSystem.controller;

import searchSystem.controller.impl.Twitter4jParser;
import org.apache.log4j.Logger;
import twitter4j.TwitterException;
import twitter4j.User;

import java.util.*;

public class InformationFactory {

    private Twitter4jParser parser = new Twitter4jParser(5);
    private final static Logger logger = Logger.getLogger(InformationFactory.class);

    public User[] getFollowers(String login) throws TwitterException {

        Long[] userId = parser.getFollowers(login).toArray(new Long[1]);
        User[] users = new User[userId.length];

        for (int i = 0; i < users.length; i++)
            users[i] = parser.getUser(userId[i]);

        return users;
    }

    public User[] filterByNumberFollowers(User[] users, int min, int max) {
        ArrayList<User> result = new ArrayList<>();
        for (User user : users) {
            int amountFollowers = user.getFollowersCount();
            if (amountFollowers <= max && amountFollowers >= min)
                result.add(user);
        }
        return result.toArray(new User[1]);
    }

    public User[] filterByNumberFavourites(User[] users, int min, int max) {
        ArrayList<User> result = new ArrayList<>();
        for (User user : users) {
            int amountFavourites = user.getFavouritesCount();
            if (amountFavourites <= max && amountFavourites >= min)
                result.add(user);
        }
        return result.toArray(new User[1]);
    }

    public User[] filterByNumberFriends(User[] users, int min, int max) {
        ArrayList<User> result = new ArrayList<>();
        for (User user : users) {
            int amountFriends = user.getFriendsCount();
            if (amountFriends <= max && amountFriends >= min)
                result.add(user);
        }
        return result.toArray(new User[1]);
    }

    public User[] filterByUserName(User[] users, String name) {
        ArrayList<User> result = new ArrayList<>();
        for (User user : users) {
            String userName = user.getName().trim().toLowerCase();
            if (userName.contains(name.toLowerCase()))
                result.add(user);
        }
        return result.toArray(new User[1]);
    }

    public User[] filterByUserScreenName(User[] users, String screenName) {
        ArrayList<User> result = new ArrayList<>();
        for (User user : users) {
            String userScreenName = user.getScreenName().trim().toLowerCase();
            if (userScreenName.contains(screenName.toLowerCase()))
                result.add(user);
        }
        return result.toArray(new User[1]);
    }

    public User check(String login) throws TwitterException {
        return parser.getUser(login);
    }
}


