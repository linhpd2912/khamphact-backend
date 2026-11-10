package anuong.cantho.backend.repository.quanan;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import anuong.cantho.backend.entity.quanan.HinhAnhQuan;

@Repository
public interface HinhAnhQuanRepository extends JpaRepository<HinhAnhQuan, Long> {

  // =====================================================
  // 1. CÁC PHƯƠNG THỨC DÀNH CHO QUÁN ĂN
  // =====================================================

  long countByQuanAn_Id(Long quanAnId);

  boolean existsByQuanAn_Id(Long quanAnId);

  // Lấy tất cả ảnh của một quán, sắp xếp theo thứ tự tăng dần
  List<HinhAnhQuan> findByQuanAn_IdOrderByThuTuAscIdAsc(Long quanAnId);

  // Lấy tất cả ảnh, sắp xếp theo thứ tự tăng dần
  List<HinhAnhQuan> findAllByOrderByThuTuAscIdAsc();

  // Tìm các ảnh đang được đặt làm ảnh đại diện của một quán
  List<HinhAnhQuan> findByQuanAn_IdAndAnhDaiDienTrue(Long quanAnId);

  // Kiểm tra quán có ảnh đại diện hay chưa
  boolean existsByQuanAn_IdAndAnhDaiDienTrue(Long quanAnId);

  // =====================================================
  // 2. CÁC PHƯƠNG THỨC DÀNH CHO DỊCH VỤ
  // =====================================================

  long countByDichVu_Id(Long dichVuId);

  boolean existsByDichVu_Id(Long dichVuId);

  // Lấy tất cả ảnh của một dịch vụ, sắp xếp theo thứ tự tăng dần
  List<HinhAnhQuan> findByDichVu_IdOrderByThuTuAscIdAsc(Long dichVuId);

  // Tìm các ảnh đang được đặt làm ảnh đại diện của một dịch vụ
  List<HinhAnhQuan> findByDichVu_IdAndAnhDaiDienTrue(Long dichVuId);

  // Kiểm tra dịch vụ có ảnh đại diện hay chưa
  boolean existsByDichVu_IdAndAnhDaiDienTrue(Long dichVuId);
}
