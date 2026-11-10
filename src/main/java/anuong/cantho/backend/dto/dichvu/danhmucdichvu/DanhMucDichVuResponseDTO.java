package anuong.cantho.backend.dto.dichvu.danhmucdichvu;

import lombok.*;

@NoArgsConstructor 
@AllArgsConstructor
@Getter
@Setter 
public class DanhMucDichVuResponseDTO {

  private Long id;

  private String tenDanhMuc;

  private String duongDanSeo;

  private String moTa;

  private String icon;

  private Integer thuTuHienThi;

  private Boolean noiBat;

  private Boolean hoatDong;

}
