package pe.rodrigo.common.security;

public class UserContext {
    private static final ThreadLocal<String> CURRENT_EMAIL = new ThreadLocal<>();
    private static final ThreadLocal<String> CURRENT_ROLE = new ThreadLocal<>();

    public static void setEmail(String email) { CURRENT_EMAIL.set(email); }
    public static String getEmail() { return CURRENT_EMAIL.get(); }

    public static void setRole(String role) { CURRENT_ROLE.set(role); }
    public static String getRole() { return CURRENT_ROLE.get(); }

    public static void clear() {
        CURRENT_EMAIL.remove();
        CURRENT_ROLE.remove();
    }
}