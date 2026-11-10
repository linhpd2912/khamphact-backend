package anuong.cantho.backend.entity.quanan;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@Table(name = "quan_an_danh_muc", uniqueConstraints = {
    @UniqueConstraint(name = "uk_quan_an_danh_muc", columnNames = { "quan_an_id", "danh_muc_id" })
}, indexes = {
    @Index(name = "idx_qadm_danh_muc", columnList = "danh_muc_id")
})
@NoArgsConstructor
@AllArgsConstructor 
@Getter
@Setter
public class QuanAnDanhMuc {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotNull(message = "Quán ăn không được để trống")
  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "quan_an_id", nullable = false)
  private QuanAn quanAn;

  @NotNull(message = "Danh mục không được để trống")
  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "danh_muc_id", nullable = false)
  private DanhMuc danhMuc;
}
