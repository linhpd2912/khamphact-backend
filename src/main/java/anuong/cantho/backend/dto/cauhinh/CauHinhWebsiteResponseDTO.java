package anuong.cantho.backend.dto.cauhinh;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CauHinhWebsiteResponseDTO {

  private Long id;

  // Thương hiệu
  private String tenWebsite;
  private String slogan;
  private String logo;
  private String favicon;

  // ==============================
  // BANNER TRANG CHỦ
  // ==============================

  private String bannerTrangChu;
  private String bannerAlt;
  private String bannerTieuDe;
  private String bannerMoTa;
  private String bannerLink;
  private Boolean hienThiBanner;

  // Liên hệ
  private String email;
  private String soDienThoai;
  private String diaChi;

  // Mạng xã hội
  private String facebook;
  private String zalo;
  private String youtube;
  private String instagram;
  private String tiktok;

  // ==========================================
  // THEO DÕI WEBSITE
  // ==========================================

  // Google Analytics
  private String maGoogleAnalytics;
  private Boolean batGoogleAnalytics;

  // Google Search Console
  private String maGoogleSearchConsole;
  private Boolean batGoogleSearchConsole;

  // Facebook Pixel
  private String maFacebookPixel;
  private Boolean batFacebookPixel;

  // SEO
  private String tieuDeSeoTrangChu;
  private String moTaSeoTrangChu;
  private String tuKhoaSeoTrangChu;

  // Footer
  private String chanTrangWebsite;

}
