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

	public List<AdVO> getReviewAd() {

		List<AdVO> allAds = repository.findAll();

		List<AdVO> list = allAds.stream() // for each
				.filter(ad -> "審核中".equals(ad.getAdStatus()) && Boolean.TRUE.equals(ad.getIsEnabled()))
				.collect(Collectors.toList());

		return list;
	}

	public List<AdVO> getUnLaunchAd() {

		List<AdVO> allAds = repository.findAll();

		List<AdVO> list = allAds.stream()
				.filter(ad -> "未上架".equals(ad.getAdStatus()) && Boolean.TRUE.equals(ad.getIsEnabled()))
				.collect(Collectors.toList());

		return list;
	}

	public List<AdVO> getLaunchAd() {

		List<AdVO> allAds = repository.findAll();

		List<AdVO> list = allAds.stream()
				.filter(ad -> "已上架".equals(ad.getAdStatus()) && Boolean.TRUE.equals(ad.getIsEnabled()))
				.collect(Collectors.toList());

		return list;
	}

	public List<AdVO> getHomePageAd() {

		List<AdVO> allAds = repository.findAll();

		java.util.Date du = new java.util.Date();
		long long1 = du.getTime();
		java.sql.Date currentSqlDate = new java.sql.Date(long1);

		List<AdVO> list = allAds.stream() // for each
				.filter(ad -> "已上架".equals(ad.getAdStatus()) && Boolean.TRUE.equals(ad.getIsEnabled())
						&& ad.getAdStartTime().compareTo(currentSqlDate) <= 0
						&& ad.getAdEndTime().compareTo(currentSqlDate) >= 0)
				.collect(Collectors.toList());

		return list;
	}

}
