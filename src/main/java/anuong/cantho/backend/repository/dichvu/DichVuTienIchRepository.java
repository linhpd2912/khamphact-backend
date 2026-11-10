package anuong.cantho.backend.repository.dichvu;

import org.springframework.stereotype.Repository;

import anuong.cantho.backend.entity.dichvu.DichVuTienIch;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;


@Repository 
public interface DichVuTienIchRepository extends JpaRepository<DichVuTienIch, Long>{

  long countByDichVu_Id(Long dichVuId);

  boolean existsByDichVu_Id(Long dichVuId);

  // Lấy tất cả tiện ích được gán cho một dịch vụ
  List<DichVuTienIch> findByDichVu_IdOrderByTienIch_TenTienIchAsc(Long dichVuId);

  // Kiểm tra tiện ích đã được gán cho dịch vụ chưa
  boolean existsByDichVu_IdAndTienIch_Id(Long dichVuId, Long tienIchId);

  // Tìm quan hệ giữa một dịch vụ và một tiện ích
  Optional<DichVuTienIch> findByDichVu_IdAndTienIch_Id(Long dichVuId, Long tienIchId);

  // Kiểm tra tiện ích có đang được dịch vụ nào sử dụng không
  boolean existsByTienIch_Id(Long tienIchId);

  // Lấy tất cả dịch vụ đang sử dụng tiện ích
  List<DichVuTienIch> findByTienIch_Id(Long tienIchId);

}
