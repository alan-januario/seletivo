package com.example.seletivo.model.mapper;

import com.example.seletivo.model.dto.ServidorTemporarioDTO;
import com.example.seletivo.model.entity.ServidorTemporario;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {PessoaMapper.class})
public interface ServidorTemporarioMapper {
    ServidorTemporarioDTO toDto(ServidorTemporario servidorTemporario);
    ServidorTemporario toEntity(ServidorTemporarioDTO dto);
}