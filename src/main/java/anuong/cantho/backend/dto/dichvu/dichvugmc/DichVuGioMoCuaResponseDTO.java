package anuong.cantho.backend.dto.dichvu.dichvugmc;

import java.time.LocalTime;

import lombok.*;

@Getter 
@Setter 
@NoArgsConstructor 
@AllArgsConstructor
public class DichVuGioMoCuaResponseDTO {

  private Long id;

  private Long dichVuId;

  private String tenDichVu;

  private Integer thu;

  private LocalTime gioMo;

  private LocalTime gioDong;

  private Boolean nghi;

  private String ghiChu;

}
