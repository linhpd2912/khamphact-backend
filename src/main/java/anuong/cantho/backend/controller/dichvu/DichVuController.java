package anuong.cantho.backend.controller.dichvu;

import java.net.URI;

import org.springframework.data.domain.*;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
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

import anuong.cantho.backend.dto.dichvu.dichvu.CapNhatTrangThaiDichVuDTO;
import anuong.cantho.backend.dto.dichvu.dichvu.DichVuRequestDTO;
import anuong.cantho.backend.dto.dichvu.dichvu.DichVuResponseDTO;
import anuong.cantho.backend.service.dichvu.dichvu.DichVuService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController 
@RequestMapping ("/api/dich-vu")
@RequiredArgsConstructor 
@Validated 
public class DichVuController {

  private final DichVuService dichVuService;

  // =====================================================
  // 1. LẤY DANH SÁCH, TÌM KIẾM VÀ LỌC
  // =====================================================

  @GetMapping
  public ResponseEntity<Page<DichVuResponseDTO>> getAll(
      @RequestParam(required = false) String keyword,
      @RequestParam(required = false) Long tinhThanhId,
      @RequestParam(required = false) Long phuongXaId,
      @RequestParam(required = false) Boolean hoatDong,
      @RequestParam(required = false) Boolean noiBat,
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "10") int size) {
    Pageable pageable = taoPageable(page, size);

    return ResponseEntity.ok(
        dichVuService.getAll(
            keyword,
            tinhThanhId,
            phuongXaId,
            hoatDong,
            noiBat,
            pageable));
  }

  // =====================================================
  // 2. LẤY QUÁN ĐANG HOẠT ĐỘNG
  // =====================================================

  @GetMapping("/hoat-dong")
  public ResponseEntity<Page<DichVuResponseDTO>> getDichVuHoatDong(
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "10") int size) {
    Pageable pageable = taoPageable(page, size);

    return ResponseEntity.ok(
        dichVuService.getDichVuHoatDong(pageable));
  }

  // =====================================================
  // 3. LẤY CHI TIẾT THEO SLUG
  // =====================================================

  @GetMapping("/slug/{slug}")
  public ResponseEntity<DichVuResponseDTO> getBySlug(
      @PathVariable String slug) {
    return ResponseEntity.ok(
        dichVuService.getBySlug(slug));
  }

  // =====================================================
  // 4. LẤY CHI TIẾT THEO ID
  // =====================================================

  @GetMapping("/{id}")
  public ResponseEntity<DichVuResponseDTO> getById(
      @PathVariable Long id) {
    return ResponseEntity.ok(
        dichVuService.getById(id));
  }

  // =====================================================
  // 5. THÊM QUÁN ĂN
  // =====================================================

  @PostMapping
  public ResponseEntity<DichVuResponseDTO> create(
      @Valid @RequestBody DichVuRequestDTO request) {
    DichVuResponseDTO result = dichVuService.create(request);

    return ResponseEntity
        .created(URI.create("/api/dich-vu/" + result.getId()))
        .body(result);
  }

  // =====================================================
  // 6. SỬA QUÁN ĂN
  // =====================================================

  @PutMapping("/{id}")
  public ResponseEntity<DichVuResponseDTO> update(
      @PathVariable Long id,
      @Valid @RequestBody DichVuRequestDTO request) {
    return ResponseEntity.ok(
        dichVuService.update(id, request));
  }

  // =====================================================
  // 7. BẬT / TẮT HOẠT ĐỘNG
  // =====================================================

  @PatchMapping("/{id}/trang-thai")
  public ResponseEntity<DichVuResponseDTO> capNhatTrangThai(
      @PathVariable Long id,
      @Valid @RequestBody CapNhatTrangThaiDichVuDTO request) {
    return ResponseEntity.ok(
        dichVuService.capNhatTrangThai(id, request));
  }

  // =====================================================
  // 8. BẬT / TẮT NỔI BẬT
  // =====================================================

  @PatchMapping("/{id}/noi-bat")
  public ResponseEntity<DichVuResponseDTO> capNhatNoiBat(
      @PathVariable Long id,
      @Valid @RequestBody CapNhatTrangThaiDichVuDTO request) {
    return ResponseEntity.ok(
        dichVuService.capNhatNoiBat(id, request));
  }

  // =====================================================
  // 9. XÓA MỀM
  // =====================================================

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> xoaMem(
      @PathVariable Long id) {
    dichVuService.xoaMem(id);

    return ResponseEntity.noContent().build();
  }

  // =====================================================
  // 10. KHÔI PHỤC
  // =====================================================

  @PostMapping("/{id}/khoi-phuc")
  public ResponseEntity<DichVuResponseDTO> khoiPhuc(
      @PathVariable Long id) {
    return ResponseEntity.ok(
        dichVuService.khoiPhuc(id));
  }

  // =====================================================
  // 11. PHÂN TRANG VÀ SẮP XẾP
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
          "Kích thước trang phải từ 1 đến 100");
    }

    return PageRequest.of(
        page,
        size,
        Sort.by(
            Sort.Order.desc("ngayTao"),
            Sort.Order.desc("id")));
  }

}
