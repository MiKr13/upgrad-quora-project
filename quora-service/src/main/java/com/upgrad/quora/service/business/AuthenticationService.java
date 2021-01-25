package com.upgrad.quora.service.business;

import com.upgrad.quora.service.dao.UserDao;
import com.upgrad.quora.service.entity.UserAuthTokenEntity;
import com.upgrad.quora.service.entity.UserEntity;
import com.upgrad.quora.service.exception.AuthenticationFailedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import com.upgrad.quora.service.entity.UserAuthTokenEntity;
import com.upgrad.quora.service.exception.SignOutRestrictedException;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;

@Service
public class AuthenticationService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private PasswordCryptographyProvider cryptographyProvider;

    /**
     * This method helps authenticate user
     *
     * @param username username of user
     * @param password password of user
     *
     * @return authentication token as UserAuthEntity object
     *
     * @throws AuthenticationFailedException if user authentication fails
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public UserAuthTokenEntity authenticate(final String username, final String password)
            throws AuthenticationFailedException {
        UserEntity userEntity = userDao.getUserByUserName(username);

        // Validate if requested user name exist or not
        if (userEntity == null) {
            throw new AuthenticationFailedException("ATH-001", "This username does not exist");
        }

        final String encryptedPassword = cryptographyProvider.encrypt(password, userEntity.getSalt());

        // Validate user given password
        if (encryptedPassword.equals(userEntity.getPassword())) {
            JwtTokenProvider jwtTokenProvider = new JwtTokenProvider(encryptedPassword);
            UserAuthTokenEntity userAuthToken = new UserAuthTokenEntity();
            userAuthToken.setUser(userEntity);
            final ZonedDateTime now = ZonedDateTime.now();
            final ZonedDateTime expiresAt = now.plusHours(8);
            userAuthToken.setUuid(userEntity.getUuid());
            userAuthToken.setAccessToken(jwtTokenProvider.generateToken(userEntity.getUuid(), now, expiresAt));
            userAuthToken.setLoginAt(now);
            userAuthToken.setExpiresAt(expiresAt);

            // Create authentication token
            userDao.createAuthToken(userAuthToken);
            userDao.updateUser(userEntity);

            return userAuthToken;
        } else {
            throw new AuthenticationFailedException("ATH-002", "Password failed");
        }
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public UserEntity signup(UserEntity userEntity) {
        String[] encryptedText = cryptographyProvider.encrypt(userEntity.getPassword());
        userEntity.setSalt(encryptedText[0]);
        userEntity.setPassword(encryptedText[1]);

        return userDao.createUser(userEntity);
    }

    /**
     * @param accessToken the first {@code String} to signout a user.
     * @return List of QuestionEntity objects.
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public UserAuthTokenEntity signOutService(String accessToken) throws SignOutRestrictedException {
        UserAuthTokenEntity userAuthTokenEntity = null;
        // check user sign in or not
        userAuthTokenEntity = userDao.getUserAuthToken(accessToken);
        if (userAuthTokenEntity != null) {
            final ZonedDateTime now = ZonedDateTime.now();
            userAuthTokenEntity.setLogoutAt(now);
            userAuthTokenEntity = userDao.updateUserLogOut(userAuthTokenEntity);
        } else {
            // if user is not sign in then throws exception
            throw new SignOutRestrictedException("SGR-001", "User is not Signed in");
        }
        return userAuthTokenEntity;
    }

}
