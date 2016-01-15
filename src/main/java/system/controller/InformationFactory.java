package system.controller;

import system.controller.impl.Twitter4jParser;
import twitter4j.TwitterException;
import twitter4j.User;

import java.util.ArrayList;

/**
 * InformationFactory.
 */
public class InformationFactory {

    /**
     * Twitter4jParser.
     */
    private Twitter4jParser parser = new Twitter4jParser();

    /**
     * getFollowers.
     * @param login userLogin.
     * @return followers.
     * @throws TwitterException intercepts exceptions.
     */
    public final User[] getFollowers(final String login)
            throws TwitterException {

        Long[] userId = parser.getFollowers(login).toArray(new Long[1]);
        User[] users = new User[userId.length];

        for (int i = 0; i < users.length; i++) {
            users[i] = parser.getUser(userId[i]);
        }

        return users;
    }

    /**
     * filterByNumberFollowers.
     * @param users usersArray.
     * @param min minUsers.
     * @param max maxUsers.
     * @return usersArray.
     */
    public final User[] filterByNumberFollowers(final User[] users,
                                                final int min,
                                                final int max) {
        ArrayList<User> result = new ArrayList<>();
        for (User user : users) {
            int amountFollowers = user.getFollowersCount();
            if (amountFollowers <= max && amountFollowers >= min) {
                result.add(user);
            }
        }
        return result.toArray(new User[1]);
    }

    /**
     * filterByNumberFavourites.
     * @param users usersArray.
     * @param min minUsers.
     * @param max maxUsers.
     * @return usersArray.
     */
    public final User[] filterByNumberFavourites(final User[] users,
                                                 final int min,
                                                 final int max) {
        ArrayList<User> result = new ArrayList<>();
        for (User user : users) {
            int amountFavourites = user.getFavouritesCount();
            if (amountFavourites <= max && amountFavourites >= min) {
                result.add(user);
            }
        }
        return result.toArray(new User[1]);
    }

    /**
     * filterByNumberFriends.
     * @param users usersArray.
     * @param min minUsers.
     * @param max maxUsers.
     * @return usersArray.
     */
    public final User[] filterByNumberFriends(final User[] users,
                                              final int min,
                                              final int max) {
        ArrayList<User> result = new ArrayList<>();
        for (User user : users) {
            int amountFriends = user.getFriendsCount();
            if (amountFriends <= max && amountFriends >= min) {
                result.add(user);
            }
        }
        return result.toArray(new User[1]);
    }

    /**
     * filterByUserName.
     * @param users usersArray.
     * @param name partOfName.
     * @return usersArray.
     */
    public final User[] filterByUserName(final User[] users,
                                         final String name) {
        ArrayList<User> result = new ArrayList<>();
        for (User user : users) {
            String userName = user.getName().trim().toLowerCase();
            if (userName.contains(name.toLowerCase())) {
                result.add(user);
            }
        }
        return result.toArray(new User[1]);
    }

    /**
     * filterByUserScreenName.
     * @param users usersArray.
     * @param screenName partOfScreenName.
     * @return usersArray.
     */
    public final User[] filterByUserScreenName(final User[] users,
                                               final String screenName) {
        ArrayList<User> result = new ArrayList<>();
        for (User user : users) {
            String userScreenName = user.getScreenName().trim().toLowerCase();
            if (userScreenName.contains(screenName.toLowerCase())) {
                result.add(user);
            }
        }
        return result.toArray(new User[1]);
    }

    /**
     * checkLogin.
     * @param login userLogin.
     * @return User.
     * @throws TwitterException intercepts exceptions.
     */
    public final User check(final String login) throws TwitterException {
        return parser.getUser(login);
    }
}
