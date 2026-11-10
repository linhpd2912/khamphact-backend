package anuong.cantho.backend.entity.dichvu;

import anuong.cantho.backend.entity.quanan.TienIch;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity 
@Table (name = "dich_vu_tien_ich", uniqueConstraints = {
    @UniqueConstraint (name = "uk_dich_vu_tien_ich", columnNames = { "dich_vu_id", "tien_ich_id" })
}, indexes = {
    @Index (name = "idx_dvti_tien_ich", columnList = "tien_ich_id")
})
@Getter
@Setter
@NoArgsConstructor 
@AllArgsConstructor 
public class DichVuTienIch {

  @Id 
  @GeneratedValue (strategy = GenerationType.IDENTITY)
  private Long id;

  @NotNull (message = "Dich vu không được để trống")
  @ManyToOne (fetch = FetchType.LAZY, optional = false)
  @JoinColumn (name = "dich_vu_id", nullable = false)
  private DichVu dichVu;

  @NotNull(message = "Tiện ích không được để trống")
  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "tien_ich_id", nullable = false)
  private TienIch tienIch;


}
