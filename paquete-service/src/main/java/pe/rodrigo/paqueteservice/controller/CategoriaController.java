package pe.rodrigo.paqueteservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.rodrigo.common.dto.ApiResponse;
import pe.rodrigo.paqueteservice.dto.response.CategoriaResponseDto;
import pe.rodrigo.paqueteservice.repository.CategoriaRepository;
import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/categorias")
@RequiredArgsConstructor
public class CategoriaController {

    private final CategoriaRepository categoriaRepository;
    private final ModelMapper modelMapper;

    @GetMapping
    public ResponseEntity<ApiResponse<List<CategoriaResponseDto>>> listar() {
        List<CategoriaResponseDto> data = categoriaRepository.findAll().stream()
                .map(c -> modelMapper.map(c, CategoriaResponseDto.class))
                .collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponse.success("Categorías obtenidas", data));
    }
}