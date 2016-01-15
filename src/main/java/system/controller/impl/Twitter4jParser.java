package system.controller.impl;

import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.TwitterException;
import twitter4j.IDs;
import twitter4j.User;

import java.util.LinkedList;
import java.util.List;

/**
 * Twitter4jParser.
 */
public class Twitter4jParser {

    /**
     * Twitter.
     */
    private Twitter twitter;

    /**
     * constructor.
     */
    public Twitter4jParser() {
        this.twitter = new TwitterFactory().getInstance();
    }

    /**
     * getFollowers.
     * @param login userLogin.
     * @return userFollowers.
     * @throws TwitterException intercepts exceptions.
     */
    public final List<Long> getFollowers(final String login)
            throws TwitterException {
        return getFollowers(new FollowersInterface() {
            @Override
            public IDs next() throws TwitterException {
                if (getCursor() == 0) {
                    return null;
                }
                IDs iDs = twitter.getFriendsIDs(login, getCursor());
                setCursor(iDs.getNextCursor());
                return iDs;
            }
        });
    }

    /**
     * getFollowers.
     * @param fol parameter.
     * @return followers.
     * @throws TwitterException intercepts exceptions.
     */
    private List<Long> getFollowers(final FollowersInterface fol)
            throws TwitterException {
        List<Long> res = new LinkedList<>();
        IDs iDs;
        while ((iDs = fol.next()) != null) {
            for (long id : iDs.getIDs()) {
                res.add(id);
            }
        }
        return res;
    }

    /**
     * getUser.
     * @param login userLogin.
     * @return User.
     * @throws TwitterException intercepts exceptions.
     */
    public final User getUser(final String login) throws TwitterException {
        return twitter.showUser(login);
    }

    /**
     * getUser.
     * @param id userId.
     * @return User.
     * @throws TwitterException intercepts exceptions.
     */
    public final User getUser(final Long id) throws TwitterException {
        return twitter.showUser(id);
    }
}

/**
 * FollowersInterface.
 */
abstract class FollowersInterface {

    /**
     * setter.
     * @param localCursor parameter.
     */
    public void setCursor(final long localCursor) {
        cursor = localCursor;
    }

    /**
     * getter.
     * @return cursor.
     */
    public long getCursor() {
        return cursor;
    }

    /**
     * cursor.
     */
    private long cursor = -1;

    /**
     * next.
     * @return IDs.
     * @throws TwitterException intercepts exceptions.
     */
    abstract IDs next() throws TwitterException;
}
