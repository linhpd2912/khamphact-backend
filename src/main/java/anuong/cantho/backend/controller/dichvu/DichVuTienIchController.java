package anuong.cantho.backend.controller.dichvu;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import anuong.cantho.backend.dto.dichvu.dichvutienich.DichVuTienIchRequestDTO;
import anuong.cantho.backend.dto.dichvu.dichvutienich.DichVuTienIchResponseDTO;
import anuong.cantho.backend.service.dichvu.DichVuTienIchService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController 
@RequestMapping ("/api/dich-vu-tien-ich")
@RequiredArgsConstructor 
@Validated 
public class DichVuTienIchController {

  private final DichVuTienIchService dichVuTienIchService;

  // GET /api/dich-vu-tien-ich/dich-vu/1
  @GetMapping("/dich-vu/{dichVuId}")
  public ResponseEntity<List<DichVuTienIchResponseDTO>> layTheoDichVu(
      @PathVariable Long dichVuId) {
    return ResponseEntity.ok(
        dichVuTienIchService.layTheoDichVu(dichVuId));
  }

  // POST /api/dich-vu-tien-ich
  @PostMapping
  public ResponseEntity<DichVuTienIchResponseDTO> them(
      @Valid @RequestBody DichVuTienIchRequestDTO dto) {
    return ResponseEntity
        .status(HttpStatus.CREATED)
        .body(dichVuTienIchService.them(dto));
  }

  // DELETE /api/dich-vu-tien-ich/dich-vu/1/tien-ich/3
  @DeleteMapping("/dich-vu/{dichVuId}/tien-ich/{tienIchId}")
  public ResponseEntity<Void> xoa(
      @PathVariable Long dichVuId,
      @PathVariable Long tienIchId) {
    dichVuTienIchService.xoa(dichVuId, tienIchId);
    return ResponseEntity.noContent().build();
  }

}
