package vn.com.atomi.loyalty.config.service;

import vn.com.atomi.loyalty.config.dto.output.RulePOCOutput;

public interface RulePocService {

    RulePOCOutput findByType(String type);
}
