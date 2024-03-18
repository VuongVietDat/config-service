package vn.com.atomi.loyalty.config.service;

import org.springframework.data.domain.Pageable;
import vn.com.atomi.loyalty.base.data.ResponsePage;
import vn.com.atomi.loyalty.config.dto.output.RankOutput;
import vn.com.atomi.loyalty.config.enums.Status;

/**
 * @author haidv
 * @version 1.0
 */
public interface RankService {

  ResponsePage<RankOutput> getRanks(Status status, Pageable pageable);
}
