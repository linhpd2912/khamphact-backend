package anuong.cantho.backend.dto.dichvu.dichvudanhmuc;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter 
@Setter 
@NoArgsConstructor 
@AllArgsConstructor 
public class DichVuDanhMucRequestDTO {

  @NotNull (message = "Dịch vụ không được để trống")
  private Long dichVuId;

  @NotNull (message = "Danh mục không được để trống")
  private Long danhMucDichVuId;

}
