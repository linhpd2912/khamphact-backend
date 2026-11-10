package anuong.cantho.backend.entity.dichvu;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@Table (name = "dich_vu_danh_muc", uniqueConstraints = {
    @UniqueConstraint (name = "uk_dich_vu_danh_muc", columnNames = { "dich_vu_id", "danh_muc_dich_vu_id" })
}, indexes = {
    @Index (name = "idx_dvdm_danh_muc", columnList = "danh_muc_dich_vu_id")
})
@Getter 
@Setter 
@NoArgsConstructor 
@AllArgsConstructor 
public class DichVuDanhMuc {

  @Id 
  @GeneratedValue (strategy = GenerationType.IDENTITY)
  private Long id;

  @NotNull (message = "Dịch vụ không được để trống")
  @ManyToOne (fetch = FetchType.LAZY, optional = false)
  @JoinColumn (name = "dich_vu_id", nullable = false)
  private DichVu dichVu;

  @NotNull (message = "Danh mục dịch vụ không được để trống")
  @ManyToOne (fetch = FetchType.LAZY, optional = false)
  @JoinColumn (name = "danh_muc_dich_vu_id", nullable = false)
  private DanhMucDichVu danhMucDichVu;
}
