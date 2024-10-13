package com.univesp.projeto_integrador.service;

import com.univesp.projeto_integrador.dto.PromotionDTO;
import com.univesp.projeto_integrador.exception.ResourceNotFoundException;
import com.univesp.projeto_integrador.model.Promotion;
import com.univesp.projeto_integrador.repository.PromotionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PromotionService {

    @Autowired
    private PromotionRepository promotionRepository;

    // Criar promoção
    public PromotionDTO createPromotion(PromotionDTO promotionDTO) {
        Promotion promotion = dtoToEntity(promotionDTO);
        promotion = promotionRepository.save(promotion);
        return entityToDto(promotion);
    }

    // Listar todas as promoções
    public List<PromotionDTO> getAllPromotions() {
        List<Promotion> promotions = promotionRepository.findAll();
        return promotions.stream().map(this::entityToDto).toList();
    }

    // Buscar promoção por ID
    public PromotionDTO getPromotionById(Long id) {
        Promotion promotion = promotionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Promoção não encontrada com id " + id));
        return entityToDto(promotion);
    }

    // Atualizar promoção
    public PromotionDTO updatePromotion(Long id, PromotionDTO promotionDTO) {
        Promotion existingPromotion = promotionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Promoção não encontrada com id " + id));

        existingPromotion.setPromotionDescription(promotionDTO.getPromotionDescription());
        existingPromotion.setStartDate(promotionDTO.getStartDate());
        existingPromotion.setEndDate(promotionDTO.getEndDate());
        existingPromotion.setDiscountPercentage(promotionDTO.getDiscountPercentage());
        existingPromotion.setStatus(promotionDTO.getStatus());

        promotionRepository.save(existingPromotion);
        return entityToDto(existingPromotion);
    }

    // Deletar promoção
    public void deletePromotion(Long id) {
        promotionRepository.deleteById(id);
    }

    // Métodos auxiliares para converter entre DTO e entidade
    public Promotion dtoToEntity(PromotionDTO promotionDTO) {
        Promotion promotion = new Promotion();
        promotion.setPromotionId(promotionDTO.getPromotionId());
        promotion.setPromotionDescription(promotionDTO.getPromotionDescription());
        promotion.setStartDate(promotionDTO.getStartDate());
        promotion.setEndDate(promotionDTO.getEndDate());
        promotion.setDiscountPercentage(promotionDTO.getDiscountPercentage());
        promotion.setStatus(promotionDTO.getStatus());
        return promotion;
    }


    PromotionDTO entityToDto(Promotion promotion) {
        PromotionDTO dto = new PromotionDTO();
        dto.setPromotionId(promotion.getPromotionId());
        dto.setPromotionDescription(promotion.getPromotionDescription());
        dto.setStartDate(promotion.getStartDate());
        dto.setEndDate(promotion.getEndDate());
        dto.setDiscountPercentage(promotion.getDiscountPercentage());
        dto.setStatus(promotion.getStatus());
        return dto;
    }
}

