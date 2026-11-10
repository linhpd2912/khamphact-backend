package anuong.cantho.backend.dto.dichvu;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
public class CapNhatGiaTriDanhMucDVDTO {

  @NotNull (message = "Giá trị trạng thái không được để trống")
  private Boolean giaTri;

}
