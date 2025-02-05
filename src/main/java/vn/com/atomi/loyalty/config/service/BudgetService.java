package vn.com.atomi.loyalty.config.service;

import org.springframework.data.domain.Pageable;
import vn.com.atomi.loyalty.base.data.ResponsePage;
import vn.com.atomi.loyalty.config.dto.input.BudgetInput;
import vn.com.atomi.loyalty.config.dto.input.BudgetUpdateInput;
import vn.com.atomi.loyalty.config.dto.output.BudgetDetailOutput;
import vn.com.atomi.loyalty.config.dto.output.BudgetOutput;
import vn.com.atomi.loyalty.config.enums.BudgetStatus;
import vn.com.atomi.loyalty.config.enums.Status;

public interface BudgetService {
  void createBudget(BudgetInput budgetInput);

  ResponsePage<BudgetOutput> getListBudget(
      String decisionNumber,
      String startDate,
      String endDate,
      BudgetStatus status,
      String name,
      Pageable pageable);

  

  BudgetDetailOutput detailBudget(Long id);

  void updateBudget(BudgetUpdateInput budgetUpdateInput);
}
