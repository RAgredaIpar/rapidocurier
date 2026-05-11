package pe.rodrigo.securityserver.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import pe.rodrigo.securityserver.entity.Role;
import pe.rodrigo.securityserver.entity.User;
import pe.rodrigo.securityserver.repository.UserRepository;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        if (userRepository.count() == 0) {
            User admin = new User();
            admin.setNombre("Admin General");
            admin.setEmail("admin@rapidocourier.pe");
            admin.setPassword(passwordEncoder.encode("Segura123!"));
            admin.setRole(Role.ADMIN);
            userRepository.save(admin);

            User operador = new User();
            operador.setNombre("Operador Central");
            operador.setEmail("operador@rapidocourier.pe");
            operador.setPassword(passwordEncoder.encode("Segura123!"));
            operador.setRole(Role.OPERADOR);
            userRepository.save(operador);

            User cliente1 = new User();
            cliente1.setNombre("Rodrigo Cliente");
            cliente1.setEmail("rodrigo@rapidocourier.pe");
            cliente1.setPassword(passwordEncoder.encode("Segura123!"));
            cliente1.setRole(Role.CLIENTE);
            userRepository.save(cliente1);

            User cliente2 = new User();
            cliente2.setNombre("Natali Cliente");
            cliente2.setEmail("natali@rapidocourier.pe");
            cliente2.setPassword(passwordEncoder.encode("Segura123!"));
            cliente2.setRole(Role.CLIENTE);
            userRepository.save(cliente2);

            System.out.println("Usuarios base creados en la BD exitosamente!");
        }
    }
}