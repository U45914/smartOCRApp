/**
 * 
 */
package com.walmart.ocr.dao.impl;

import java.io.Serializable;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.walmart.ocr.dao.OcrInfoDao;
import com.walmart.ocr.model.SmartOCRDataModel;
import com.walmart.ocr.util.HQLQueryConstants;

/**
 * @author U46591
 *
 */

@Repository
public class OcrInfoDaoImpl implements OcrInfoDao {

	Logger LOGGER = Logger.getLogger(OcrInfoDaoImpl.class);
	@Autowired
	SessionFactory template;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.walmart.ocr.dao.OcrInfoDao#save(com.walmart.ocr.model.
	 * SmartOCRDataModel)
	 */
	@Override
	@Transactional(readOnly = false)
	public Serializable createOcrData(SmartOCRDataModel ocrDataModel) {

		LOGGER.info("persist ");
		Serializable id = null;
		Session session = template.openSession();
		id = session.save(ocrDataModel);
		return id;
	}

	@Override
	@Transactional(readOnly = false)
	public void updateOcrData(SmartOCRDataModel ocrDataModel) {
		Session session = template.openSession();
		try {
			// session.update(ocrDataModel);
			Query query = session.createQuery(HQLQueryConstants.UPDATE_QUERY);
			query.setParameter(0, ocrDataModel.getGivisionResponse());
			query.setParameter(1, ocrDataModel.getAbsoobaRequestInfo());
			query.setParameter(2, ocrDataModel.getAbsoobaResponse());
			query.setParameter(3, ocrDataModel.getCrowdSourceUserId());
			query.setParameter(4, ocrDataModel.getStatus());
			query.setParameter(5, ocrDataModel.getImageUrls());// query.setParameter(5,
																// "updated the
																// image url
																// manually");//
			query.setParameter(6, ocrDataModel.getImage());
			query.setParameter(7, ocrDataModel.getImageUploadDate());
			query.setParameter(8, ocrDataModel.getImageProcessedDate());
			query.setParameter(9, ocrDataModel.getCloudeSourceSubmitDate());
			query.setParameter(10, ocrDataModel.getCrowdSourceResponse());
			query.setParameter(11, ocrDataModel.getAbzoobaResponse2());
			query.setParameter(12, ocrDataModel.getOcrRequestId());

			int updatedCount = query.executeUpdate();
			System.out.println("----> ******************************************** " + updatedCount);
		} catch (Exception e) {
			System.err.println(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.walmart.ocr.dao.OcrInfoDao#get(java.io.Serializable)
	 */
	@Override
	public SmartOCRDataModel findOcrDataById(Serializable id) {
		// TODO Auto-generated method stub
		LOGGER.info("retrieve ocr data");
		SmartOCRDataModel dataModel = null;
		Criteria criteria = template.openSession().createCriteria(SmartOCRDataModel.class);
		criteria.add(Restrictions.eq("ocrRequestId", id));

		dataModel = (SmartOCRDataModel) criteria.uniqueResult();
		return dataModel;
	}

	@SuppressWarnings({ "unchecked" })
	@Override
	public List<SmartOCRDataModel> findAllOcrData() {
		Session session = template.openSession();
		String hql = "From SmartOCRDataModel";
		List<SmartOCRDataModel> users = null;
		try {
			Query query = session.createQuery(hql);
			users = query.list();
		} catch (Exception e) {
			System.out.println(e);
		}
		return users;
	}

}
