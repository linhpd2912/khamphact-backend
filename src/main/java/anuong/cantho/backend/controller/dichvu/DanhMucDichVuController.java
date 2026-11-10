package anuong.cantho.backend.controller.dichvu;

import java.net.URI;
import java.util.List;

import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import anuong.cantho.backend.dto.dichvu.danhmucdichvu.DanhMucDichVuRequestDTO;
import anuong.cantho.backend.dto.dichvu.danhmucdichvu.DanhMucDichVuResponseDTO;
import anuong.cantho.backend.dto.dichvu.danhmucdichvu.CapNhatGiaTriDanhMucDVDTO;
import anuong.cantho.backend.service.dichvu.DanhMucDichVuService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/danh-muc-dich-vu")
@RequiredArgsConstructor
public class DanhMucDichVuController {

  private final DanhMucDichVuService danhMucDichVuService;

  // =====================================================
  // LẤY DANH SÁCH CÓ PHÂN TRANG
  // =====================================================

  @GetMapping
  public ResponseEntity<Page<DanhMucDichVuResponseDTO>> getAll(
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "10") int size) {

    Pageable pageable = taoPageable(page, size);

    return ResponseEntity.ok(
        danhMucDichVuService.getAll(pageable));
  }

  // =====================================================
  // LẤY TOÀN BỘ DANH MỤC
  // =====================================================

  @GetMapping("/all")
  public ResponseEntity<List<DanhMucDichVuResponseDTO>> getAll() {
    return ResponseEntity.ok(
        danhMucDichVuService.getAll());
  }

  // =====================================================
  // DANH MỤC ĐANG HOẠT ĐỘNG
  // =====================================================

  @GetMapping("/hoat-dong")
  public ResponseEntity<List<DanhMucDichVuResponseDTO>> getDanhMucHoatDong() {
    return ResponseEntity.ok(
        danhMucDichVuService.getDanhMucHoatDong());
  }

  // =====================================================
  // DANH MỤC NỔI BẬT
  // =====================================================

  @GetMapping("/noi-bat")
  public ResponseEntity<List<DanhMucDichVuResponseDTO>> getDanhMucNoiBat() {
    return ResponseEntity.ok(
        danhMucDichVuService.getDanhMucNoiBat());
  }

  // =====================================================
    // TÌM KIẾM
    // =====================================================

    @GetMapping("/tim-kiem")
    public ResponseEntity<Page<DanhMucDichVuResponseDTO>> timKiem(
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = taoPageable(page, size);

        return ResponseEntity.ok(
            danhMucDichVuService.timKiem(keyword, pageable)
        );
    }

    // =====================================================
    // CHI TIẾT THEO SLUG
    // =====================================================

    @GetMapping("/slug/{slug}")
    public ResponseEntity<DanhMucDichVuResponseDTO> getBySlug(
            @PathVariable String slug) {

        return ResponseEntity.ok(
            danhMucDichVuService.getBySlug(slug)
        );
    }

    // =====================================================
    // CHI TIẾT THEO ID
    // =====================================================

    @GetMapping("/{id:\\d+}")
    public ResponseEntity<DanhMucDichVuResponseDTO> getById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
            danhMucDichVuService.getById(id)
        );
    }

    // =====================================================
    // THÊM MỚI
    // =====================================================

    @PostMapping
    public ResponseEntity<DanhMucDichVuResponseDTO> create(
            @Valid @RequestBody DanhMucDichVuRequestDTO request) {

        DanhMucDichVuResponseDTO result = danhMucDichVuService.create(request);

        return ResponseEntity
            .created(URI.create("/api/danh-muc-dich-vu/" + result.getId()))
            .body(result);
    }

    // =====================================================
    // CẬP NHẬT
    // =====================================================

    @PutMapping("/{id:\\d+}")
    public ResponseEntity<DanhMucDichVuResponseDTO> update(
            @PathVariable Long id,
            @Valid @RequestBody DanhMucDichVuRequestDTO request) {

        return ResponseEntity.ok(
            danhMucDichVuService.update(id, request)
        );
    }

    // =====================================================
    // CẬP NHẬT TRẠNG THÁI
    // =====================================================

    @PatchMapping("/{id:\\d+}/trang-thai")
    public ResponseEntity<DanhMucDichVuResponseDTO> capNhatTrangThai(
            @PathVariable Long id,
            @Valid @RequestBody CapNhatGiaTriDanhMucDVDTO request) {

        return ResponseEntity.ok(
            danhMucDichVuService.capNhatTrangThai(id, request)
        );
    }

    // =====================================================
    // CẬP NHẬT NỔI BẬT
    // =====================================================

    @PatchMapping("/{id:\\d+}/noi-bat")
    public ResponseEntity<DanhMucDichVuResponseDTO> capNhatNoiBat(
            @PathVariable Long id,
            @Valid @RequestBody CapNhatGiaTriDanhMucDVDTO request) {

        return ResponseEntity.ok(
            danhMucDichVuService.capNhatNoiBat(id, request)
        );
    }

    // =====================================================
    // XÓA
    // =====================================================

    @DeleteMapping("/{id:\\d+}")
    public ResponseEntity<Void> delete(
            @PathVariable Long id) {

        danhMucDichVuService.delete(id);

        return ResponseEntity.noContent().build();
    }

  // =====================================================
  // VALIDATE PHÂN TRANG
  // =====================================================

  private Pageable taoPageable(int page, int size) {
    if (page < 0) {
      throw new org.springframework.web.server.ResponseStatusException(
          HttpStatus.BAD_REQUEST,
          "Số trang không được âm");
    }

    if (size < 1 || size > 100) {
      throw new org.springframework.web.server.ResponseStatusException(
          HttpStatus.BAD_REQUEST,
          "Kích thước trang phải nằm trong khoảng 1 đến 100");
    }

    return PageRequest.of(
        page,
        size,
        Sort.by(
            Sort.Order.asc("thuTuHienThi"),
            Sort.Order.asc("id")));
  }
}
