package vn.com.atomi.loyalty.config.service.impl;

import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import vn.com.atomi.loyalty.base.data.BaseService;
import vn.com.atomi.loyalty.base.data.ResponsePage;
import vn.com.atomi.loyalty.base.exception.BaseException;
import vn.com.atomi.loyalty.base.utils.RequestUtils;
import vn.com.atomi.loyalty.config.dto.input.BudgetInput;
import vn.com.atomi.loyalty.config.dto.input.BudgetUpdateInput;
import vn.com.atomi.loyalty.config.dto.output.BudgetDetailOutput;
import vn.com.atomi.loyalty.config.dto.output.BudgetOutput;
import vn.com.atomi.loyalty.config.enums.BudgetStatus;
import vn.com.atomi.loyalty.config.enums.ErrorCode;
import vn.com.atomi.loyalty.config.enums.Status;
import vn.com.atomi.loyalty.config.feign.LoyaltyCoreClient;
import vn.com.atomi.loyalty.config.repository.BudgetRepository;
import vn.com.atomi.loyalty.config.repository.CampaignRepository;
import vn.com.atomi.loyalty.config.service.BudgetService;
import vn.com.atomi.loyalty.config.utils.Utils;

@Service
@RequiredArgsConstructor
public class BudgetServiceImpl extends BaseService implements BudgetService {
  private final BudgetRepository budgetRepository;
  private final CampaignRepository campaignRepository;
  private final LoyaltyCoreClient loyaltyCoreClient;

  @Override
  public void createBudget(BudgetInput budgetInput) {
    var startDate = Utils.convertToLocalDate(budgetInput.getStartDate());
    var endDate = Utils.convertToLocalDate(budgetInput.getEndDate());

    if (budgetRepository
        .findByDeletedFalseAndDecisionNumber(budgetInput.getDecisionNumber())
        .isPresent()) {
      throw new BaseException(ErrorCode.EXISTED_DECISION_NUMBER);
    }

    var budget = super.modelMapper.createBudget(budgetInput, startDate, endDate);
    budgetRepository.save(budget);
  }

  @Override
  public ResponsePage<BudgetOutput> getListBudget(
      String decisionNumber,
      String startDate,
      String endDate,
      BudgetStatus status,
      String name,
      Pageable pageable) {
    var page =
        budgetRepository.getListBudget(
            decisionNumber,
            status,
            name,
            Utils.convertToLocalDate(startDate),
            Utils.convertToLocalDate(endDate),
            pageable);
    return new ResponsePage<>(page, super.modelMapper.convertToBudgetOutPut(page.getContent()));
  }

  @Override
  public void updateBudget(BudgetUpdateInput budgetUpdateInput) {
    var budget =
        budgetRepository
            .findByDeletedFalseAndId(budgetUpdateInput.getId())
            .orElseThrow(() -> new BaseException(ErrorCode.RECORD_NOT_EXISTED));

    if (budgetUpdateInput.getName() != null) {
      budget.setName(budgetUpdateInput.getName());
    }

    if (!budgetUpdateInput.getStatus().equals(budget.getStatus()) && budget.getStatus() == Status.INACTIVE) {
      LocalDate currentDate = LocalDate.now();
      if (budget.getEndDate().isBefore(currentDate)) {
        throw new BaseException(ErrorCode.CONDITION_BUDGET_FAILED);
      }
      budget.setStatus(budgetUpdateInput.getStatus());
    }

    if (budgetUpdateInput.getTotalBudget() != null && budgetUpdateInput.getTotalBudget() > budget.getTotalBudget()) {
      budget.setTotalBudget(budgetUpdateInput.getTotalBudget());
    }
    budgetRepository.save(budget);
  }

  @Override
  public BudgetDetailOutput detailBudget(Long id) {
    var budget =
        budgetRepository
            .findByDeletedFalseAndId(id)
            .orElseThrow(() -> new BaseException(ErrorCode.RECORD_NOT_EXISTED));

//    var amountUsedTotal = loyaltyCoreClient.getAmountUsed(RequestUtils.extractRequestId(), id);
//    budget.setTotalBudget(amountUsedTotal.getData());
    return super.modelMapper.getDetailBudget(budget);
  }
}
