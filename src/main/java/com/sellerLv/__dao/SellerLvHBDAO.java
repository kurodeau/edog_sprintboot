package com.sellerLv.__dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.sellerLv.entity.SellerLvVO;

// import util.HibernateUtil;

public class SellerLvHBDAO implements SellerLvDAO {

	private SessionFactory factory;

	public SellerLvHBDAO() {
		 // factory = HibernateUtil.getSessionFactory();
	}

	private Session getSession() {
		return factory.getCurrentSession();
	}

	@Override
	public Integer insert(SellerLvVO sellerLv) {

		return (Integer) getSession().save(sellerLv);
	}

	@Override
	public Integer update(SellerLvVO sellerLv) {
		try {
			getSession().update(sellerLv);
			return 1;
		} catch (Exception e) {
			return -1;
		}
	}

	@Override
	public Integer delete(Integer sellerLvId) {
		SellerLvVO sellerLv = getSession().get(SellerLvVO.class, sellerLvId);
		try {
			if (sellerLv != null) {
				getSession().delete(sellerLvId);
				return 1;
			} else {
				return 0;
			}
		} catch (Exception e) {
			return -1;
		}
	}

	@Override
	public SellerLvVO findByPrimaryKey(Integer sellerLvId) {
		return getSession().get(SellerLvVO.class, sellerLvId);
	}

	@Override
	public List<SellerLvVO> getAll() {
		return getSession().createQuery("from SellerLvVO", SellerLvVO.class).list();
	}

	@Override
	public Integer getTotal() {
		Long total = getSession().createQuery("select count(*) from SellerLvVO",Long.class).uniqueResult();
		return total.intValue();
	}
	
//	public static void main(String[] args) {
//		// 創建 Hibernate 配置和測試 DAO 實例
//		SessionFactory factory = HibernateUtil.getSessionFactory();
//
//		Session session = factory.getCurrentSession();
//		session.beginTransaction();

		// ================Insert==============
//		SellerLvVO sellerLv = new SellerLvVO();
//		sellerLv.setLvName("TEST");
//		sellerLv.setPlatformCommission(BigDecimal.valueOf(0.1));
//		sellerLv.setAdAllowType(1);
//		sellerLv.setIsExportGoldflow(true);
//		sellerLv.setFreightSub(2);
//		sellerLv.setReturnSubPerMonth(1);
//		sellerLv.setIsShowPriority(true);
//		sellerLv.setShelvesNumber(10);
//
//		Serializable sellerLvId = session.save(sellerLv);

//		// ================Update==============
//		SellerLvVO sellerLvUpdate = session.get(SellerLvVO.class, (Integer) sellerLvId);
//		sellerLvUpdate.setLvName("DELETE");
//		session.update(sellerLvUpdate);
//
//		// ================Query==============
//		SellerLvVO sellerLvQuery = session.get(SellerLvVO.class, 1);
//
//		Set<SellerVO> sellers = sellerLvQuery.getSellers();
//
//		for (SellerVO seller : sellers) {
//			System.out.println(seller.getSellerCompany());
//		}
//
//		// ================Delete==============
//		SellerLvVO sellerLvDel = session.get(SellerLvVO.class, (Integer) sellerLvId);
//		session.delete(sellerLvDel);
		
		
//		session.getTransaction().commit();
//
//
//	
//		HibernateUtil.shutdown();
//	}

}