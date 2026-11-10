package anuong.cantho.backend.dto.dichvu.dichvutienich;

import lombok.*;

@Getter 
@Setter 
@NoArgsConstructor
@AllArgsConstructor
public class DichVuTienIchResponseDTO {

  private Long id;

  private Long dichVuId;

  private Long tienIchId;

  private String tenTienIch;

  private String icon;

  private Boolean hoatDong;

}
