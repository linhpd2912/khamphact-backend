package anuong.cantho.backend.service.dichvu.dichvu;

import org.springframework.data.domain.*;

import anuong.cantho.backend.dto.dichvu.dichvu.CapNhatTrangThaiDichVuDTO;
import anuong.cantho.backend.dto.dichvu.dichvu.DichVuRequestDTO;
import anuong.cantho.backend.dto.dichvu.dichvu.DichVuResponseDTO;

public interface DichVuService {

  Page<DichVuResponseDTO> getAll(
      String keyword,
      Long tinhThanhId,
      Long phuongXaId,
      Boolean hoatDong,
      Boolean noiBat,
      Pageable pageable);

  Page<DichVuResponseDTO> getDichVuHoatDong(
      Pageable pageable);

  DichVuResponseDTO getById(Long id);

  DichVuResponseDTO getBySlug(String slug);

  DichVuResponseDTO create(DichVuRequestDTO request);

  DichVuResponseDTO update(
      Long id,
      DichVuRequestDTO request);

  DichVuResponseDTO capNhatTrangThai(
      Long id,
      CapNhatTrangThaiDichVuDTO request);

  DichVuResponseDTO capNhatNoiBat(
      Long id,
      CapNhatTrangThaiDichVuDTO request);

  void xoaMem(Long id);

  DichVuResponseDTO khoiPhuc(Long id);

}
