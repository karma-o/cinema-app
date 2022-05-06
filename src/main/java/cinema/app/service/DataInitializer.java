package cinema.app.service;

import cinema.app.model.Role;
import cinema.app.model.User;
import java.util.Set;
import javax.annotation.PostConstruct;
import org.springframework.stereotype.Component;

/**
 * Class for initializing data in database.
 * Only for testing purposes.
 */
@Component
public class DataInitializer {
    private final UserService userService;
    private final RoleService roleService;

    public DataInitializer(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @PostConstruct
    public void inject() {
        System.out.println("Injecting mock admin");
        Role adminRole = new Role();
        adminRole.setName(Role.RoleName.ADMIN);
        roleService.add(adminRole);
        Role userRole = new Role();
        userRole.setName(Role.RoleName.USER);
        roleService.add(userRole);
        User user = new User();
        user.setEmail("admin@i.ua");
        user.setPassword("admin123");
        user.setRoles(Set.of(adminRole, userRole));
        userService.add(user);
    }
}
