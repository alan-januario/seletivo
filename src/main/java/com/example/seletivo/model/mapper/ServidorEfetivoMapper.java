package com.example.seletivo.model.mapper;

import com.example.seletivo.model.dto.ServidorEfetivoDTO;
import com.example.seletivo.model.entity.ServidorEfetivo;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {PessoaMapper.class})
public interface ServidorEfetivoMapper {
    ServidorEfetivoDTO toDto(ServidorEfetivo servidorEfetivo);
    ServidorEfetivo toEntity(ServidorEfetivoDTO dto);
}
