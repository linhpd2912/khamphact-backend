package anuong.cantho.backend.service.dichvu;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import anuong.cantho.backend.dto.dichvu.dichvutienich.DichVuTienIchRequestDTO;
import anuong.cantho.backend.dto.dichvu.dichvutienich.DichVuTienIchResponseDTO;
import anuong.cantho.backend.entity.dichvu.DichVu;
import anuong.cantho.backend.entity.quanan.TienIch;
import anuong.cantho.backend.entity.dichvu.DichVuTienIch;
import anuong.cantho.backend.repository.dichvu.DichVuRepository;
import anuong.cantho.backend.repository.dichvu.DichVuTienIchRepository;
import anuong.cantho.backend.repository.quanan.TienIchRepository;
import lombok.RequiredArgsConstructor;

@Service 
@RequiredArgsConstructor 
@Transactional 
public class DichVuTienIchService {

  private final DichVuTienIchRepository dichVuTienIchRepository;
  private final DichVuRepository dichVuRepository;
  private final TienIchRepository tienIchRepository;


  // Lấy tiện ích của một quán
  @Transactional(readOnly = true)
  public List<DichVuTienIchResponseDTO> layTheoDichVu(Long dichVuId) {

    if (!dichVuRepository.existsById(dichVuId)) {
      throw new ResponseStatusException(
          HttpStatus.NOT_FOUND,
          "Không tìm thấy dịch vụ có ID: " + dichVuId);
    }

    return dichVuTienIchRepository
        .findByDichVu_IdOrderByTienIch_TenTienIchAsc(dichVuId)
        .stream()
        .map(this::toResponseDTO)
        .toList();
  }

  // Gán một tiện ích cho một dịch vụ
  public DichVuTienIchResponseDTO them(
      DichVuTienIchRequestDTO dto) {
    Long dichVuId = dto.getDichVuId();
    Long tienIchId = dto.getTienIchId();

    DichVu dichVu = dichVuRepository.findById(dichVuId)
        .orElseThrow(() -> new ResponseStatusException(
            HttpStatus.NOT_FOUND,
            "Không tìm thấy dịch vụ có ID: " + dichVuId));

    TienIch tienIch = tienIchRepository.findById(tienIchId)
        .orElseThrow(() -> new ResponseStatusException(
            HttpStatus.NOT_FOUND,
            "Không tìm thấy tiện ích có ID: " + tienIchId));

    if (!Boolean.TRUE.equals(tienIch.getHoatDong())) {
      throw new ResponseStatusException(
          HttpStatus.BAD_REQUEST,
          "Không thể gán tiện ích đã ngừng hoạt động");
    }

    if (dichVuTienIchRepository.existsByDichVu_IdAndTienIch_Id(
        dichVuId, tienIchId)) {
      throw new ResponseStatusException(
          HttpStatus.CONFLICT,
          "Dịch vụ đã được gán tiện ích này");
    }

    DichVuTienIch dichVuTienIch = new DichVuTienIch();
    dichVuTienIch.setDichVu(dichVu);
    dichVuTienIch.setTienIch(tienIch);

    return toResponseDTO(
        dichVuTienIchRepository.save(dichVuTienIch));
  }

  // Bỏ một tiện ích khỏi dịch vụ
  public void xoa(Long dichVuId, Long tienIchId) {

    DichVuTienIch dichVuTienIch = dichVuTienIchRepository
        .findByDichVu_IdAndTienIch_Id(dichVuId, tienIchId)
        .orElseThrow(() -> new ResponseStatusException(
            HttpStatus.NOT_FOUND,
            "Dịch vụ chưa được gán tiện ích này"));

    dichVuTienIchRepository.delete(dichVuTienIch);
  }

  // Chuyển entity sang DTO
  private DichVuTienIchResponseDTO toResponseDTO(
      DichVuTienIch dichVuTienIch) {
    DichVuTienIchResponseDTO dto = new DichVuTienIchResponseDTO();

    TienIch tienIch = dichVuTienIch.getTienIch();

    dto.setId(dichVuTienIch.getId());
    dto.setDichVuId(dichVuTienIch.getDichVu().getId());
    dto.setTienIchId(tienIch.getId());
    dto.setTenTienIch(tienIch.getTenTienIch());
    dto.setIcon(tienIch.getIcon());
    dto.setHoatDong(tienIch.getHoatDong());

    return dto;
  }

}
