package com.ad.model;

import java.time.LocalDate;
import java.sql.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("adService")
public class AdService {

	@Autowired
	AdRepository repository;

	public void addAd(AdVO adVO) {
		repository.save(adVO);
	}

	public void updateAd(AdVO adVO) {
		repository.save(adVO);
	}

	public void deleteAd(Integer adId) {
		if (repository.existsById(adId))
			repository.deleteByAdId(adId);
	}

	public AdVO getOneAd(Integer adId) {
		Optional<AdVO> optional = repository.findById(adId);
		return optional.orElse(null);

	}

	public List<AdVO> getAll() {

		List<AdVO> allAds = repository.findAll();

		List<AdVO> list = allAds.stream().filter(ad -> Boolean.TRUE.equals(ad.getIsEnabled()))
				.collect(Collectors.toList());

		return list;

	}

	public List<AdVO> getSellerAdAll(Integer sellerId) {

		List<AdVO> allAds = repository.findSellerAdAll(sellerId);

		List<AdVO> list = allAds.stream().filter(ad -> Boolean.TRUE.equals(ad.getIsEnabled()))
				.collect(Collectors.toList());

		return list;

	}

	public List<AdVO> getReviewAd(Integer sellerId) {

		List<AdVO> allAds = repository.findSellerAdAll(sellerId);

		List<AdVO> list = allAds.stream() // for each
				.filter(ad -> "審核中".equals(ad.getAdStatus())
						|| "審核失敗".equals(ad.getAdStatus()) 
						&& Boolean.TRUE.equals(ad.getIsEnabled()))
				.collect(Collectors.toList());

		return list;
	}

	public List<AdVO> getUnLaunchAd(Integer sellerId) {

		List<AdVO> allAds = repository.findSellerAdAll(sellerId);

		List<AdVO> list = allAds.stream()
				.filter(ad -> "未上架".equals(ad.getAdStatus()) && Boolean.TRUE.equals(ad.getIsEnabled()))
				.collect(Collectors.toList());

		return list;
	}

	public List<AdVO> getLaunchAd(Integer sellerId) {

		List<AdVO> allAds = repository.findSellerAdAll(sellerId);

		List<AdVO> list = allAds.stream()
				.filter(ad -> "已上架".equals(ad.getAdStatus()) && Boolean.TRUE.equals(ad.getIsEnabled()))
				.collect(Collectors.toList());

		return list;
	}

	public List<AdVO> getPremiumHomePageAd() {

		List<AdVO> allAds = repository.findAll();

		java.util.Date du = new java.util.Date();
		long long1 = du.getTime();
		java.sql.Date currentSqlDate = new java.sql.Date(long1);

		List<AdVO> list = allAds.stream() // for each
				.filter(ad -> "已上架".equals(ad.getAdStatus()) && Boolean.TRUE.equals(ad.getIsEnabled())
						&& ad.getAdId() != 1 && Integer.valueOf(1).equals(ad.getAdLv())
						&& ad.getAdStartTime().compareTo(currentSqlDate) <= 0
						&& ad.getAdEndTime().compareTo(currentSqlDate) >= 0)
				.collect(Collectors.toList());

		if (list == null || list.size() == 0) {
			Optional<AdVO> noAd = repository.findById(1);
			list.add(noAd.orElse(null));
			return list;
		}

		return list;
	}

	public List<AdVO> getBaseHomePageAd() {

		List<AdVO> allAds = repository.findAll();

		java.util.Date du = new java.util.Date();
		long long1 = du.getTime();
		java.sql.Date currentSqlDate = new java.sql.Date(long1);

		List<AdVO> list = allAds.stream() // for each
				.filter(ad -> "已上架".equals(ad.getAdStatus()) && Boolean.TRUE.equals(ad.getIsEnabled())
						&& ad.getAdId() != 1 && Integer.valueOf(0).equals(ad.getAdLv())
						&& ad.getAdStartTime().compareTo(currentSqlDate) <= 0
						&& ad.getAdEndTime().compareTo(currentSqlDate) >= 0)
				.collect(Collectors.toList());

		if (list == null || list.size() == 0) {
			Optional<AdVO> noAd = repository.findById(1);
			list.add(noAd.orElse(null));
			return list;
		}
		
		
		return list;
	}

	public List<AdVO> getHomePageUsed() {

		List<AdVO> allAds = repository.findAll();


		List<AdVO> list = allAds.stream()
				.filter(ad -> "平台使用".equals(ad.getAdStatus()) && Boolean.TRUE.equals(ad.getIsEnabled())
						&& ad.getAdId() == 3 || ad.getAdId() == 4
					    && Integer.valueOf(0).equals(ad.getAdLv()))
				.collect(Collectors.toList());
		

		return list;
	}

}
