package anuong.cantho.backend.repository.dichvu;

import java.util.*;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import anuong.cantho.backend.entity.dichvu.DanhMucDichVu;

@Repository
public interface DanhMucDichVuRepository extends JpaRepository<DanhMucDichVu, Long> {

  // Kiểm tra slug đã tồn tại hay chưa
  boolean existsByDuongDanSeo(String duongDanSeo);

  // Kiểm tra slug trùng với bản ghi khác, bỏ qua ID hiện tại
  boolean existsByDuongDanSeoAndIdNot(String duongDanSeo, Long id);

  // Tìm danh mục theo slug
  Optional<DanhMucDichVu> findByDuongDanSeo(String duongDanSeo);

  // Danh mục đang hoạt động
  List<DanhMucDichVu> findByHoatDongTrueOrderByThuTuHienThiAscIdAsc();

  // Danh mục nổi bật và đang hoạt động
  List<DanhMucDichVu> findByNoiBatTrueAndHoatDongTrueOrderByThuTuHienThiAscIdAsc();

  // Tìm kiếm theo tên, mô tả hoặc slug
  @Query("""
      SELECT d
      FROM DanhMucDichVu d
      WHERE
          LOWER(d.tenDanhMuc) LIKE LOWER(CONCAT('%', :keyword, '%'))
          OR LOWER(COALESCE(d.moTa, ''))
              LIKE LOWER(CONCAT('%', :keyword, '%'))
          OR LOWER(COALESCE(d.duongDanSeo, ''))
              LIKE LOWER(CONCAT('%', :keyword, '%'))
      """)
  Page<DanhMucDichVu> timKiem(
      @Param("keyword") String keyword,
      Pageable pageable);

}
