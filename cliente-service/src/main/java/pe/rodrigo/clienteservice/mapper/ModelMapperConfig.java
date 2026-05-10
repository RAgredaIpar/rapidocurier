package pe.rodrigo.clienteservice.mapper;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pe.rodrigo.clienteservice.dto.response.ClienteResponseDto;
import pe.rodrigo.clienteservice.entity.Cliente;

@Configuration
public class ModelMapperConfig {
    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();

        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        modelMapper.createTypeMap(Cliente.class, ClienteResponseDto.class);

        return modelMapper;
    }
}