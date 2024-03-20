package vn.com.atomi.loyalty.config.service.impl;

import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import vn.com.atomi.loyalty.base.data.BaseService;
import vn.com.atomi.loyalty.base.data.ResponsePage;
import vn.com.atomi.loyalty.config.dto.output.RankOutput;
import vn.com.atomi.loyalty.config.enums.Status;
import vn.com.atomi.loyalty.config.repository.RankRepository;
import vn.com.atomi.loyalty.config.service.RankService;

/**
 * @author haidv
 * @version 1.0
 */
@RequiredArgsConstructor
@Service
public class RankServiceImpl extends BaseService implements RankService {

  private final RankRepository rankRepository;

  @Override
  public ResponsePage<RankOutput> getRanks(Status status, Pageable pageable) {
    var rankPage = rankRepository.findByCondition(status, pageable);
    if (CollectionUtils.isEmpty(rankPage.getContent()))
      return new ResponsePage<>(rankPage, new ArrayList<>());

    return new ResponsePage<>(rankPage, modelMapper.convertToRankOutputs(rankPage.getContent()));
  }

  @Override
  public List<RankOutput> getAllRanks() {
    return modelMapper.convertToRankOutputs(rankRepository.findByDeletedFalse());
  }
}
