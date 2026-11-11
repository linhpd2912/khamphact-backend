package anuong.cantho.backend.service.quanan;

import java.util.List;

import anuong.cantho.backend.dto.dichvu.dichvugmc.DichVuGioMoCuaRequestDTO;
import anuong.cantho.backend.dto.dichvu.dichvugmc.DichVuGioMoCuaResponseDTO;
import anuong.cantho.backend.dto.quanan.giomocua.GioMoCuaRequestDTO;
import anuong.cantho.backend.dto.quanan.giomocua.GioMoCuaResponseDTO;

public interface GioMoCuaService {

    // gio mo cua quan an

    List<GioMoCuaResponseDTO> layTatCa();

    List<GioMoCuaResponseDTO> layTheoQuanAn(Long quanAnId);

    GioMoCuaResponseDTO layTheoId(Long id);

    GioMoCuaResponseDTO them(GioMoCuaRequestDTO request);

    GioMoCuaResponseDTO capNhat(
            Long id,
            GioMoCuaRequestDTO request);

    void xoa(Long id);

    GioMoCuaResponseDTO layTheoIdVaQuanAn(
            Long id,
            Long quanAnId);

    // gio mo cua dich vu

    List<DichVuGioMoCuaResponseDTO> layTatCaDichVu();

    List<DichVuGioMoCuaResponseDTO> layTheoDichVu(Long dichVuId);

    DichVuGioMoCuaResponseDTO layTheoIdDichVu(Long id);

    DichVuGioMoCuaResponseDTO themDichVu(DichVuGioMoCuaRequestDTO request);

    DichVuGioMoCuaResponseDTO capNhatDichVu(
            Long id,
            DichVuGioMoCuaRequestDTO request);

    void xoaDichVu(Long id);

    DichVuGioMoCuaResponseDTO layTheoIdVaDichVu(
            Long id,
            Long dichVuId);

}
