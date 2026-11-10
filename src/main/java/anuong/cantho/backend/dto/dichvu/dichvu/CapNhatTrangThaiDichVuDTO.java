package anuong.cantho.backend.dto.dichvu.dichvu;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter 
@Setter 
@NoArgsConstructor 
public class CapNhatTrangThaiDichVuDTO {

  @NotNull (message = "Giá trị trạng thái không được để trống")
  private Boolean giaTri;

}
