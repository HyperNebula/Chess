package service;

import java.util.Objects;
import java.util.UUID;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.UserDAO;
import model.DataModel.*;
import model.RequestModal.*;
import model.ResultModal.*;

public class UserService {

    private UserDAO userDB;
    private AuthDAO authDB;

    public UserService(UserDAO userDB, AuthDAO authDB) {
        this.userDB = userDB;
        this.authDB = authDB;
    }

    public static String generateToken() {
        return UUID.randomUUID().toString();
    }

    public RegisterResult register(RegisterRequest registerRequest) throws AlreadyTakenException, DataAccessException {
        if (registerRequest.username() == null || registerRequest.password() == null || registerRequest.email() == null) {
            throw new BadRequestException("Error: bad request");
        }

        if (userDB.getUser(registerRequest.username()) != null) {
            throw new AlreadyTakenException("Error: already taken");
        }
        userDB.createUser(new UserData(registerRequest.username(), registerRequest.password(), registerRequest.email()));

        String tempAuth = generateToken();

        authDB.createAuth(new AuthData(tempAuth, registerRequest.username()));
        return new RegisterResult(registerRequest.username(), tempAuth);
    }

    public LoginResult login(LoginRequest loginRequest) throws UnauthorizedException, DataAccessException {
        if (loginRequest.username() == null || loginRequest.password() == null) {
            throw new BadRequestException("Error: bad request");
        }

        UserData tempUserData = userDB.getUser(loginRequest.username());

        if (tempUserData == null) {
            throw new UnauthorizedException("Error: unauthorized");
        }

        if (!Objects.equals(tempUserData.password(), loginRequest.password())) {
            throw new UnauthorizedException("Error: unauthorized");
        }

        String tempAuth = generateToken();

        authDB.createAuth(new AuthData(tempAuth, loginRequest.username()));
        return new LoginResult(loginRequest.username(), tempAuth);

    }

    public LogoutResult logout(LogoutRequest logoutRequest) throws UnauthorizedException, DataAccessException {
        AuthData tempAuthData = authDB.getAuth(logoutRequest.authToken());

        if (tempAuthData == null) {
            throw new UnauthorizedException("Error: unauthorized");
        }

        authDB.deleteAuth(tempAuthData);

        return new LogoutResult(true);
    }

    public void deleteAllUsers() throws DataAccessException {
        userDB.deleteAll();
        authDB.deleteAll();
    }
}
