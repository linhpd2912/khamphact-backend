package anuong.cantho.backend.service.dichvu;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import anuong.cantho.backend.dto.dichvu.CapNhatGiaTriDanhMucDVDTO;
import anuong.cantho.backend.dto.dichvu.DanhMucDichVuResponseDTO;

public interface DanhMucDichVuService {

  // Danh sách có phân trang
  Page<DanhMucDichVuResponseDTO> getAll(Pageable pageable);

  // Danh sách đầy đủ
  Page<DanhMucDichVuResponseDTO> getAll();

  // Chi tiết theo ID
  DanhMucDichVuResponseDTO getById(Long id);

  // Chi tiết theo slug
  DanhMucDichVuResponseDTO getBySlug(String slug);

  // Danh mục đang hoạt động
  List<DanhMucDichVuResponseDTO> getDanhMucHoatDong();

  // Danh mục nổi bật
  List<DanhMucDichVuResponseDTO> getDanhMucNoiBat();

  // Tìm kiếm có phân trang
  Page<DanhMucDichVuResponseDTO> timKiem(
      String keyword,
      Pageable pageable
  );

  // Thêm mới
  DanhMucDichVuResponseDTO create(DanhMucDichVuResponseDTO request);

  // Cập nhật
  DanhMucDichVuResponseDTO update(Long id, DanhMucDichVuResponseDTO request);

  // Cập nhật trạng thái hoạt động
  DanhMucDichVuResponseDTO capNhatTrangThai(
      Long id,
      CapNhatGiaTriDanhMucDVDTO request
  );

  // Cập nhật trạng thái nổi bật
  DanhMucDichVuResponseDTO capNhatNoiBat(
      Long id,
      CapNhatGiaTriDanhMucDVDTO request
  );  

  // Xóa danh mục
  void delete(Long id);

}
