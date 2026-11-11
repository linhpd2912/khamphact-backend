package anuong.cantho.backend.entity.quanan;

import java.time.LocalTime;

import anuong.cantho.backend.entity.dichvu.DichVu;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

@Entity
@Table(name = "gio_mo_cua", uniqueConstraints = {
    @UniqueConstraint(name = "uk_gio_mo_cua_quan_thu", columnNames = { "quan_an_id", "thu" }),
    @UniqueConstraint(name = "uk_gio_mo_cua_dich_vu_thu", columnNames = { "dich_vu_id", "thu" })
}, indexes = {
    @Index(name = "idx_gio_mo_cua_quan", columnList = "quan_an_id"),
    @Index(name = "idx_gio_mo_cua_dich_vu", columnList = "dich_vu_id")
})
@NoArgsConstructor
@Getter
@Setter
public class GioMoCua {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  // Liên kết với quán ăn
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "quan_an_id", nullable = true)
  private QuanAn quanAn;

  // Liên kết với dịch vụ
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "dich_vu_id", nullable = true)
  private DichVu dichVu;

  @NotNull(message = "Vui lòng chọn thứ trong tuần")
  @Min(value = 1, message = "Thứ phải nằm trong khoảng 1 đến 7")
  @Max(value = 7, message = "Thứ phải nằm trong khoảng 1 đến 7")
  @Column(name = "thu", nullable = false)
  private Integer thu;

  @Column(name = "gio_mo")
  private LocalTime gioMo;

  @Column(name = "gio_dong")
  private LocalTime gioDong;

  @NotNull
  @Column(name = "nghi", nullable = false)
  private Boolean nghi = false;

  @Size(max = 500)
  @Column(name = "ghi_chu", length = 500)
  private String ghiChu;

  @jakarta.persistence.PrePersist
  protected void onCreate() {
    if (nghi == null) {
      nghi = false;
    }
  }

}
