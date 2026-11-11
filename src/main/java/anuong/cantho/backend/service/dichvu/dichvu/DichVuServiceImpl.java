package anuong.cantho.backend.service.dichvu.dichvu;

import java.text.Normalizer;
import java.util.Locale;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import anuong.cantho.backend.dto.dichvu.dichvu.CapNhatTrangThaiDichVuDTO;
import anuong.cantho.backend.dto.dichvu.dichvu.DichVuRequestDTO;
import anuong.cantho.backend.dto.dichvu.dichvu.DichVuResponseDTO;
import anuong.cantho.backend.entity.diachi.PhuongXa;
import anuong.cantho.backend.entity.diachi.TinhThanh;
import anuong.cantho.backend.entity.dichvu.DichVu;
import anuong.cantho.backend.repository.diachi.PhuongXaRepository;
import anuong.cantho.backend.repository.diachi.TinhThanhRepository;
import anuong.cantho.backend.repository.dichvu.DichVuDanhMucRepository;
import anuong.cantho.backend.repository.dichvu.DichVuRepository;
import anuong.cantho.backend.repository.dichvu.DichVuTienIchRepository;
import anuong.cantho.backend.repository.quanan.GioMoCuaRepository;
import anuong.cantho.backend.repository.quanan.HinhAnhQuanRepository;
import lombok.RequiredArgsConstructor;

@Service 
@Transactional (readOnly = true)
@RequiredArgsConstructor 
public class DichVuServiceImpl implements DichVuService {

  private final DichVuRepository dichVuRepository;
  private final TinhThanhRepository tinhThanhRepository;
  private final PhuongXaRepository phuongXaRepository;
  private final GioMoCuaRepository gioMoCuaRepository;
  private final HinhAnhQuanRepository hinhAnhQuanRepository;
  private final DichVuDanhMucRepository dichVuDanhMucRepository;
  private final DichVuTienIchRepository dichVuTienIchRepository;


  // =====================================================
  // 1. LẤY DANH SÁCH, TÌM KIẾM VÀ LỌC
  // =====================================================

  @Override
  public Page<DichVuResponseDTO> getAll(
      String keyword,
      Long tinhThanhId,
      Long phuongXaId,
      Boolean hoatDong,
      Boolean noiBat,
      Pageable pageable) {
    kiemTraIdLoc(tinhThanhId, "Tỉnh/thành");
    kiemTraIdLoc(phuongXaId, "Phường/xã");

    if (tinhThanhId != null
        && !tinhThanhRepository.existsById(tinhThanhId)) {
      throw new ResponseStatusException(
          HttpStatus.NOT_FOUND,
          "Không tìm thấy tỉnh/thành có ID " + tinhThanhId);
    }

    if (phuongXaId != null) {
      PhuongXa phuongXa = phuongXaRepository.findById(phuongXaId)
          .orElseThrow(() -> new ResponseStatusException(
              HttpStatus.NOT_FOUND,
              "Không tìm thấy phường/xã có ID " + phuongXaId));

      if (tinhThanhId != null
          && !tinhThanhId.equals(
              phuongXa.getTinhThanh().getId())) {
        throw new ResponseStatusException(
            HttpStatus.BAD_REQUEST,
            "Phường/xã không thuộc tỉnh/thành đã chọn");
      }
    }

    String tuKhoa = chuanHoa(keyword);

    return dichVuRepository.timKiemVaLoc(
        tuKhoa,
        tinhThanhId,
        phuongXaId,
        hoatDong,
        noiBat,
        pageable)
        .map(this::toResponseDTO);
  }

  @Override
  public Page<DichVuResponseDTO> getDichVuHoatDong(
      Pageable pageable) {
    return dichVuRepository
        .findByHoatDongTrue(pageable)
        .map(this::toResponseDTO);
  }

  // =====================================================
  // 2. LẤY CHI TIẾT
  // =====================================================
  private void ganThongKeLienQuan(
      DichVuResponseDTO dto,
      Long dichVuId) {
    dto.setSoGioMoCua(
        gioMoCuaRepository.countByDichVu_Id(dichVuId));

    dto.setSoHinhAnh(
        hinhAnhQuanRepository.countByDichVu_Id(dichVuId));

    dto.setSoDanhMuc(
        dichVuDanhMucRepository.countByDichVu_Id(dichVuId));

    dto.setSoTienIch(
        dichVuTienIchRepository.countByDichVu_Id(dichVuId));
  }

  @Override
  public DichVuResponseDTO getById(Long id) {
    kiemTraId(id);

    DichVu dichVu = timDichVu(id);

    DichVuResponseDTO dto = toResponseDTO(dichVu);

    ganThongKeLienQuan(dto, dichVu.getId());

    return dto;
  }

  @Override
  public DichVuResponseDTO getBySlug(String slug) {
    if (slug == null || slug.isBlank()) {
      throw new ResponseStatusException(
          HttpStatus.BAD_REQUEST,
          "Đường dẫn SEO không được để trống");
    }

    DichVu dichVu = dichVuRepository
        .findByDuongDanSeo(slug.trim())
        .orElseThrow(() -> new ResponseStatusException(
            HttpStatus.NOT_FOUND,
            "Không tìm thấy dịch vụ"));

    DichVuResponseDTO dto = toResponseDTO(dichVu);

    ganThongKeLienQuan(dto, dichVu.getId());

    return dto;
  }

  // =====================================================
  // 3. THÊM DỊCH VỤ
  // =====================================================

  @Override
  @Transactional
  public DichVuResponseDTO create(DichVuRequestDTO request) {
    if (request == null) {
      throw new ResponseStatusException(
          HttpStatus.BAD_REQUEST,
          "Dữ liệu dịch vụ không được để trống");
    }

    kiemTraDuLieu(request);

    DichVu dichVu = new DichVu();

    ganDuLieu(dichVu, request);

    try {
      DichVu daLuu = dichVuRepository.saveAndFlush(dichVu);

      return toResponseDTO(daLuu);

    } catch (DataIntegrityViolationException ex) {
      throw new ResponseStatusException(
          HttpStatus.CONFLICT,
          "Không thể thêm dịch vụ. Đường dẫn SEO có thể đã tồn tại "
              + "hoặc dữ liệu vi phạm ràng buộc database.",
          ex);
    }
  }

  // =====================================================
  // 4. CẬP NHẬT DỊCH VỤ
  // =====================================================

  @Override
  @Transactional
  public DichVuResponseDTO update(
      Long id,
      DichVuRequestDTO request) {
    kiemTraId(id);

    if (request == null) {
      throw new ResponseStatusException(
          HttpStatus.BAD_REQUEST,
          "Dữ liệu cập nhật không được để trống");
    }

    kiemTraDuLieu(request);

    DichVu dichVu = timDichVu(id);

    ganDuLieu(dichVu, request);

    try {
      DichVu daLuu = dichVuRepository.saveAndFlush(dichVu);

      return toResponseDTO(daLuu);

    } catch (DataIntegrityViolationException ex) {
      throw new ResponseStatusException(
          HttpStatus.CONFLICT,
          "Không thể cập nhật dịch vụ. Đường dẫn SEO có thể đã "
              + "được sử dụng hoặc dữ liệu vi phạm ràng buộc.",
          ex);
    }
  }

  // =====================================================
  // 5. BẬT / TẮT HOẠT ĐỘNG
  // =====================================================

  @Override
  @Transactional
  public DichVuResponseDTO capNhatTrangThai(
      Long id,
      CapNhatTrangThaiDichVuDTO request) {
    kiemTraId(id);
    kiemTraGiaTri(request);

    DichVu dichVu = timDichVu(id);

    boolean dangHoatDong = request.getGiaTri();

    dichVu.setHoatDong(dangHoatDong);

    // Dịch vụ bị tắt hoạt động thì không còn được đánh dấu nổi bật.
    if (!dangHoatDong) {
      dichVu.setNoiBat(false);
    }

    DichVu daLuu = dichVuRepository.saveAndFlush(dichVu);

    return toResponseDTO(daLuu);
  }

  // =====================================================
  // 6. BẬT / TẮT NỔI BẬT
  // =====================================================

  @Override
  @Transactional
  public DichVuResponseDTO capNhatNoiBat(
      Long id,
      CapNhatTrangThaiDichVuDTO request) {
    kiemTraId(id);
    kiemTraGiaTri(request);

    DichVu dichVu = timDichVu(id);

    if (!Boolean.TRUE.equals(dichVu.getHoatDong())
        && Boolean.TRUE.equals(request.getGiaTri())) {
      throw new ResponseStatusException(
          HttpStatus.BAD_REQUEST,
          "Không thể đánh dấu nổi bật cho dịch vụ đã tắt hoạt động");
    }

    dichVu.setNoiBat(request.getGiaTri());

    DichVu daLuu = dichVuRepository.saveAndFlush(dichVu);

    return toResponseDTO(daLuu);
  }

  // =====================================================
  // 7. XÓA MỀM QUÁN ĂN
  // =====================================================

  @Override
  @Transactional
  public void xoaMem(Long id) {
    kiemTraId(id);

    DichVu dichVu = timDichVu(id);

    // Không xóa bản ghi khỏi database.
    // Giữ nguyên các quan hệ với 4 bảng liên quan.
    dichVu.setHoatDong(false);
    dichVu.setNoiBat(false);

    dichVuRepository.saveAndFlush(dichVu);
  }

  // =====================================================
  // 8. KHÔI PHỤC QUÁN ĂN
  // =====================================================

  @Override
  @Transactional
  public DichVuResponseDTO khoiPhuc(Long id) {
    kiemTraId(id);

    DichVu dichVu = timDichVu(id);

    // Chỉ bật hoạt động lại; không tự bật nổi bật.
    dichVu.setHoatDong(true);

    DichVu daLuu = dichVuRepository.saveAndFlush(dichVu);

    return toResponseDTO(daLuu);
  }

  // =====================================================
  // 9. KIỂM TRA DỮ LIỆU
  // =====================================================

  private void kiemTraDuLieu(DichVuRequestDTO request) {
    if (request.getTenDichVu() == null
        || request.getTenDichVu().isBlank()) {
      throw new ResponseStatusException(
          HttpStatus.BAD_REQUEST,
          "Tên dịch vụ không được để trống");
    }

    if (request.getTinhThanhId() == null
        || request.getPhuongXaId() == null) {
      throw new ResponseStatusException(
          HttpStatus.BAD_REQUEST,
          "Vui lòng chọn tỉnh/thành và phường/xã");
    }

    if (request.getGiaTu() != null
        && request.getGiaDen() != null
        && request.getGiaTu().compareTo(request.getGiaDen()) > 0) {
      throw new ResponseStatusException(
          HttpStatus.BAD_REQUEST,
          "Giá từ không được lớn hơn giá đến");
    }

    boolean coViDo = request.getViDo() != null;
    boolean coKinhDo = request.getKinhDo() != null;

    if (coViDo != coKinhDo) {
      throw new ResponseStatusException(
          HttpStatus.BAD_REQUEST,
          "Vui lòng nhập cả vĩ độ và kinh độ, hoặc để trống cả hai");
    }

    if (request.getNoiBat() == null
        || request.getHoatDong() == null) {
      throw new ResponseStatusException(
          HttpStatus.BAD_REQUEST,
          "Vui lòng chọn trạng thái hoạt động và nổi bật");
    }
  }

  // =====================================================
  // 10. GÁN DỮ LIỆU VÀO ENTITY
  // =====================================================

  private void ganDuLieu(
      DichVu dichVu,
      DichVuRequestDTO request) {
    TinhThanh tinhThanh = tinhThanhRepository
        .findById(request.getTinhThanhId())
        .orElseThrow(() -> new ResponseStatusException(
            HttpStatus.NOT_FOUND,
            "Không tìm thấy tỉnh/thành có ID "
                + request.getTinhThanhId()));

    PhuongXa phuongXa = phuongXaRepository
        .findById(request.getPhuongXaId())
        .orElseThrow(() -> new ResponseStatusException(
            HttpStatus.NOT_FOUND,
            "Không tìm thấy phường/xã có ID "
                + request.getPhuongXaId()));

    // Quan trọng: phường/xã phải thuộc tỉnh/thành đã chọn.
    if (!tinhThanh.getId().equals(
        phuongXa.getTinhThanh().getId())) {
      throw new ResponseStatusException(
          HttpStatus.BAD_REQUEST,
          "Phường/xã đã chọn không thuộc tỉnh/thành này");
    }

    String slugNhap = chuanHoa(request.getDuongDanSeo());

    String slug = slugNhap == null
        ? taoSlug(request.getTenDichVu())
        : taoSlug(slugNhap);

    if (slug.isBlank()) {
      throw new ResponseStatusException(
          HttpStatus.BAD_REQUEST,
          "Không thể tạo đường dẫn SEO từ tên dịch vụ");
    }

    boolean trungSlug = dichVu.getId() == null
        ? dichVuRepository.existsByDuongDanSeo(slug)
        : dichVuRepository.existsByDuongDanSeoAndIdNot(
            slug,
            dichVu.getId());

    if (trungSlug) {
      throw new ResponseStatusException(
          HttpStatus.CONFLICT,
          "Đường dẫn SEO đã được sử dụng: " + slug);
    }

    dichVu.setTenDichVu(request.getTenDichVu().trim());
    dichVu.setDuongDanSeo(slug);
    dichVu.setDiaChi(chuanHoa(request.getDiaChi()));

    dichVu.setTinhThanh(tinhThanh);
    dichVu.setPhuongXa(phuongXa);

    dichVu.setSoDienThoai(chuanHoa(request.getSoDienThoai()));
    dichVu.setEmail(chuanHoa(request.getEmail()));

    dichVu.setWebsite(chuanHoa(request.getWebsite()));
    dichVu.setUrlFacebook(chuanHoa(request.getUrlFacebook()));
    dichVu.setUrlTiktok(chuanHoa(request.getUrlTiktok()));
    dichVu.setUrlGoogleMap(chuanHoa(request.getUrlGoogleMap()));

    dichVu.setViDo(request.getViDo());
    dichVu.setKinhDo(request.getKinhDo());

    dichVu.setMoTaNgan(chuanHoa(request.getMoTaNgan()));
    dichVu.setMoTaChiTiet(chuanHoa(request.getMoTaChiTiet()));

    dichVu.setGiaTu(request.getGiaTu());
    dichVu.setGiaDen(request.getGiaDen());

    dichVu.setDiemDanhGiaGoogle(request.getDiemDanhGiaGoogle());
    dichVu.setTongDanhGiaGoogle(request.getTongDanhGiaGoogle());

    dichVu.setNoiBat(request.getNoiBat());
    dichVu.setHoatDong(request.getHoatDong());

    dichVu.setTieuDeSeo(chuanHoa(request.getTieuDeSeo()));
    dichVu.setMoTaSeo(chuanHoa(request.getMoTaSeo()));
    dichVu.setTuKhoaSeo(chuanHoa(request.getTuKhoaSeo()));
  }

  // =====================================================
  // 11. CHUYỂN ENTITY THÀNH RESPONSE DTO
  // =====================================================

  private DichVuResponseDTO toResponseDTO(DichVu dichVu) {
    DichVuResponseDTO dto = new DichVuResponseDTO();

    dto.setId(dichVu.getId());

    dto.setTenDichVu(dichVu.getTenDichVu());
    dto.setDuongDanSeo(dichVu.getDuongDanSeo());
    dto.setDiaChi(dichVu.getDiaChi());

    if (dichVu.getTinhThanh() != null) {
      dto.setTinhThanhId(dichVu.getTinhThanh().getId());
      dto.setTenTinhThanh(dichVu.getTinhThanh().getTenTinhThanh());
    }

    if (dichVu.getPhuongXa() != null) {
      dto.setPhuongXaId(dichVu.getPhuongXa().getId());
      dto.setTenPhuongXa(dichVu.getPhuongXa().getTenPhuongXa());
    }

    dto.setSoDienThoai(dichVu.getSoDienThoai());
    dto.setEmail(dichVu.getEmail());

    dto.setWebsite(dichVu.getWebsite());
    dto.setUrlFacebook(dichVu.getUrlFacebook());
    dto.setUrlTiktok(dichVu.getUrlTiktok());
    dto.setUrlGoogleMap(dichVu.getUrlGoogleMap());

    dto.setViDo(dichVu.getViDo());
    dto.setKinhDo(dichVu.getKinhDo());

    dto.setMoTaNgan(dichVu.getMoTaNgan());
    dto.setMoTaChiTiet(dichVu.getMoTaChiTiet());

    dto.setGiaTu(dichVu.getGiaTu());
    dto.setGiaDen(dichVu.getGiaDen());

    dto.setDiemDanhGiaGoogle(dichVu.getDiemDanhGiaGoogle());
    dto.setTongDanhGiaGoogle(dichVu.getTongDanhGiaGoogle());
    dto.setLuotXem(dichVu.getLuotXem());

    dto.setNoiBat(dichVu.getNoiBat());
    dto.setHoatDong(dichVu.getHoatDong());

    dto.setTieuDeSeo(dichVu.getTieuDeSeo());
    dto.setMoTaSeo(dichVu.getMoTaSeo());
    dto.setTuKhoaSeo(dichVu.getTuKhoaSeo());

    dto.setNgayTao(dichVu.getNgayTao());
    dto.setNgayCapNhat(dichVu.getNgayCapNhat());

    return dto;
  }

  // =====================================================
  // 12. HÀM HỖ TRỢ
  // =====================================================

  private DichVu timDichVu(Long id) {
    return dichVuRepository.findById(id)
        .orElseThrow(() -> new ResponseStatusException(
            HttpStatus.NOT_FOUND,
            "Không tìm thấy dịch vụ có ID " + id));
  }

  private void kiemTraId(Long id) {
    if (id == null || id <= 0) {
      throw new ResponseStatusException(
          HttpStatus.BAD_REQUEST,
          "ID dịch vụ không hợp lệ");
    }
  }

  private void kiemTraIdLoc(Long id, String tenTruong) {
    if (id != null && id <= 0) {
      throw new ResponseStatusException(
          HttpStatus.BAD_REQUEST,
          tenTruong + " không hợp lệ");
    }
  }

  private void kiemTraGiaTri(
      CapNhatTrangThaiDichVuDTO request) {
    if (request == null || request.getGiaTri() == null) {
      throw new ResponseStatusException(
          HttpStatus.BAD_REQUEST,
          "Giá trị cập nhật không được để trống");
    }
  }

  private String chuanHoa(String value) {
    if (value == null) {
      return null;
    }

    String result = value.trim();

    return result.isEmpty() ? null : result;
  }

  private String taoSlug(String value) {
    if (value == null || value.isBlank()) {
      return "";
    }

    String slug = Normalizer.normalize(
        value.trim(),
        Normalizer.Form.NFD);

    slug = slug.replaceAll("\\p{M}+", "");
    slug = slug.replace('đ', 'd').replace('Đ', 'D');
    slug = slug.toLowerCase(Locale.ROOT);
    slug = slug.replaceAll("[^a-z0-9]+", "-");
    slug = slug.replaceAll("^-+|-+$", "");

    return slug;
  }


}
