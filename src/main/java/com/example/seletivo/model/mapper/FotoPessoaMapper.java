package com.example.seletivo.model.mapper;

import com.example.seletivo.model.dto.FotoPessoaDTO;
import com.example.seletivo.model.entity.FotoPessoa;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface FotoPessoaMapper {
    
    @Mapping(target = "pessoaId", source = "pessoa.id")
    @Mapping(target = "pessoaNome", source = "pessoa.nome")
    @Mapping(target = "url", ignore = true)
    FotoPessoaDTO toDto(FotoPessoa fotoPessoa);
    
    @Mapping(target = "pessoa.id", source = "pessoaId")
    FotoPessoa toEntity(FotoPessoaDTO dto);
}
