package vn.com.atomi.loyalty.config.service;

import vn.com.atomi.loyalty.config.entity.RulePOC;

public interface RulePocService {

    RulePOC findByType(String type);
}
