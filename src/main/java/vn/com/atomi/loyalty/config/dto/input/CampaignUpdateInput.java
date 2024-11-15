package vn.com.atomi.loyalty.config.dto.input;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import vn.com.atomi.loyalty.base.annotations.DateTimeValidator;
import vn.com.atomi.loyalty.base.constant.DateConstant;
import vn.com.atomi.loyalty.config.enums.ApprovalStatus;
import vn.com.atomi.loyalty.config.enums.Status;

/**
 * @author haidv
 * @version 1.0
 */
@Setter
@Getter
public class CampaignUpdateInput {
    @Schema(description = "ID bản ghi chờ duyệt")
    @NotNull
    private Long id;

    @Schema(description = "Tên chiến dịch")
    @NotBlank
    @Size(max = 168)
    private String name;

    @Schema(description = "Mô tả")
    @NotBlank
    private String description;

    @Schema(description = "Mã chiến dịch")
    @NotBlank
    private String code;

    @Schema(description = "Trạng thái:</br> ACTIVE: Hiệu lực</br> INACTIVE: Không hiệu lực")
    private Status status;

    @Schema(description = "ngan sach chien dich")
    @NotNull
    private Long budgetAmount;

    @Schema(description = "id ngan sach")
    @NotNull
    private Long budgetId;

    @Schema(description = "Trạng thái duyet")
    @NotNull
    private ApprovalStatus approvalStatus;
}
