package com.example.seletivo.model.mapper;

import com.example.seletivo.model.dto.EnderecoDTO;
import com.example.seletivo.model.entity.Endereco;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, unmappedSourcePolicy = ReportingPolicy.IGNORE, nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS, uses = {CidadeMapper.class})
public interface EnderecoMapper {
   
    EnderecoDTO toDto(Endereco endereco);
   
    Endereco toEntity(EnderecoDTO dto);
}
