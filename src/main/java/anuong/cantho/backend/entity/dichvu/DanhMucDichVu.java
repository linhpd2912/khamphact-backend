package anuong.cantho.backend.entity.dichvu;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table (name = "danh_muc_dich_vu", uniqueConstraints = {
  @UniqueConstraint (name = "uk_danh_muc_dich_vu_slug", columnNames = "duong_dan_seo")
}, indexes = {
  @Index (name = "idx_danh_muc_dich_vu_hoat_dong", columnList = "hoat_dong")
})
@Getter 
@Setter 
@NoArgsConstructor 
public class DanhMucDichVu {

  @Id 
  @GeneratedValue (strategy = GenerationType.IDENTITY)
  private Long id;

  @NotBlank (message = "Tên danh mục không được để trống")
  @Size (max = 255)
  @Column (name = "ten_danh_muc", nullable = false, length = 255)
  private String tenDanhMuc;

  @Size (max = 255)
  @Column (name = "duong_dan_seo", length = 255)
  private String duongDanSeo;

  
  private String moTa;

  private String icon;

  private Integer thuTuHienThi = 0;

  private Boolean noiBat = false;

  private Boolean hoatDong = true;



}
