package anuong.cantho.backend.repository.quanan;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import anuong.cantho.backend.entity.quanan.GioMoCua;

@Repository
public interface GioMoCuaRepository extends JpaRepository<GioMoCua, Long> {

    // =====================================================
    // 1. CÁC PHƯƠNG THỨC DÀNH CHO QUÁN ĂN
    // =====================================================

    long countByQuanAn_Id(Long quanAnId);

    boolean existsByQuanAn_Id(Long quanAnId);

    // Lấy toàn bộ giờ mở cửa của một quán.
    List<GioMoCua> findByQuanAn_IdOrderByThuAsc(Long quanAnId);

    // Kiểm tra quán đã có giờ mở cửa của thứ này chưa.
    boolean existsByQuanAn_IdAndThu(Long quanAnId, Integer thu);

    // Kiểm tra trùng thứ khi cập nhật, bỏ qua chính bản ghi đang sửa.
    boolean existsByQuanAn_IdAndThuAndIdNot(
            Long quanAnId,
            Integer thu,
            Long id);

    // Tìm giờ mở cửa theo ID và ID quán.
    Optional<GioMoCua> findByIdAndQuanAn_Id(
            Long id,
            Long quanAnId);

    // Lấy dữ liệu kèm tên quán, tránh truy vấn riêng cho từng tên quán.
    @Query("""
            SELECT g
            FROM GioMoCua g
            JOIN FETCH g.quanAn q
            WHERE q.id = :quanAnId
            ORDER BY g.thu ASC
            """)
    List<GioMoCua> timTheoQuanAn(
            @Param("quanAnId") Long quanAnId);

    // Lấy tất cả bản ghi kèm thông tin quán.
    @Query("""
            SELECT g
            FROM GioMoCua g
            JOIN FETCH g.quanAn q
            ORDER BY q.id ASC, g.thu ASC
            """)
    List<GioMoCua> timTatCa();

    // =====================================================
    // 2. CÁC PHƯƠNG THỨC DÀNH CHO DỊCH VỤ
    // =====================================================

    // Đếm số bản ghi giờ hoạt động của dịch vụ.
    long countByDichVu_Id(Long dichVuId);

    // Kiểm tra dịch vụ đã có giờ hoạt động chưa.
    boolean existsByDichVu_Id(Long dichVuId);

    // Lấy toàn bộ giờ hoạt động của một dịch vụ.
    List<GioMoCua> findByDichVu_IdOrderByThuAsc(Long dichVuId);

    // Kiểm tra dịch vụ đã có giờ hoạt động của thứ này chưa.
    boolean existsByDichVu_IdAndThu(
        Long dichVuId,
        Integer thu
    );

    // Kiểm tra trùng thứ khi cập nhật, bỏ qua bản ghi đang sửa.
    boolean existsByDichVu_IdAndThuAndIdNot(
        Long dichVuId,
        Integer thu,
        Long id
    );

    // Tìm giờ hoạt động theo ID và ID dịch vụ.
    Optional<GioMoCua> findByIdAndDichVu_Id(
        Long id,
        Long dichVuId
    );

    // Lấy dữ liệu kèm thông tin dịch vụ.
    @Query("""
        SELECT g
        FROM GioMoCua g
        JOIN FETCH g.dichVu d
        WHERE d.id = :dichVuId
        ORDER BY g.thu ASC
        """)
    List<GioMoCua> timTheoDichVu(
        @Param("dichVuId") Long dichVuId
    );

    // Lấy tất cả bản ghi giờ hoạt động của dịch vụ.
    @Query("""
        SELECT g
        FROM GioMoCua g
        JOIN FETCH g.dichVu d
        ORDER BY d.id ASC, g.thu ASC
        """)
    List<GioMoCua> timTatCaDichVu();
}
