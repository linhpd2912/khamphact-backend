package anuong.cantho.backend.dto.dichvu.dichvuhinhanh;

import anuong.cantho.backend.entity.quanan.HinhAnhQuan;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DichVuHinhAnhResponseDTO {

  private Long id;
  private Long dichVuId;
  private String urlAnh;
  private String loaiAnh;
  private String chuThich;
  private Boolean anhDaiDien;
  private Integer thuTu;

  public static DichVuHinhAnhResponseDTO fromEntity(HinhAnhQuan entity) {
    return new DichVuHinhAnhResponseDTO(
        entity.getId(),
        entity.getDichVu().getId(),
        entity.getUrlAnh(),
        entity.getLoaiAnh(),
        entity.getChuThich(),
        entity.getAnhDaiDien(),
        entity.getThuTu());
  }

}
