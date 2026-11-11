package anuong.cantho.backend.service.dichvu.dichvudanhmuc;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import anuong.cantho.backend.dto.dichvu.dichvudanhmuc.DichVuDanhMucRequestDTO;
import anuong.cantho.backend.dto.dichvu.dichvudanhmuc.DichVuDanhMucResponseDTO;
import anuong.cantho.backend.entity.dichvu.DanhMucDichVu;
import anuong.cantho.backend.entity.dichvu.DichVu;
import anuong.cantho.backend.entity.dichvu.DichVuDanhMuc;
import anuong.cantho.backend.repository.dichvu.DanhMucDichVuRepository;
import anuong.cantho.backend.repository.dichvu.DichVuDanhMucRepository;
import anuong.cantho.backend.repository.dichvu.DichVuRepository;
import lombok.RequiredArgsConstructor;

@Service 
@RequiredArgsConstructor 
public class DichVuDanhMucService {

  private final DichVuDanhMucRepository dichVuDanhMucRepository;
  private final DichVuRepository dichVuRepository;
  private final DanhMucDichVuRepository danhMucDichVuRepository;

  // Chuyển Entity thành ResponseDTO
    private DichVuDanhMucResponseDTO toResponseDTO(
            DichVuDanhMuc dichVuDanhMuc) {
        return new DichVuDanhMucResponseDTO(
                dichVuDanhMuc.getId(),
                dichVuDanhMuc.getDichVu().getId(),
                dichVuDanhMuc.getDanhMucDichVu().getId());
    }

    // Lấy tất cả liên kết quán - danh mục
    @Transactional (readOnly = true)
    public List<DichVuDanhMucResponseDTO> layTatCa() {
        return dichVuDanhMucRepository.findAll()
                .stream()
                .map(this::toResponseDTO)
                .toList();
    }

    // Lấy danh mục của một dịch vụ
    @Transactional (readOnly = true)
    public List<DichVuDanhMucResponseDTO> layTheoDichVu(
            Long dichVuId) {
        if (!dichVuRepository.existsById(dichVuId)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Không tìm thấy dịch vụ có ID: " + dichVuId);
        }

        return dichVuDanhMucRepository.findByDichVu_Id(dichVuId)
                .stream()
                .map(this::toResponseDTO)
                .toList();
    }

    // Lấy các dịch vụ thuộc một danh mục
    @Transactional(readOnly = true)
    public List<DichVuDanhMucResponseDTO> layTheoDanhMuc(
            Long danhMucDichVuId) {
        if (!danhMucDichVuRepository.existsById(danhMucDichVuId)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Không tìm thấy danh mục có ID: " + danhMucDichVuId);
        }

        return dichVuDanhMucRepository.findByDanhMucDichVu_Id(danhMucDichVuId)
                .stream()
                .map(this::toResponseDTO)
                .toList();
    }

    // Thêm liên kết dịch vụ - danh mục
    @Transactional
    public DichVuDanhMucResponseDTO them(
            DichVuDanhMucRequestDTO request) {
        Long dichVuId = request.getDichVuId();
        Long danhMucDichVuId = request.getDanhMucDichVuId();

        // Kiểm tra dịch vụ tồn tại
        DichVu dichVu = dichVuRepository.findById(dichVuId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Không tìm thấy dịch vụ có ID: " + dichVuId));

        if (!Boolean.TRUE.equals(dichVu.getHoatDong())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Dịch vụ đã ngưng hoạt động, không thể thêm danh mục.");
        }

        // Kiểm tra danh mục tồn tại
        DanhMucDichVu danhMucDichVu = danhMucDichVuRepository.findById(danhMucDichVuId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Không tìm thấy danh mục có ID: " + danhMucDichVuId));

        // Không cho phép thêm liên kết trùng
        if (dichVuDanhMucRepository
                .existsByDichVu_IdAndDanhMucDichVu_Id(dichVuId, danhMucDichVuId)) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Dịch vụ đã được gán danh mục này");
        }

        DichVuDanhMuc entity = new DichVuDanhMuc();
        entity.setDichVu(dichVu);
        entity.setDanhMucDichVu(danhMucDichVu);

        DichVuDanhMuc saved = dichVuDanhMucRepository.save(entity);

        return toResponseDTO(saved);
    }

    // Cập nhật liên kết
    @Transactional
    public DichVuDanhMucResponseDTO capNhat(
            Long id,
            DichVuDanhMucRequestDTO request) {
        DichVuDanhMuc entity = dichVuDanhMucRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Không tìm thấy liên kết có ID: " + id));

        Long dichVuId = request.getDichVuId();
        Long danhMucDichVuId = request.getDanhMucDichVuId();

        DichVu dichVu = dichVuRepository.findById(dichVuId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Không tìm thấy dịch vụ có ID: " + dichVuId));

        DanhMucDichVu danhMucDichVu = danhMucDichVuRepository.findById(danhMucDichVuId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Không tìm thấy danh mục có ID: " + danhMucDichVuId));

        // Kiểm tra liên kết trùng với bản ghi khác
        if (dichVuDanhMucRepository
                .existsByDichVu_IdAndDanhMucDichVu_IdAndIdNot(
                        dichVuId, danhMucDichVuId, id)) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Dịch vụ đã được gán danh mục này");
        }

        entity.setDichVu(dichVu);
        entity.setDanhMucDichVu(danhMucDichVu);

        DichVuDanhMuc saved = dichVuDanhMucRepository.save(entity);

        return toResponseDTO(saved);
    }

    // Xóa liên kết, không xóa quán hay danh mục
    @Transactional
    public void xoa(Long id) {
        DichVuDanhMuc entity = dichVuDanhMucRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Không tìm thấy liên kết có ID: " + id));

        dichVuDanhMucRepository.delete(entity);
    }

}
