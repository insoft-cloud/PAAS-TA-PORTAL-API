package org.openpaas.paasta.portal.api.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.cloudfoundry.client.lib.CloudCredentials;
import org.cloudfoundry.client.lib.CloudFoundryClient;
import org.cloudfoundry.client.lib.CloudFoundryException;
import org.cloudfoundry.uaa.tokens.AbstractToken;
import org.openpaas.paasta.portal.api.common.Common;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.DefaultOAuth2RefreshToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.stereotype.Service;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class LoginServiceV1 extends Common {
    private static final Logger LOGGER = LoggerFactory.getLogger(LoginServiceV1.class);

    ConcurrentHashMap<String, OAuth2AccessToken> tokenCaches = new ConcurrentHashMap<>();
    
    TypeReference<Map<String, String>> typeRef = new TypeReference<Map<String, String>>(){};
    ObjectMapper objectMapper = new ObjectMapper();

    /**
     * id, password 방식으로 CloudFoundry 인증 토큰을 OAuth2AccessToken 형태로 반환한다.
     *
     * @param id       the id
     * @param password the password
     * @return OAuth2AccessToken o auth 2 access token
     * @throws MalformedURLException the malformed url exception
     * @throws URISyntaxException    the uri syntax exception
     */

    public OAuth2AccessToken login(String id, String password) throws MalformedURLException, URISyntaxException {
        CloudCredentials cc = new CloudCredentials(id, password);
        OAuth2AccessToken token = new CloudFoundryClient(cc, getTargetURL(apiTarget), true).login();
        tokenCaches.put(token.getValue(), token);
        return token;
    }

    public OAuth2AccessToken refresh(String token, String refreshToken) throws MalformedURLException, URISyntaxException {
        CloudCredentials cc = new CloudCredentials(getOAuth2Token(token, refreshToken), true);
        OAuth2AccessToken newToken = new CloudFoundryClient(cc, getTargetURL(apiTarget), true).login();

        return newToken;
    }

    public OAuth2AccessToken refresh(OAuth2AccessToken token) throws MalformedURLException, URISyntaxException {
        CloudCredentials cc = new CloudCredentials(token, true);
        OAuth2AccessToken newToken = new CloudFoundryClient(cc, getTargetURL(apiTarget), true).login();

        return newToken;
    }

    public OAuth2AccessToken refresh(String oldToken) throws MalformedURLException, URISyntaxException {
        if (tokenCaches.containsKey(oldToken)) {
            OAuth2AccessToken oAuthToken = tokenCaches.get(oldToken);
            final long current = System.currentTimeMillis();
            if (oAuthToken.getExpiration().getTime() - current <= 60_000L) {
                OAuth2AccessToken newOAuthToken = refresh(oAuthToken);
                tokenCaches.put(oldToken, newOAuthToken);
                LOGGER.info("Refresh token : {} ----> {}", oAuthToken.getValue(), newOAuthToken.getValue());
            }
            return oAuthToken;
        } else {
            LOGGER.error("Cannot refresh token without login : {}", oldToken);
            throw new CloudFoundryException(HttpStatus.BAD_REQUEST, "Cannot refresh token without login : " + oldToken);
        }
    }

    private final OAuth2AccessToken getOAuth2Token(String token, String refreshToken) {
        DefaultOAuth2AccessToken oAuthToken = new DefaultOAuth2AccessToken( token );
        oAuthToken.setRefreshToken( new DefaultOAuth2RefreshToken( refreshToken ) );
        
        return oAuthToken;
    }

    private final OAuth2AccessToken getOAuth2TokenFromTokenResponse(AbstractToken tokenResponse) {
        final Map<String, String> tokenMap = objectMapper.convertValue( tokenResponse, typeRef );
        return DefaultOAuth2AccessToken.valueOf( tokenMap );
    }



    /**
     * CF Target URL을 가져온다.
     *
     * @param target cf target
     * @return URL target
     * @throws MalformedURLException, URISyntaxException the exception
     */
    public URL getTargetURL(String target) throws MalformedURLException, URISyntaxException {
        return getTargetURI(target).toURL();
    }

    /**
     * CF Target URL을 가져온다.
     *
     * @param target cf target
     * @return URL target
     * @throws URISyntaxException
     */
    private URI getTargetURI(String target) throws URISyntaxException {
        return new URI(target);
    }
}
