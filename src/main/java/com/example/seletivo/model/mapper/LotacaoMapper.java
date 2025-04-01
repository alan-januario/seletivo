package com.example.seletivo.model.mapper;

import com.example.seletivo.model.dto.LotacaoDTO;
import com.example.seletivo.model.entity.Lotacao;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {PessoaMapper.class, UnidadeMapper.class})
public interface LotacaoMapper {
    LotacaoDTO toDto(Lotacao lotacao);
    Lotacao toEntity(LotacaoDTO dto);
}
