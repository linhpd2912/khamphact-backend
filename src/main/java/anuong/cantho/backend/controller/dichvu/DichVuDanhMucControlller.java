package anuong.cantho.backend.controller.dichvu;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import anuong.cantho.backend.dto.dichvu.dichvudanhmuc.DichVuDanhMucRequestDTO;
import anuong.cantho.backend.dto.dichvu.dichvudanhmuc.DichVuDanhMucResponseDTO;
import anuong.cantho.backend.service.dichvu.dichvudanhmuc.DichVuDanhMucService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController 
@RequestMapping ("/api/dich-vu-danh-muc")
@RequiredArgsConstructor 
public class DichVuDanhMucControlller {

  private final DichVuDanhMucService dichVuDanhMucService;

  // GET: Lấy tất cả liên kết
  @GetMapping
  public ResponseEntity<List<DichVuDanhMucResponseDTO>> layTatCa() {
    return ResponseEntity.ok(dichVuDanhMucService.layTatCa());
  }

  // GET: Lấy danh mục của một dịch vụ
  @GetMapping("/dich-vu/{dichVuId}")
  public ResponseEntity<List<DichVuDanhMucResponseDTO>> layTheoDichVu(
      @PathVariable Long dichVuId) {
    return ResponseEntity.ok(
        dichVuDanhMucService.layTheoDichVu(dichVuId));
  }

  // GET: Lấy các dịch vụ thuộc một danh mục
  @GetMapping("/danh-muc/{danhMucId}")
  public ResponseEntity<List<DichVuDanhMucResponseDTO>> layTheoDanhMuc(
      @PathVariable Long danhMucId) {
    return ResponseEntity.ok(
        dichVuDanhMucService.layTheoDanhMuc(danhMucId));
  }

  // POST: Thêm liên kết
  @PostMapping
  public ResponseEntity<DichVuDanhMucResponseDTO> them(
      @Valid @RequestBody DichVuDanhMucRequestDTO request) {
    DichVuDanhMucResponseDTO result = dichVuDanhMucService.them(request);

    return ResponseEntity
        .status(HttpStatus.CREATED)
        .body(result);
  }

  // PUT: Cập nhật liên kết
  @PutMapping("/{id}")
  public ResponseEntity<DichVuDanhMucResponseDTO> capNhat(
      @PathVariable Long id,
      @Valid @RequestBody DichVuDanhMucRequestDTO request) {
    return ResponseEntity.ok(
        dichVuDanhMucService.capNhat(id, request));
  }

  // DELETE: Xóa liên kết
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> xoa(
      @PathVariable Long id) {
    dichVuDanhMucService.xoa(id);
    return ResponseEntity.noContent().build();
  }

}
