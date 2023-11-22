public class CurrentUser {
    private static User user = null;

    // Sets the current user
    public static void setUser(User newUser) {
        user = newUser;
    }

    // Gets the current user
    public static User getUser() {
        return user;
    }

    // Clears the current user (for logout)
    public static void clearUser() {
        user = null;
    }
}
