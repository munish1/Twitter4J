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

package twitter4j.conf;

import twitter4j.Version;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Configuration base class with default settings.
 *
 * @author Yusuke Yamamoto - yusuke at mac.com
 */
class ConfigurationBase implements Configuration, java.io.Serializable {

    private boolean debug;
    private String userAgent;
    private String user;
    private String password;
    private boolean useSSL;
    private boolean prettyDebug;
    private boolean gzipEnabled;
    private String httpProxyHost;
    private String httpProxyUser;
    private String httpProxyPassword;
    private int httpProxyPort;
    private int httpConnectionTimeout;
    private int httpReadTimeout;

    private int httpStreamingReadTimeout;
    private int httpRetryCount;
    private int httpRetryIntervalSeconds;
    private int maxTotalConnections;
    private int defaultMaxPerRoute;
    private String oAuthConsumerKey;
    private String oAuthConsumerSecret;
    private String oAuthAccessToken;
    private String oAuthAccessTokenSecret;

    private String oAuthRequestTokenURL;
    private String oAuthAuthorizationURL;
    private String oAuthAccessTokenURL;
    private String oAuthAuthenticationURL;

    private String restBaseURL;
    private String searchBaseURL;
    private String streamBaseURL;
    private String userStreamBaseURL;
    private String siteStreamBaseURL;

    private String dispatcherImpl;

    private int asyncNumThreads;

    private boolean includeRTsEnabled;

    private boolean includeEntitiesEnabled;

    private boolean userStreamRepliesAllEnabled;

    private String mediaProvider;

    private String mediaProviderAPIKey;

    private Properties mediaProviderParameters;
    // hidden portion
    private String clientVersion;
    private String clientURL;

    public static final String DALVIK = "twitter4j.dalvik";

    private static final String DEFAULT_OAUTH_REQUEST_TOKEN_URL = "http://api.twitter.com/oauth/request_token";
    private static final String DEFAULT_OAUTH_AUTHORIZATION_URL = "http://api.twitter.com/oauth/authorize";
    private static final String DEFAULT_OAUTH_ACCESS_TOKEN_URL = "http://api.twitter.com/oauth/access_token";
    private static final String DEFAULT_OAUTH_AUTHENTICATION_URL = "http://api.twitter.com/oauth/authenticate";

    private static final String DEFAULT_REST_BASE_URL = "http://api.twitter.com/1/";
    private static final String DEFAULT_SEARCH_BASE_URL = "http://search.twitter.com/";
    private static final String DEFAULT_STREAM_BASE_URL = "http://stream.twitter.com/1/";
    private static final String DEFAULT_USER_STREAM_BASE_URL = "https://userstream.twitter.com/2/";
    private static final String DEFAULT_SITE_STREAM_BASE_URL = "https://betastream.twitter.com/2b/";

    private boolean IS_DALVIK;
    private static final long serialVersionUID = -6610497517837844232L;

    static String dalvikDetected;

    static {
        // detecting dalvik (Android platform)
        try {
            // dalvik.system.VMRuntime class should be existing on Android platform.
            // @see http://developer.android.com/reference/dalvik/system/VMRuntime.html
            Class.forName("dalvik.system.VMRuntime");
            dalvikDetected = "true";
        } catch (ClassNotFoundException cnfe) {
            dalvikDetected = "false";
        }
    }

    protected ConfigurationBase() {
        setDebug(false);
        setUser(null);
        setPassword(null);
        setUseSSL(false);
        setPrettyDebugEnabled(false);
        setGZIPEnabled(true);
        setHttpProxyHost(null);
        setHttpProxyUser(null);
        setHttpProxyPassword(null);
        setHttpProxyPort(-1);
        setHttpConnectionTimeout(20000);
        setHttpReadTimeout(120000);
        setHttpStreamingReadTimeout(60 * 5 * 1000);
        setHttpRetryCount(0);
        setHttpRetryIntervalSeconds(5);
        setHttpMaxTotalConnections(20);
        setHttpDefaultMaxPerRoute(2);
        setOAuthConsumerKey(null);
        setOAuthConsumerSecret(null);
        setOAuthAccessToken(null);
        setOAuthAccessTokenSecret(null);
        setAsyncNumThreads(1);
        setClientVersion(Version.getVersion());
        setClientURL("http://twitter4j.org/en/twitter4j-" + Version.getVersion() + ".xml");
        setUserAgent("twitter4j http://twitter4j.org/ /" + Version.getVersion());

        setIncludeRTsEnbled(true);

        setIncludeEntitiesEnbled(false);


        setOAuthRequestTokenURL(DEFAULT_OAUTH_REQUEST_TOKEN_URL);
        setOAuthAuthorizationURL(DEFAULT_OAUTH_AUTHORIZATION_URL);
        setOAuthAccessTokenURL(DEFAULT_OAUTH_ACCESS_TOKEN_URL);
        setOAuthAuthenticationURL(DEFAULT_OAUTH_AUTHENTICATION_URL);

        setRestBaseURL(DEFAULT_REST_BASE_URL);
        // search api tends to fail with SSL as of 12/31/2009
        // setSearchBaseURL(fixURL(useSSL, "http://search.twitter.com/"));
        setSearchBaseURL(DEFAULT_SEARCH_BASE_URL);
        // streaming api doesn't support SSL as of 12/30/2009
        // setStreamBaseURL(fixURL(useSSL, "http://stream.twitter.com/1/"));
        setStreamBaseURL(DEFAULT_STREAM_BASE_URL);
        setUserStreamBaseURL(DEFAULT_USER_STREAM_BASE_URL);
        setSiteStreamBaseURL(DEFAULT_SITE_STREAM_BASE_URL);

        setDispatcherImpl("twitter4j.internal.async.DispatcherImpl");

        setIncludeRTsEnbled(true);
        setUserStreamRepliesAllEnabled(false);
        String isDalvik;
        try {
            isDalvik = System.getProperty(DALVIK, dalvikDetected);
        } catch (SecurityException ignore) {
            // Unsigned applets are not allowed to access System properties
            isDalvik = dalvikDetected;
        }
        IS_DALVIK = Boolean.valueOf(isDalvik);

        setMediaProvider("YFROG");
        setMediaProviderAPIKey(null);
        setMediaProviderParameters(null);
    }


    public final boolean isDalvik() {
        return IS_DALVIK;
    }

    public final boolean isDebugEnabled() {
        return debug;
    }

    protected final void setDebug(boolean debug) {
        this.debug = debug;
    }

    public final String getUserAgent() {
        return this.userAgent;
    }

    protected final void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
        initRequestHeaders();
    }

    public final String getUser() {
        return user;
    }

    protected final void setUser(String user) {
        this.user = user;
    }

    public final String getPassword() {
        return password;
    }

    protected final void setPassword(String password) {
        this.password = password;
    }

    public boolean isPrettyDebugEnabled() {
        return prettyDebug;
    }

    protected final void setUseSSL(boolean useSSL) {
        this.useSSL = useSSL;
        fixRestBaseURL();
    }

    protected final void setPrettyDebugEnabled(boolean prettyDebug) {
        this.prettyDebug = prettyDebug;
    }

    protected final void setGZIPEnabled(boolean gzipEnabled) {
        this.gzipEnabled = gzipEnabled;
        initRequestHeaders();
    }

    public boolean isGZIPEnabled() {
        return gzipEnabled;
    }

    // method for HttpRequestFactoryConfiguration
    Map<String, String> requestHeaders;

    private void initRequestHeaders() {
        requestHeaders = new HashMap<String, String>();
        requestHeaders.put("X-Twitter-Client-Version", getClientVersion());
        requestHeaders.put("X-Twitter-Client-URL", getClientURL());
        requestHeaders.put("X-Twitter-Client", "Twitter4J");

        requestHeaders.put("User-Agent", getUserAgent());
        if (gzipEnabled) {
            requestHeaders.put("Accept-Encoding", "gzip");
        }
        if (IS_DALVIK) {
            requestHeaders.put("Connection", "close");
        }
    }

    public Map<String, String> getRequestHeaders() {
        return requestHeaders;
    }

    // methods for HttpClientConfiguration

    public final String getHttpProxyHost() {
        return httpProxyHost;
    }

    protected final void setHttpProxyHost(String proxyHost) {
        this.httpProxyHost = proxyHost;
    }

    public final String getHttpProxyUser() {
        return httpProxyUser;
    }

    protected final void setHttpProxyUser(String proxyUser) {
        this.httpProxyUser = proxyUser;
    }

    public final String getHttpProxyPassword() {
        return httpProxyPassword;
    }

    protected final void setHttpProxyPassword(String proxyPassword) {
        this.httpProxyPassword = proxyPassword;
    }

    public final int getHttpProxyPort() {
        return httpProxyPort;
    }

    protected final void setHttpProxyPort(int proxyPort) {
        this.httpProxyPort = proxyPort;
    }

    public final int getHttpConnectionTimeout() {
        return httpConnectionTimeout;
    }

    protected final void setHttpConnectionTimeout(int connectionTimeout) {
        this.httpConnectionTimeout = connectionTimeout;
    }

    public final int getHttpReadTimeout() {
        return httpReadTimeout;
    }

    protected final void setHttpReadTimeout(int readTimeout) {
        this.httpReadTimeout = readTimeout;
    }

    public int getHttpStreamingReadTimeout() {
        return httpStreamingReadTimeout;
    }

    protected final void setHttpStreamingReadTimeout(int httpStreamingReadTimeout) {
        this.httpStreamingReadTimeout = httpStreamingReadTimeout;
    }


    public final int getHttpRetryCount() {
        return httpRetryCount;
    }

    protected final void setHttpRetryCount(int retryCount) {
        this.httpRetryCount = retryCount;
    }

    public final int getHttpRetryIntervalSeconds() {
        return httpRetryIntervalSeconds;
    }

    protected final void setHttpRetryIntervalSeconds(int retryIntervalSeconds) {
        this.httpRetryIntervalSeconds = retryIntervalSeconds;
    }

    public final int getHttpMaxTotalConnections() {
        return maxTotalConnections;
    }

    protected final void setHttpMaxTotalConnections(int maxTotalConnections) {
        this.maxTotalConnections = maxTotalConnections;
    }

    public final int getHttpDefaultMaxPerRoute() {
        return defaultMaxPerRoute;
    }

    protected final void setHttpDefaultMaxPerRoute(int defaultMaxPerRoute) {
        this.defaultMaxPerRoute = defaultMaxPerRoute;
    }

    // oauth related setter/getters

    public final String getOAuthConsumerKey() {
        return oAuthConsumerKey;
    }

    protected final void setOAuthConsumerKey(String oAuthConsumerKey) {
        this.oAuthConsumerKey = oAuthConsumerKey;
        fixRestBaseURL();
    }

    public final String getOAuthConsumerSecret() {
        return oAuthConsumerSecret;
    }

    protected final void setOAuthConsumerSecret(String oAuthConsumerSecret) {
        this.oAuthConsumerSecret = oAuthConsumerSecret;
        fixRestBaseURL();
    }

    public String getOAuthAccessToken() {
        return oAuthAccessToken;
    }

    protected final void setOAuthAccessToken(String oAuthAccessToken) {
        this.oAuthAccessToken = oAuthAccessToken;
    }

    public String getOAuthAccessTokenSecret() {
        return oAuthAccessTokenSecret;
    }

    protected final void setOAuthAccessTokenSecret(String oAuthAccessTokenSecret) {
        this.oAuthAccessTokenSecret = oAuthAccessTokenSecret;
    }

    public final int getAsyncNumThreads() {
        return asyncNumThreads;
    }

    protected final void setAsyncNumThreads(int asyncNumThreads) {
        this.asyncNumThreads = asyncNumThreads;
    }

    public final String getClientVersion() {
        return clientVersion;
    }

    protected final void setClientVersion(String clientVersion) {
        this.clientVersion = clientVersion;
        initRequestHeaders();
    }

    public final String getClientURL() {
        return clientURL;
    }

    protected final void setClientURL(String clientURL) {
        this.clientURL = clientURL;
        initRequestHeaders();
    }

    public String getRestBaseURL() {
        return restBaseURL;
    }

    protected final void setRestBaseURL(String restBaseURL) {
        this.restBaseURL = restBaseURL;
        fixRestBaseURL();
    }

    private void fixRestBaseURL() {
        if (DEFAULT_REST_BASE_URL.equals(fixURL(false, restBaseURL))) {
            if (null != oAuthConsumerKey && null != oAuthConsumerSecret) {
                this.restBaseURL = fixURL(false, restBaseURL);
            } else {
                this.restBaseURL = fixURL(useSSL, restBaseURL);
            }
        }
        if (DEFAULT_OAUTH_ACCESS_TOKEN_URL.equals(fixURL(false, oAuthAccessTokenURL))) {
            this.oAuthAccessTokenURL = fixURL(useSSL, oAuthAccessTokenURL);
        }
        if (DEFAULT_OAUTH_AUTHENTICATION_URL.equals(fixURL(false, oAuthAuthenticationURL))) {
            this.oAuthAuthenticationURL = fixURL(useSSL, oAuthAuthenticationURL);
        }
        if (DEFAULT_OAUTH_AUTHORIZATION_URL.equals(fixURL(false, oAuthAuthorizationURL))) {
            this.oAuthAuthorizationURL = fixURL(useSSL, oAuthAuthorizationURL);
        }
        if (DEFAULT_OAUTH_REQUEST_TOKEN_URL.equals(fixURL(false, oAuthRequestTokenURL))) {
            this.oAuthRequestTokenURL = fixURL(useSSL, oAuthRequestTokenURL);
        }
    }

    public String getSearchBaseURL() {
        return searchBaseURL;
    }

    protected final void setSearchBaseURL(String searchBaseURL) {
        this.searchBaseURL = searchBaseURL;
    }

    public String getStreamBaseURL() {
        return streamBaseURL;
    }

    protected final void setStreamBaseURL(String streamBaseURL) {
        this.streamBaseURL = streamBaseURL;
    }

    public String getUserStreamBaseURL() {
        return userStreamBaseURL;
    }

    protected final void setUserStreamBaseURL(String siteStreamBaseURL) {
        this.userStreamBaseURL = siteStreamBaseURL;
    }

    public String getSiteStreamBaseURL() {
        return siteStreamBaseURL;
    }

    protected final void setSiteStreamBaseURL(String siteStreamBaseURL) {
        this.siteStreamBaseURL = siteStreamBaseURL;
    }

    public String getOAuthRequestTokenURL() {
        return oAuthRequestTokenURL;
    }

    protected final void setOAuthRequestTokenURL(String oAuthRequestTokenURL) {
        this.oAuthRequestTokenURL = oAuthRequestTokenURL;
        fixRestBaseURL();
    }

    public String getOAuthAuthorizationURL() {
        return oAuthAuthorizationURL;
    }

    protected final void setOAuthAuthorizationURL(String oAuthAuthorizationURL) {
        this.oAuthAuthorizationURL = oAuthAuthorizationURL;
        fixRestBaseURL();
    }

    public String getOAuthAccessTokenURL() {
        return oAuthAccessTokenURL;
    }

    protected final void setOAuthAccessTokenURL(String oAuthAccessTokenURL) {
        this.oAuthAccessTokenURL = oAuthAccessTokenURL;
        fixRestBaseURL();
    }

    public String getOAuthAuthenticationURL() {
        return oAuthAuthenticationURL;
    }

    protected final void setOAuthAuthenticationURL(String oAuthAuthenticationURL) {
        this.oAuthAuthenticationURL = oAuthAuthenticationURL;
        fixRestBaseURL();
    }

    public String getDispatcherImpl() {
        return dispatcherImpl;
    }

    protected final void setDispatcherImpl(String dispatcherImpl) {
        this.dispatcherImpl = dispatcherImpl;
    }

    public boolean isIncludeRTsEnabled() {
        return this.includeRTsEnabled;
    }

    protected final void setIncludeRTsEnbled(boolean enabled) {
        this.includeRTsEnabled = enabled;
    }

    public boolean isIncludeEntitiesEnabled() {
        return this.includeEntitiesEnabled;
    }

    protected final void setIncludeEntitiesEnbled(boolean enabled) {
        this.includeEntitiesEnabled = enabled;
    }

    public boolean isUserStreamRepliesAllEnabled() {
        return this.userStreamRepliesAllEnabled;
    }

    protected final void setUserStreamRepliesAllEnabled(boolean enabled) {
        this.userStreamRepliesAllEnabled = enabled;
    }

    public String getMediaProvider() {
        return this.mediaProvider;
    }

    protected final void setMediaProvider(String mediaProvider) {
        this.mediaProvider = mediaProvider;
    }

    public String getMediaProviderAPIKey() {
        return this.mediaProviderAPIKey;
    }

    protected final void setMediaProviderAPIKey(String mediaProviderAPIKey) {
        this.mediaProviderAPIKey = mediaProviderAPIKey;
    }

    public Properties getMediaProviderParameters() {
        return this.mediaProviderParameters;
    }

    protected final void setMediaProviderParameters(Properties props) {
        this.mediaProviderParameters = props;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ConfigurationBase that = (ConfigurationBase) o;

        if (IS_DALVIK != that.IS_DALVIK) return false;
        if (asyncNumThreads != that.asyncNumThreads) return false;
        if (debug != that.debug) return false;
        if (defaultMaxPerRoute != that.defaultMaxPerRoute) return false;
        if (httpConnectionTimeout != that.httpConnectionTimeout) return false;
        if (httpProxyPort != that.httpProxyPort) return false;
        if (httpReadTimeout != that.httpReadTimeout) return false;
        if (httpRetryCount != that.httpRetryCount) return false;
        if (httpRetryIntervalSeconds != that.httpRetryIntervalSeconds)
            return false;
        if (httpStreamingReadTimeout != that.httpStreamingReadTimeout)
            return false;
        if (includeEntitiesEnabled != that.includeEntitiesEnabled) return false;
        if (includeRTsEnabled != that.includeRTsEnabled) return false;
        if (maxTotalConnections != that.maxTotalConnections) return false;
        if (prettyDebug != that.prettyDebug) return false;
        if (useSSL != that.useSSL) return false;
        if (userStreamRepliesAllEnabled != that.userStreamRepliesAllEnabled)
            return false;
        if (clientURL != null ? !clientURL.equals(that.clientURL) : that.clientURL != null)
            return false;
        if (clientVersion != null ? !clientVersion.equals(that.clientVersion) : that.clientVersion != null)
            return false;
        if (dispatcherImpl != null ? !dispatcherImpl.equals(that.dispatcherImpl) : that.dispatcherImpl != null)
            return false;
        if (httpProxyHost != null ? !httpProxyHost.equals(that.httpProxyHost) : that.httpProxyHost != null)
            return false;
        if (httpProxyPassword != null ? !httpProxyPassword.equals(that.httpProxyPassword) : that.httpProxyPassword != null)
            return false;
        if (httpProxyUser != null ? !httpProxyUser.equals(that.httpProxyUser) : that.httpProxyUser != null)
            return false;
        if (mediaProvider != null ? !mediaProvider.equals(that.mediaProvider) : that.mediaProvider != null)
            return false;
        if (mediaProviderAPIKey != null ? !mediaProviderAPIKey.equals(that.mediaProviderAPIKey) : that.mediaProviderAPIKey != null)
            return false;
        if (mediaProviderParameters != null ? !mediaProviderParameters.equals(that.mediaProviderParameters) : that.mediaProviderParameters != null)
            return false;
        if (oAuthAccessToken != null ? !oAuthAccessToken.equals(that.oAuthAccessToken) : that.oAuthAccessToken != null)
            return false;
        if (oAuthAccessTokenSecret != null ? !oAuthAccessTokenSecret.equals(that.oAuthAccessTokenSecret) : that.oAuthAccessTokenSecret != null)
            return false;
        if (oAuthAccessTokenURL != null ? !oAuthAccessTokenURL.equals(that.oAuthAccessTokenURL) : that.oAuthAccessTokenURL != null)
            return false;
        if (oAuthAuthenticationURL != null ? !oAuthAuthenticationURL.equals(that.oAuthAuthenticationURL) : that.oAuthAuthenticationURL != null)
            return false;
        if (oAuthAuthorizationURL != null ? !oAuthAuthorizationURL.equals(that.oAuthAuthorizationURL) : that.oAuthAuthorizationURL != null)
            return false;
        if (oAuthConsumerKey != null ? !oAuthConsumerKey.equals(that.oAuthConsumerKey) : that.oAuthConsumerKey != null)
            return false;
        if (oAuthConsumerSecret != null ? !oAuthConsumerSecret.equals(that.oAuthConsumerSecret) : that.oAuthConsumerSecret != null)
            return false;
        if (oAuthRequestTokenURL != null ? !oAuthRequestTokenURL.equals(that.oAuthRequestTokenURL) : that.oAuthRequestTokenURL != null)
            return false;
        if (password != null ? !password.equals(that.password) : that.password != null)
            return false;
        if (requestHeaders != null ? !requestHeaders.equals(that.requestHeaders) : that.requestHeaders != null)
            return false;
        if (restBaseURL != null ? !restBaseURL.equals(that.restBaseURL) : that.restBaseURL != null)
            return false;
        if (searchBaseURL != null ? !searchBaseURL.equals(that.searchBaseURL) : that.searchBaseURL != null)
            return false;
        if (siteStreamBaseURL != null ? !siteStreamBaseURL.equals(that.siteStreamBaseURL) : that.siteStreamBaseURL != null)
            return false;
        if (streamBaseURL != null ? !streamBaseURL.equals(that.streamBaseURL) : that.streamBaseURL != null)
            return false;
        if (user != null ? !user.equals(that.user) : that.user != null)
            return false;
        if (userAgent != null ? !userAgent.equals(that.userAgent) : that.userAgent != null)
            return false;
        if (userStreamBaseURL != null ? !userStreamBaseURL.equals(that.userStreamBaseURL) : that.userStreamBaseURL != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (debug ? 1 : 0);
        result = 31 * result + (userAgent != null ? userAgent.hashCode() : 0);
        result = 31 * result + (user != null ? user.hashCode() : 0);
        result = 31 * result + (password != null ? password.hashCode() : 0);
        result = 31 * result + (useSSL ? 1 : 0);
        result = 31 * result + (prettyDebug ? 1 : 0);
        result = 31 * result + (httpProxyHost != null ? httpProxyHost.hashCode() : 0);
        result = 31 * result + (httpProxyUser != null ? httpProxyUser.hashCode() : 0);
        result = 31 * result + (httpProxyPassword != null ? httpProxyPassword.hashCode() : 0);
        result = 31 * result + httpProxyPort;
        result = 31 * result + httpConnectionTimeout;
        result = 31 * result + httpReadTimeout;
        result = 31 * result + httpStreamingReadTimeout;
        result = 31 * result + httpRetryCount;
        result = 31 * result + httpRetryIntervalSeconds;
        result = 31 * result + maxTotalConnections;
        result = 31 * result + defaultMaxPerRoute;
        result = 31 * result + (oAuthConsumerKey != null ? oAuthConsumerKey.hashCode() : 0);
        result = 31 * result + (oAuthConsumerSecret != null ? oAuthConsumerSecret.hashCode() : 0);
        result = 31 * result + (oAuthAccessToken != null ? oAuthAccessToken.hashCode() : 0);
        result = 31 * result + (oAuthAccessTokenSecret != null ? oAuthAccessTokenSecret.hashCode() : 0);
        result = 31 * result + (oAuthRequestTokenURL != null ? oAuthRequestTokenURL.hashCode() : 0);
        result = 31 * result + (oAuthAuthorizationURL != null ? oAuthAuthorizationURL.hashCode() : 0);
        result = 31 * result + (oAuthAccessTokenURL != null ? oAuthAccessTokenURL.hashCode() : 0);
        result = 31 * result + (oAuthAuthenticationURL != null ? oAuthAuthenticationURL.hashCode() : 0);
        result = 31 * result + (restBaseURL != null ? restBaseURL.hashCode() : 0);
        result = 31 * result + (searchBaseURL != null ? searchBaseURL.hashCode() : 0);
        result = 31 * result + (streamBaseURL != null ? streamBaseURL.hashCode() : 0);
        result = 31 * result + (userStreamBaseURL != null ? userStreamBaseURL.hashCode() : 0);
        result = 31 * result + (siteStreamBaseURL != null ? siteStreamBaseURL.hashCode() : 0);
        result = 31 * result + (dispatcherImpl != null ? dispatcherImpl.hashCode() : 0);
        result = 31 * result + asyncNumThreads;
        result = 31 * result + (includeRTsEnabled ? 1 : 0);
        result = 31 * result + (userStreamRepliesAllEnabled ? 1 : 0);
        result = 31 * result + (clientVersion != null ? clientVersion.hashCode() : 0);
        result = 31 * result + (clientURL != null ? clientURL.hashCode() : 0);
        result = 31 * result + (IS_DALVIK ? 1 : 0);
        result = 31 * result + (requestHeaders != null ? requestHeaders.hashCode() : 0);
        result = 31 * result + (mediaProvider != null ? mediaProvider.hashCode() : 0);
        result = 31 * result + (mediaProviderAPIKey != null ? mediaProviderAPIKey.hashCode() : 0);
        result = 31 * result + (mediaProviderParameters != null ? mediaProviderParameters.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "ConfigurationBase{" +
                "debug=" + debug +
                ", userAgent='" + userAgent + '\'' +
                ", user='" + user + '\'' +
                ", password='" + password + '\'' +
                ", useSSL=" + useSSL +
                ", httpProxyHost='" + httpProxyHost + '\'' +
                ", httpProxyUser='" + httpProxyUser + '\'' +
                ", httpProxyPassword='" + httpProxyPassword + '\'' +
                ", httpProxyPort=" + httpProxyPort +
                ", httpConnectionTimeout=" + httpConnectionTimeout +
                ", httpReadTimeout=" + httpReadTimeout +
                ", httpStreamingReadTimeout=" + httpStreamingReadTimeout +
                ", httpRetryCount=" + httpRetryCount +
                ", httpRetryIntervalSeconds=" + httpRetryIntervalSeconds +
                ", maxTotalConnections=" + maxTotalConnections +
                ", defaultMaxPerRoute=" + defaultMaxPerRoute +
                ", oAuthConsumerKey='" + oAuthConsumerKey + '\'' +
                ", oAuthConsumerSecret='" + oAuthConsumerSecret + '\'' +
                ", oAuthAccessToken='" + oAuthAccessToken + '\'' +
                ", oAuthAccessTokenSecret='" + oAuthAccessTokenSecret + '\'' +
                ", oAuthRequestTokenURL='" + oAuthRequestTokenURL + '\'' +
                ", oAuthAuthorizationURL='" + oAuthAuthorizationURL + '\'' +
                ", oAuthAccessTokenURL='" + oAuthAccessTokenURL + '\'' +
                ", oAuthAuthenticationURL='" + oAuthAuthenticationURL + '\'' +
                ", restBaseURL='" + restBaseURL + '\'' +
                ", searchBaseURL='" + searchBaseURL + '\'' +
                ", streamBaseURL='" + streamBaseURL + '\'' +
                ", userStreamBaseURL='" + userStreamBaseURL + '\'' +
                ", siteStreamBaseURL='" + siteStreamBaseURL + '\'' +
                ", dispatcherImpl='" + dispatcherImpl + '\'' +
                ", asyncNumThreads=" + asyncNumThreads +
                ", includeRTsEnabled=" + includeRTsEnabled +
                ", includeEntitiesEnabled=" + includeEntitiesEnabled +
                ", userStreamRepliesAllEnabled=" + userStreamRepliesAllEnabled +
                ", clientVersion='" + clientVersion + '\'' +
                ", clientURL='" + clientURL + '\'' +
                ", IS_DALVIK=" + IS_DALVIK +
                ", requestHeaders=" + requestHeaders +
                ", mediaProvider=" + mediaProvider +
                ", mediaProviderAPIKey=" + mediaProviderAPIKey +
                ", mediaProviderParameters=" + mediaProviderParameters +
                '}';
    }

    static String fixURL(boolean useSSL, String url) {
        if (null == url) {
            return null;
        }
        int index = url.indexOf("://");
        if (-1 == index) {
            throw new IllegalArgumentException("url should contain '://'");
        }
        String hostAndLater = url.substring(index + 3);
        if (useSSL) {
            return "https://" + hostAndLater;
        } else {
            return "http://" + hostAndLater;
        }
    }
    //@todo implement readresolve to save memory usage
}
