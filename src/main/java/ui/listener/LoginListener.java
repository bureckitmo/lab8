package ui.listener;


import database.CurrentUser;

public interface LoginListener {
    void onLoginSuccessful(CurrentUser user);
    void onLoginError(String message);
}
