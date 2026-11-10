package anuong.cantho.backend.repository.dichvu;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import anuong.cantho.backend.entity.dichvu.DichVuDanhMuc;

@Repository 
public interface DichVuDanhMucRepository extends JpaRepository<DichVuDanhMuc, Long> {
  // Kiểm tra danh mục có đang được liên kết với dịch vụ hay không
  boolean existsByDanhMuc_Id(Long danhMucId);

  // Đếm số liên kết để hiển thị thông tin nếu cần
  long countByDanhMuc_Id(Long danhMucId);

  long countByDichVu_Id(Long dichVuId);

  boolean existsByDichVu_Id(Long dichVuId);

  // Lấy tất cả danh mục được gán cho một dịch vụ
  List<DichVuDanhMuc> findByDichVu_Id(Long dichVuId);

  // Lấy tất cả dịch vụ thuộc một danh mục
  List<DichVuDanhMuc> findByDanhMuc_Id(Long danhMucId);

  // Kiểm tra một dịch vụ đã có danh mục này chưa
  boolean existsByDichVu_IdAndDanhMuc_Id(
      Long dichVuId,
      Long danhMucId);

  // Kiểm tra trùng khi cập nhật, loại trừ bản ghi đang sửa
  boolean existsByDichVu_IdAndDanhMuc_IdAndIdNot(
      Long dichVuId,
      Long danhMucId,
      Long id);
  

}
