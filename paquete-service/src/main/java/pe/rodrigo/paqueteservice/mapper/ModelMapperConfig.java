package pe.rodrigo.paqueteservice.mapper;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pe.rodrigo.paqueteservice.dto.response.PaqueteResponseDto;
import pe.rodrigo.paqueteservice.dto.response.CategoriaResponseDto;
import pe.rodrigo.paqueteservice.entity.Paquete;
import pe.rodrigo.paqueteservice.entity.Categoria;

@Configuration
public class ModelMapperConfig {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();

        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        modelMapper.createTypeMap(Paquete.class, PaqueteResponseDto.class);
        modelMapper.createTypeMap(Categoria.class, CategoriaResponseDto.class);

        return modelMapper;
    }
}