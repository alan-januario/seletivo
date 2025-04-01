package com.example.seletivo.model.mapper;

import com.example.seletivo.model.dto.UnidadeDTO;
import com.example.seletivo.model.entity.Unidade;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, unmappedSourcePolicy = ReportingPolicy.IGNORE, nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS, uses = {EnderecoMapper.class})
public interface UnidadeMapper {
        
    UnidadeDTO toDto(Unidade unidade);
        
    Unidade toEntity(UnidadeDTO dto);
}
