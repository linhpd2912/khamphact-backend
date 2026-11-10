package anuong.cantho.backend.entity.quanan;

import anuong.cantho.backend.entity.dichvu.DichVu;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

@Entity
@Table(name = "hinh_anh_quan", indexes = {
    @Index(name = "idx_hinh_anh_quan_thu_tu", columnList = "quan_an_id, thu_tu"),
    @Index(name = "idx_hinh_anh_dich_vu_thu_tu", columnList = "dich_vu_id, thu_tu")
}, check = {
    @CheckConstraint(name = "chk_hinh_anh_quan_doi_tuong", constraint = """
            (quan_an_id IS NOT NULL AND dich_vu_id IS NULL)
            OR
            (quan_an_id IS NULL AND dich_vu_id IS NOT NULL)
        """)
})
@NoArgsConstructor
@Getter
@Setter
public class HinhAnhQuan {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

     // Liên kết quán ăn
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "quan_an_id")
    private QuanAn quanAn;

    // Liên kết dịch vụ
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dich_vu_id")
    private DichVu dichVu;

  @NotBlank(message = "URL ảnh không được để trống")
  @Size(max = 2048)
  @Column(name = "url_anh", nullable = false, length = 2048)
  private String urlAnh;

  @Size(max = 50)
  @Column(name = "loai_anh", length = 50)
  private String loaiAnh;

  @Size(max = 500)
  @Column(name = "chu_thich", length = 500)
  private String chuThich;

  @NotNull
  @Column(name = "anh_dai_dien", nullable = false)
  private Boolean anhDaiDien = false;

  @PositiveOrZero(message = "Thứ tự ảnh không được âm")
  @Column(name = "thu_tu", nullable = false)
  private Integer thuTu = 0;

  @jakarta.persistence.PrePersist
  protected void onCreate() {
    if (anhDaiDien == null) {
      anhDaiDien = false;
    }

    if (thuTu == null) {
      thuTu = 0;
    }
  }
}
