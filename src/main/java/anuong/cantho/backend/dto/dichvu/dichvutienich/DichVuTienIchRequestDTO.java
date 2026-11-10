package anuong.cantho.backend.dto.dichvu.dichvutienich;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DichVuTienIchRequestDTO {

  @NotNull(message = "ID dịch vụ không được để trống")
  @Positive(message = "ID dịch vụ phải lớn hơn 0")
  private Long dichVuId;

  @NotNull(message = "ID tiện ích không được để trống")
  @Positive(message = "ID tiện ích phải lớn hơn 0")
  private Long tienIchId;

}
