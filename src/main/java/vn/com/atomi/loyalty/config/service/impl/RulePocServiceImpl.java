package vn.com.atomi.loyalty.config.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.com.atomi.loyalty.base.data.BaseService;
import vn.com.atomi.loyalty.config.entity.RulePOC;
import vn.com.atomi.loyalty.config.repository.RulePOCRepository;
import vn.com.atomi.loyalty.config.service.RulePocService;

@RequiredArgsConstructor
@Service
public class RulePocServiceImpl extends BaseService implements RulePocService {

    private final RulePOCRepository rulePOCRepository;

    public RulePOC findByType(String type) {
        return rulePOCRepository.findByDeletedFalseAndType(type).orElse(null);
    }

}
