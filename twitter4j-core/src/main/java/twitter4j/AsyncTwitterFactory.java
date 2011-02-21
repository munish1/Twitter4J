/*
 * Copyright 2007 Yusuke Yamamoto
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package twitter4j;

import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationContext;
import twitter4j.http.AccessToken;
import twitter4j.http.Authorization;
import twitter4j.http.AuthorizationFactory;
import twitter4j.http.BasicAuthorization;
import twitter4j.http.OAuthAuthorization;


/**
 * A factory class for AsyncTwitter.<br>
 * An instance of this class is completely thread safe and can be re-used and used concurrently.<br>
 * Note that currently AsyncTwitter is NOT compatible with Google App Engine as it is maintaining threads internally.
 *
 * @author Yusuke Yamamoto - yusuke at mac.com
 * @since Twitter4J 2.1.0
 */
public final class AsyncTwitterFactory implements java.io.Serializable {
    private final TwitterListener listener;
    private final Configuration conf;
    private static final long serialVersionUID = -2565686715640816219L;

    /**
     * Creates an AsyncTwitterFactory with the root configuration, with no listener. AsyncTwitter instances will not perform callbacks when using this constructor.
     */
    public AsyncTwitterFactory() {
        this(ConfigurationContext.getInstance());
    }

    /**
     * Creates an AsyncTwitterFactory with the given configuration.
     *
     * @param conf the configuration to use
     * @since Twitter4J 2.1.1
     */
    public AsyncTwitterFactory(Configuration conf) {
        if (conf == null) {
            throw new NullPointerException("configuration cannot be null");
        }
        this.conf = conf;
        this.listener = null;
    }

    /**
     * Creates a AsyncTwitterFactory with the specified config tree, with given listener
     *
     * @param configTreePath the path
     * @since Twitter4J 2.1.12
     */
    public AsyncTwitterFactory(String configTreePath) {
        this.conf = ConfigurationContext.getInstance(configTreePath);
        this.listener = null;
    }

    /**
     * Returns a default singleton instance.
     *
     * @return default singleton instance
     */
    public AsyncTwitter getInstance() {
        return getInstance(conf);
    }

    /**
     * Returns an XAuth Authenticated instance.
     *
     * @param screenName screen name
     * @param password   password
     * @return an instance
     */
    public AsyncTwitter getInstance(String screenName, String password) {
        return getInstance(conf, new BasicAuthorization(screenName, password));
    }

    /**
     * Returns a OAuth Authenticated instance.<br>
     * consumer key and consumer Secret must be provided by twitter4j.properties, or system properties.<br>
     * Unlike {@link AsyncTwitter#setOAuthAccessToken(twitter4j.http.AccessToken)}, this factory method potentially returns a cached instance.
     *
     * @param accessToken access token
     * @return an instance
     */
    public AsyncTwitter getInstance(AccessToken accessToken) {
        String consumerKey = conf.getOAuthConsumerKey();
        String consumerSecret = conf.getOAuthConsumerSecret();
        if (null == consumerKey && null == consumerSecret) {
            throw new IllegalStateException("Consumer key and Consumer secret not supplied.");
        }
        OAuthAuthorization oauth = new OAuthAuthorization(conf);
        oauth.setOAuthConsumer(consumerKey, consumerSecret);
        oauth.setOAuthAccessToken(accessToken);
        return getInstance(conf, oauth);
    }

    /**
     * @param auth authorization
     * @return an instance
     */
    public AsyncTwitter getInstance(Authorization auth) {
        return getInstance(conf, auth);
    }

    private AsyncTwitter getInstance(Configuration conf, Authorization auth) {
        AsyncTwitter twitter = new AsyncTwitter(conf, auth);
        if (null != listener) {
            twitter.addListener(listener);
        }
        return twitter;
    }

    private AsyncTwitter getInstance(Configuration conf) {
        AsyncTwitter asyncTwitter = new AsyncTwitter(conf, AuthorizationFactory.getInstance(conf));
        if (null != listener) {
            asyncTwitter.addListener(listener);
        }
        return asyncTwitter;
    }
}
