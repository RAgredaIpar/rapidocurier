package pe.rodrigo.paqueteservice.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import pe.rodrigo.paqueteservice.entity.Categoria;
import pe.rodrigo.paqueteservice.repository.CategoriaRepository;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final CategoriaRepository categoriaRepository;

    @Override
    public void run(String... args) {
        if (categoriaRepository.count() == 0) {

            Categoria fragil = crear("FRAGIL", "Requiere embalaje protector y trato manual", 10.0);
            Categoria perecible = crear("PERECIBLE", "Requiere cadena de frío o ventilación", 15.0);
            Categoria granPeso = crear("GRAN_PESO", "Paquetes de +20kg que requieren montacargas", 25.0);
            Categoria documentos = crear("DOCUMENTOS", "Sobres o valijas de alta seguridad", 5.0);
            Categoria quimicos = crear("QUIMICOS", "Manejo de materiales peligrosos/restringidos", 40.0);
            Categoria express = crear("EXPRESS", "Prioridad máxima de entrega (menos de 24h)", 20.0);

            categoriaRepository.saveAll(List.of(fragil, perecible, granPeso, documentos, quimicos, express));
            System.out.println(">> Catálogo logístico de 6 categorías inicializado correctamente.");
        }
    }

    private Categoria crear(String nombre, String desc, Double recargo) {
        Categoria c = new Categoria();
        c.setNombre(nombre);
        c.setDescripcion(desc);
        c.setRecargo(recargo);
        return c;
    }
}