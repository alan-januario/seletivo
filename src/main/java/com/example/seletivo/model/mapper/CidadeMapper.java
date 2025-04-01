package com.example.seletivo.model.mapper;

import com.example.seletivo.model.dto.CidadeDTO;
import com.example.seletivo.model.entity.Cidade;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CidadeMapper {
    CidadeDTO toDto(Cidade cidade);
    Cidade toEntity(CidadeDTO dto);
}
