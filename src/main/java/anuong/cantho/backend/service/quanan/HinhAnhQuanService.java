package anuong.cantho.backend.service.quanan;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import anuong.cantho.backend.dto.dichvu.dichvuhinhanh.DichVuHinhAnhResponseDTO;
import anuong.cantho.backend.dto.quanan.hinhanhquan.HinhAnhQuanResponseDTO;

public interface HinhAnhQuanService {

    // Quan

    // Lấy tất cả hình ảnh
    List<HinhAnhQuanResponseDTO> layTatCa();

    // Lấy hình ảnh theo quán ăn
    List<HinhAnhQuanResponseDTO> layTheoQuanAn(Long quanAnId);

    // Tải lên một hoặc nhiều hình ảnh
    List<HinhAnhQuanResponseDTO> taiLen(
            Long quanAnId,
            MultipartFile[] files,
            String chuThich);

    // Chọn ảnh đại diện
    HinhAnhQuanResponseDTO datAnhDaiDien(Long id);

    // Cập nhật thứ tự ảnh
    HinhAnhQuanResponseDTO capNhatThuTu(
            Long id,
            Integer thuTu);

    // Xóa ảnh
    void xoa(Long id);

    // Dich Vu

    List<DichVuHinhAnhResponseDTO> layTatCaDichVu();

    List<DichVuHinhAnhResponseDTO> layTheoDichVu(Long dichVuId);

    List<DichVuHinhAnhResponseDTO> taiLenAnhDichVu(
            Long dichVuId,
            MultipartFile[] files,
            String chuThich

    );

    DichVuHinhAnhResponseDTO datAnhDaiDienDichVu(Long id);

    DichVuHinhAnhResponseDTO capNhatThuTuDichVu(
            Long id, Integer thuTu);

    void xoaDichVuHinhAnh(Long id);

}
