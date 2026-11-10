package anuong.cantho.backend.repository.dichvu;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import anuong.cantho.backend.entity.dichvu.DichVuDanhMuc;

@Repository 
public interface DichVuDanhMucRepository extends JpaRepository<DichVuDanhMuc, Long> {
  // Kiểm tra danh mục có đang được liên kết với dịch vụ hay không
  boolean existsByDanhMucDichVu_Id(Long danhMucDichVuId);

  // Đếm số liên kết để hiển thị thông tin nếu cần
  long countByDanhMucDichVu_Id(Long danhMucDichVuId);

  long countByDichVu_Id(Long dichVuId);

  boolean existsByDichVu_Id(Long dichVuId);

  // Lấy tất cả danh mục được gán cho một dịch vụ
  List<DichVuDanhMuc> findByDichVu_Id(Long dichVuId);

  // Lấy tất cả dịch vụ thuộc một danh mục
  List<DichVuDanhMuc> findByDanhMucDichVu_Id(Long danhMucDichVuId);

  // Kiểm tra một dịch vụ đã có danh mục này chưa
  boolean existsByDichVu_IdAndDanhMucDichVu_Id(
      Long dichVuId,
      Long danhMucDichVuId);

  // Kiểm tra trùng khi cập nhật, loại trừ bản ghi đang sửa
  boolean existsByDichVu_IdAndDanhMucDichVu_IdAndIdNot(
      Long dichVuId,
      Long danhMucDichVuId,
      Long id);
  

}
