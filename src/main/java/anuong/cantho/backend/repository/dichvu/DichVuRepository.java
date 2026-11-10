package anuong.cantho.backend.repository.dichvu;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import anuong.cantho.backend.entity.dichvu.DichVu;

@Repository 
public interface DichVuRepository extends JpaRepository<DichVu, Long>{

  boolean existsByDuongDanSeo(String duongDanSeo);

  boolean existsByDuongDanSeoAndIdNot(String duongDanSeo, Long id);

  Optional<DichVu> findByDuongDanSeo(String duongDanSeo);

  Page<DichVu> findByHoatDongTrue(Pageable pageable);

  @Query ("""
    SELECT d
    FROM DichVu d
    WHERE
        (
            :keyword IS NULL
            OR LOWER(d.tenDichVu)
                LIKE LOWER(CONCAT('%', :keyword, '%'))
            OR LOWER(COALESCE(d.diaChi, ''))
                  LIKE LOWER(CONCAT('%', :keyword, '%'))
              OR LOWER(COALESCE(d.moTaNgan, ''))
                  LIKE LOWER(CONCAT('%', :keyword, '%'))
        )
        AND (:tinhThanhId IS NULL OR d.tinhThanh.id = :tinhThanhId)
        AND (:phuongXaId IS NULL OR d.phuongXa.id = :phuongXaId)
        AND (:hoatDong IS NULL OR d.hoatDong = :hoatDong)
        AND (:noiBat IS NULL OR d.noiBat = :noiBat)
    """)

    Page<DichVu> timKiemVaLoc(
        @Param ("keyword") String keyword,
        @Param("tinhThanhId") Long tinhThanhId,
        @Param("phuongXaId") Long phuongXaId,
        @Param("hoatDong") Boolean hoatDong,
        @Param("noiBat") Boolean noiBat,
        Pageable pageable);


}
