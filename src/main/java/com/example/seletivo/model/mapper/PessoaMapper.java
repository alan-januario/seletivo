package com.example.seletivo.model.mapper;

import com.example.seletivo.model.dto.PessoaDTO;
import com.example.seletivo.model.entity.Pessoa;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE, unmappedSourcePolicy = ReportingPolicy.IGNORE, nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS, uses = {EnderecoMapper.class})
public interface PessoaMapper {
    PessoaDTO toDto(Pessoa pessoa);
      
    Pessoa toEntity(PessoaDTO dto);
}
