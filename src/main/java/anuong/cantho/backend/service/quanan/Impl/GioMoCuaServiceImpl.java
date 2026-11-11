package anuong.cantho.backend.service.quanan.Impl;

import java.util.List;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import anuong.cantho.backend.dto.dichvu.dichvugmc.DichVuGioMoCuaRequestDTO;
import anuong.cantho.backend.dto.dichvu.dichvugmc.DichVuGioMoCuaResponseDTO;
import anuong.cantho.backend.dto.quanan.giomocua.GioMoCuaRequestDTO;
import anuong.cantho.backend.dto.quanan.giomocua.GioMoCuaResponseDTO;
import anuong.cantho.backend.entity.dichvu.DichVu;
import anuong.cantho.backend.entity.quanan.GioMoCua;
import anuong.cantho.backend.entity.quanan.QuanAn;
import anuong.cantho.backend.repository.dichvu.DichVuRepository;
import anuong.cantho.backend.repository.quanan.GioMoCuaRepository;
import anuong.cantho.backend.repository.quanan.QuanAnRepository;
import anuong.cantho.backend.service.quanan.GioMoCuaService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GioMoCuaServiceImpl implements GioMoCuaService {
  private final GioMoCuaRepository gioMoCuaRepository;
  private final QuanAnRepository quanAnRepository;
  private final DichVuRepository dichVuRepository;

  // =====================================================
  // 1. LẤY TẤT CẢ GIỜ MỞ CỬA
  // =====================================================

  @Override
  public List<GioMoCuaResponseDTO> layTatCa() {

    return gioMoCuaRepository.timTatCa()
        .stream()
        .map(this::toResponseDTO)
        .toList();
  }

  @Override
  public List<DichVuGioMoCuaResponseDTO> layTatCaDichVu() {
    return gioMoCuaRepository.timTatCaDichVu()
        .stream()
        .map(this::toResponseDVDTO)
        .toList();
  }

  // =====================================================
  // 2. LẤY GIỜ MỞ CỬA THEO QUÁN / dv
  // =====================================================

  @Override
  public List<GioMoCuaResponseDTO> layTheoQuanAn(Long quanAnId) {

    if (quanAnId == null || quanAnId <= 0) {
      throw new ResponseStatusException(
          HttpStatus.BAD_REQUEST,
          "ID quán ăn không hợp lệ");
    }

    // Kiểm tra quán có tồn tại không.
    if (!quanAnRepository.existsById(quanAnId)) {
      throw new ResponseStatusException(
          HttpStatus.NOT_FOUND,
          "Không tìm thấy quán ăn");
    }

    return gioMoCuaRepository.timTheoQuanAn(quanAnId)
        .stream()
        .map(this::toResponseDTO)
        .toList();
  }

  @Override
  public List<DichVuGioMoCuaResponseDTO> layTheoDichVu(Long dichVuId) {

    if (dichVuId == null || dichVuId <= 0) {
      throw new ResponseStatusException(
          HttpStatus.BAD_REQUEST,
          "ID dịch vụ không hợp lệ");
    }

    // Kiểm tra dịch vụ có tồn tại không.
    if (!dichVuRepository.existsById(dichVuId)) {
      throw new ResponseStatusException(
          HttpStatus.NOT_FOUND,
          "Không tìm thấy dịch vụ");
    }

    return gioMoCuaRepository.timTheoDichVu(dichVuId)
        .stream()
        .map(this::toResponseDVDTO)
        .toList();
  }

  // =====================================================
  // 3. LẤY MỘT BẢN GHI THEO ID
  // =====================================================

  @Override
  public GioMoCuaResponseDTO layTheoId(Long id) {

    GioMoCua gioMoCua = timGioMoCua(id);

    return toResponseDTO(gioMoCua);
  }

  @Override
  public DichVuGioMoCuaResponseDTO layTheoIdDichVu(Long id) {

    GioMoCua gioMoCua = timGioMoCua(id);

    return toResponseDVDTO(gioMoCua);
  }

  // =====================================================
  // 4. LẤY BẢN GHI THEO ID VÀ ID QUÁN / dv
  // =====================================================

  @Override
  public GioMoCuaResponseDTO layTheoIdVaQuanAn(
      Long id,
      Long quanAnId) {

    if (id == null || id <= 0 || quanAnId == null || quanAnId <= 0) {
      throw new ResponseStatusException(
          HttpStatus.BAD_REQUEST,
          "ID giờ mở cửa hoặc ID quán ăn không hợp lệ");
    }

    GioMoCua gioMoCua = gioMoCuaRepository
        .findByIdAndQuanAn_Id(id, quanAnId)
        .orElseThrow(() -> new ResponseStatusException(
            HttpStatus.NOT_FOUND,
            "Không tìm thấy giờ mở cửa thuộc quán ăn này"));

    return toResponseDTO(gioMoCua);
  }

  @Override
  public DichVuGioMoCuaResponseDTO layTheoIdVaDichVu(
      Long id,
      Long dichVuId) {

    if (id == null || id <= 0 || dichVuId == null || dichVuId <= 0) {
      throw new ResponseStatusException(
          HttpStatus.BAD_REQUEST,
          "ID giờ mở cửa hoặc ID dịch vụ không hợp lệ");
    }

    GioMoCua gioMoCua = gioMoCuaRepository
        .findByIdAndDichVu_Id(id, dichVuId)
        .orElseThrow(() -> new ResponseStatusException(
            HttpStatus.NOT_FOUND,
            "Không tìm thấy giờ mở cửa thuộc dịch vụ này"));

    return toResponseDVDTO(gioMoCua);
  }

  // =====================================================
  // 5. THÊM GIỜ MỞ CỬA cho quan/ dv
  // =====================================================

  @Override
  @Transactional
  public GioMoCuaResponseDTO them(GioMoCuaRequestDTO request) {

    // Kiểm tra dữ liệu đầu vào.
    kiemTraDuLieu(request);

    // Tìm quán ăn.
    QuanAn quanAn = timQuanAn(request.getQuanAnId());

    // Không cho thêm giờ mở cửa cho quán đã ngừng hoạt động.
    kiemTraQuanDangHoatDong(quanAn);

    // Không cho một quán có hai bản ghi cùng thứ.
    if (gioMoCuaRepository.existsByQuanAn_IdAndThu(
        request.getQuanAnId(),
        request.getThu())) {
      throw new ResponseStatusException(
          HttpStatus.CONFLICT,
          "Quán ăn đã có giờ mở cửa cho thứ này");
    }

    GioMoCua gioMoCua = new GioMoCua();

    ganDuLieu(gioMoCua, request, quanAn);

    return luuVaTraVe(gioMoCua);
  }

  @Override
  @Transactional
  public DichVuGioMoCuaResponseDTO themDichVu(DichVuGioMoCuaRequestDTO request) {

    // Kiểm tra dữ liệu đầu vào.
    kiemTraDuLieuDichVu(request);

    // Tìm dịch vụ.
    DichVu dichVu = timDichVu(request.getDichVuId());

    // Không cho thêm giờ mở cửa cho dịch vụ đã ngừng hoạt động.
    kiemTraDichVuDangHoatDong(dichVu);

    // Không cho một dịch vụ có hai bản ghi cùng thứ.
    if (gioMoCuaRepository.existsByDichVu_IdAndThu(
        request.getDichVuId(),
        request.getThu())) {
      throw new ResponseStatusException(
          HttpStatus.CONFLICT,
          "Dịch vụ đã có giờ mở cửa cho thứ này");
    }

    GioMoCua gioMoCua = new GioMoCua();

    ganDuLieuDichVu(gioMoCua, request, dichVu);

    return luuVaTraVeDV(gioMoCua);
  }

  // =====================================================
  // 6. CẬP NHẬT GIỜ MỞ CỬA quan/dv
  // =====================================================

  @Override
  @Transactional
  public GioMoCuaResponseDTO capNhat(
      Long id,
      GioMoCuaRequestDTO request) {

    if (id == null || id <= 0) {
      throw new ResponseStatusException(
          HttpStatus.BAD_REQUEST,
          "ID giờ mở cửa không hợp lệ");
    }

    kiemTraDuLieu(request);

    GioMoCua gioMoCua = timGioMoCua(id);

    QuanAn quanAn = timQuanAn(request.getQuanAnId());

    kiemTraQuanDangHoatDong(quanAn);

    // Kiểm tra trùng thứ, không tính bản ghi hiện tại.
    boolean biTrung = gioMoCuaRepository
        .existsByQuanAn_IdAndThuAndIdNot(
            request.getQuanAnId(),
            request.getThu(),
            id);

    if (biTrung) {
      throw new ResponseStatusException(
          HttpStatus.CONFLICT,
          "Quán ăn đã có giờ mở cửa cho thứ này");
    }

    ganDuLieu(gioMoCua, request, quanAn);

    return luuVaTraVe(gioMoCua);
  }

  @Override
  @Transactional
  public DichVuGioMoCuaResponseDTO capNhatDichVu(
      Long id,
      DichVuGioMoCuaRequestDTO request) {
    if (id == null || id <= 0) {
      throw new ResponseStatusException(
          HttpStatus.BAD_REQUEST,
          "ID giờ mở cửa không hợp lệ");
    }
    kiemTraDuLieuDichVu(request);
    GioMoCua gioMoCua = timGioMoCua(id);

    DichVu dichVu = timDichVu(request.getDichVuId());
    kiemTraDichVuDangHoatDong(dichVu);
    boolean biTrung = gioMoCuaRepository
        .existsByDichVu_IdAndThuAndIdNot(
            request.getDichVuId(),
            request.getThu(),
            id);
    if (biTrung) {
      throw new ResponseStatusException(
          HttpStatus.CONFLICT,
          "Dịch vụ đã có giờ mở cửa cho thứ này");
    }
    ganDuLieuDichVu(gioMoCua, request, dichVu);
    return luuVaTraVeDV(gioMoCua);
  }

  // =====================================================
  // 7. XÓA GIỜ MỞ CỬA quan /dv
  // =====================================================

  @Override
  @Transactional
  public void xoa(Long id) {

    GioMoCua gioMoCua = timGioMoCua(id);

    // Chỉ xóa bản ghi giờ mở cửa, không xóa quán ăn.
    gioMoCuaRepository.delete(gioMoCua);
  }

  @Override
  @Transactional
  public void xoaDichVu(Long id) {
    GioMoCua gioMoCua = timGioMoCua(id);
    gioMoCuaRepository.delete(gioMoCua);
  }

  // =====================================================
  // CÁC HÀM HỖ TRỢ
  // =====================================================

  private GioMoCua timGioMoCua(Long id) {

    if (id == null || id <= 0) {
      throw new ResponseStatusException(
          HttpStatus.BAD_REQUEST,
          "ID giờ mở cửa không hợp lệ");
    }

    return gioMoCuaRepository.findById(id)
        .orElseThrow(() -> new ResponseStatusException(
            HttpStatus.NOT_FOUND,
            "Không tìm thấy giờ mở cửa"));
  }

  private QuanAn timQuanAn(Long quanAnId) {

    if (quanAnId == null || quanAnId <= 0) {
      throw new ResponseStatusException(
          HttpStatus.BAD_REQUEST,
          "ID quán ăn không hợp lệ");
    }

    return quanAnRepository.findById(quanAnId)
        .orElseThrow(() -> new ResponseStatusException(
            HttpStatus.NOT_FOUND,
            "Không tìm thấy quán ăn"));
  }

  private DichVu timDichVu(Long dichVuId) {

    if (dichVuId == null || dichVuId <= 0) {
      throw new ResponseStatusException(
          HttpStatus.BAD_REQUEST,
          "ID dịch vụ không hợp lệ");
    }

    return dichVuRepository.findById(dichVuId)
        .orElseThrow(() -> new ResponseStatusException(
            HttpStatus.NOT_FOUND,
            "Không tìm thấy dịch vụ"));
  }

  private void kiemTraQuanDangHoatDong(QuanAn quanAn) {

    if (!Boolean.TRUE.equals(quanAn.getHoatDong())) {
      throw new ResponseStatusException(
          HttpStatus.CONFLICT,
          "Quán ăn đã ngừng hoạt động, không thể thêm hoặc sửa giờ mở cửa");
    }
  }

  private void kiemTraDichVuDangHoatDong(DichVu dichVu) {

    if (!Boolean.TRUE.equals(dichVu.getHoatDong())) {
      throw new ResponseStatusException(
          HttpStatus.CONFLICT,
          "Dịch vụ đã ngừng hoạt động, không thể thêm hoặc sửa giờ mở cửa");
    }
  }

  /**
   * Kiểm tra các quy tắc nghiệp vụ của giờ mở cửa.
   */
  private void kiemTraDuLieu(GioMoCuaRequestDTO request) {

    if (request == null) {
      throw new ResponseStatusException(
          HttpStatus.BAD_REQUEST,
          "Dữ liệu giờ mở cửa không được để trống");
    }

    if (request.getQuanAnId() == null || request.getQuanAnId() <= 0) {
      throw new ResponseStatusException(
          HttpStatus.BAD_REQUEST,
          "Vui lòng chọn quán ăn hợp lệ");
    }

    Integer thu = request.getThu();

    if (thu == null || thu < 1 || thu > 7) {
      throw new ResponseStatusException(
          HttpStatus.BAD_REQUEST,
          "Thứ phải nằm trong khoảng từ 1 đến 7");
    }

    if (request.getNghi() == null) {
      throw new ResponseStatusException(
          HttpStatus.BAD_REQUEST,
          "Vui lòng xác định quán có nghỉ hay không");
    }

    String ghiChu = request.getGhiChu();

    if (ghiChu != null && ghiChu.length() > 500) {
      throw new ResponseStatusException(
          HttpStatus.BAD_REQUEST,
          "Ghi chú không được vượt quá 500 ký tự");
    }

    // Trường hợp quán nghỉ cả ngày.
    if (Boolean.TRUE.equals(request.getNghi())) {
      return;
    }

    // Nếu quán hoạt động, bắt buộc phải có cả giờ mở và giờ đóng.
    if (request.getGioMo() == null || request.getGioDong() == null) {
      throw new ResponseStatusException(
          HttpStatus.BAD_REQUEST,
          "Khi quán hoạt động, vui lòng nhập cả giờ mở và giờ đóng");
    }

    // Không chấp nhận hai thời điểm giống nhau.
    if (request.getGioMo().equals(request.getGioDong())) {
      throw new ResponseStatusException(
          HttpStatus.BAD_REQUEST,
          "Giờ mở cửa không được trùng giờ đóng cửa");
    }

    /*
     * Không cấm gioDong nhỏ hơn gioMo.
     * Ví dụ 18:00 - 02:00 là một khung giờ qua đêm hợp lệ.
     */
  }

  private void kiemTraDuLieuDichVu(DichVuGioMoCuaRequestDTO request) {

    if (request == null) {
      throw new ResponseStatusException(
          HttpStatus.BAD_REQUEST,
          "Dữ liệu giờ mở cửa không được để trống");
    }

    if (request.getDichVuId() == null || request.getDichVuId() <= 0) {
      throw new ResponseStatusException(
          HttpStatus.BAD_REQUEST,
          "Vui lòng chọn dịch vụ hợp lệ");
    }

    Integer thu = request.getThu();

    if (thu == null || thu < 1 || thu > 7) {
      throw new ResponseStatusException(
          HttpStatus.BAD_REQUEST,
          "Thứ phải nằm trong khoảng từ 1 đến 7");
    }

    if (request.getNghi() == null) {
      throw new ResponseStatusException(
          HttpStatus.BAD_REQUEST,
          "Vui lòng xác định dịch vụ có nghỉ hay không");
    }

    String ghiChu = request.getGhiChu();

    if (ghiChu != null && ghiChu.length() > 500) {
      throw new ResponseStatusException(
          HttpStatus.BAD_REQUEST,
          "Ghi chú không được vượt quá 500 ký tự");
    }

    // Trường hợp dịch vụ nghỉ cả ngày.
    if (Boolean.TRUE.equals(request.getNghi())) {
      return;
    }

    // Nếu dịch vụ hoạt động, bắt buộc phải có cả giờ mở và giờ đóng.
    if (request.getGioMo() == null || request.getGioDong() == null) {
      throw new ResponseStatusException(
          HttpStatus.BAD_REQUEST,
          "Khi dịch vụ hoạt động, vui lòng nhập cả giờ mở và giờ đóng");
    }

    // Không chấp nhận hai thời điểm giống nhau.
    if (request.getGioMo().equals(request.getGioDong())) {
      throw new ResponseStatusException(
          HttpStatus.BAD_REQUEST,
          "Giờ mở cửa không được trùng giờ đóng cửa");
    }
  }

  /**
   * Gán dữ liệu từ DTO vào entity.
   */
  private void ganDuLieu(
      GioMoCua gioMoCua,
      GioMoCuaRequestDTO request,
      QuanAn quanAn) {

    gioMoCua.setQuanAn(quanAn);
    // gioMoCua.setDichVu(null);
    gioMoCua.setThu(request.getThu());
    gioMoCua.setNghi(request.getNghi());

    if (Boolean.TRUE.equals(request.getNghi())) {
      // Quán nghỉ thì xóa giờ cũ để tránh dữ liệu gây nhầm lẫn.
      gioMoCua.setGioMo(null);
      gioMoCua.setGioDong(null);
    } else {
      gioMoCua.setGioMo(request.getGioMo());
      gioMoCua.setGioDong(request.getGioDong());
    }

    // Chuẩn hóa ghi chú: chuỗi rỗng được lưu thành null.
    String ghiChu = request.getGhiChu();

    if (ghiChu != null) {
      ghiChu = ghiChu.trim();
    }

    gioMoCua.setGhiChu(
        ghiChu == null || ghiChu.isEmpty()
            ? null
            : ghiChu);
  }

  private void ganDuLieuDichVu(
      GioMoCua gioMoCua,
      DichVuGioMoCuaRequestDTO request,
      DichVu dichVu) {

    // gioMoCua.setQuanAn(null);
    gioMoCua.setDichVu(dichVu);
    gioMoCua.setThu(request.getThu());
    gioMoCua.setNghi(request.getNghi());

    if (Boolean.TRUE.equals(request.getNghi())) {
      // Dịch vụ nghỉ thì xóa giờ cũ để tránh dữ liệu gây nhầm lẫn.
      gioMoCua.setGioMo(null);
      gioMoCua.setGioDong(null);
    } else {
      gioMoCua.setGioMo(request.getGioMo());
      gioMoCua.setGioDong(request.getGioDong());
    }

    // Chuẩn hóa ghi chú: chuỗi rỗng được lưu thành null.
    String ghiChu = request.getGhiChu();

    if (ghiChu != null) {
      ghiChu = ghiChu.trim();
    }

    gioMoCua.setGhiChu(
        ghiChu == null || ghiChu.isEmpty()
            ? null
            : ghiChu);
  }

  /**
   * Lưu dữ liệu và chuyển lỗi ràng buộc duy nhất thành HTTP 409.
   */
  private GioMoCuaResponseDTO luuVaTraVe(GioMoCua gioMoCua) {

    try {
      GioMoCua daLuu = gioMoCuaRepository.saveAndFlush(gioMoCua);

      return toResponseDTO(daLuu);

    } catch (DataIntegrityViolationException e) {
      throw new ResponseStatusException(
          HttpStatus.CONFLICT,
          "Không thể lưu giờ mở cửa. Có thể quán đã có giờ cho thứ này hoặc dữ liệu vi phạm ràng buộc cơ sở dữ liệu");
    }
  }

  private DichVuGioMoCuaResponseDTO luuVaTraVeDV(GioMoCua gioMoCua) {

    try {
      GioMoCua daLuu = gioMoCuaRepository.saveAndFlush(gioMoCua);

      return toResponseDVDTO(daLuu);

    } catch (DataIntegrityViolationException e) {

      e.printStackTrace();

      throw new ResponseStatusException(
          HttpStatus.CONFLICT,
          "Không thể lưu giờ mở cửa dịch vụ. "
              + "Vui lòng kiểm tra ràng buộc cơ sở dữ liệu",
          e);
    }
  }

  /**
   * Chuyển entity thành DTO.
   */
  private GioMoCuaResponseDTO toResponseDTO(GioMoCua gioMoCua) {

    QuanAn quanAn = gioMoCua.getQuanAn();

    return new GioMoCuaResponseDTO(
        gioMoCua.getId(),
        quanAn.getId(),
        quanAn.getTenQuan(),
        gioMoCua.getThu(),
        gioMoCua.getGioMo(),
        gioMoCua.getGioDong(),
        gioMoCua.getNghi(),
        gioMoCua.getGhiChu());
  }

  private DichVuGioMoCuaResponseDTO toResponseDVDTO(GioMoCua gioMoCua) {
    DichVu dichVu = gioMoCua.getDichVu();
    return new DichVuGioMoCuaResponseDTO(
        gioMoCua.getId(),
        dichVu.getId(),
        dichVu.getTenDichVu(),
        gioMoCua.getThu(),
        gioMoCua.getGioMo(),
        gioMoCua.getGioDong(),
        gioMoCua.getNghi(),
        gioMoCua.getGhiChu());
  }
}
