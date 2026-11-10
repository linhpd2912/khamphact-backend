package anuong.cantho.backend.dto.dichvu.dichvugmc;

import java.time.LocalTime;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter 
@Setter 
@NoArgsConstructor 
@AllArgsConstructor 
public class DichVuGioMoCuaRequestDTO {

  @NotNull (message = "Vui lòng chọn dịch vụ")
  private Long dichVuId;

  @NotNull (message = "Vui lòng chọn thứ trong tuần")
  @Min (value = 1, message = "Thứ phải nằm trong khoảng 1 đến 7")
  @Max (value = 7, message = "Thứ phải nằm trong khoảng 1 đến 7") 
  private Integer thu;

  private LocalTime gioMo;

  private LocalTime gioDong;

  @NotNull (message = "Vui lòng xác định dịch vụ có nghỉ hay không")
  private Boolean nghi;

  @Size (max = 500, message = "Ghi chú không được vượt quá 500 ký tự")
  private String ghiChu;

}
