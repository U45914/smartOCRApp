CREATE TABLE IF NOT EXISTS OCR_INFO (
	OCR_REQUEST_ID INTEGER PRIMARY KEY,
	GI_VISION_RESPONSE VARCHAR ,
	CROWD_SOURCE_RESPONSE VARCHAR ,
	ABZOOBA_REQUEST_INFO VARCHAR,
	ABZOOBA_REQUEST_RESPONSE VARCHAR,
	CLOUD_SOURCE_USER_ID VARCHAR,
	STATUS VARCHAR,
	IMAGE_URL VARCHAR,
	IMAGE BLOB,
	IMAGE_UPLOAD_DATE DATE,
	IMAGE_PROCESSED_DATE DATE,
	CROWD_SOURCE_SUBMIT_DATE DATE
);
