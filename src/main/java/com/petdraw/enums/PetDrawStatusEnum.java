package com.petdraw.enums;

public enum PetDrawStatusEnum {
	    NOT_DRAWN_YET(0, "今日尚未抽卡"),
	    DRAWN_WAITING_FOR_RESPONSE(1, "今日已經抽卡，但尚未得到回覆"),
	    DRAWN_REJECTED(2, "今日抽卡但被拒絕"),
	    DRAWN_SUCCESS(3, "抽卡成功");

	    private final int code;
	    private final String description;

	    PetDrawStatusEnum(int code, String description) {
	        this.code = code;
	        this.description = description;
	    }

	    public int getCode() {
	        return code;
	    }

	    public String getDescription() {
	        return description;
	    }

	    public static PetDrawStatusEnum fromCode(int code) {
	        for (PetDrawStatusEnum status : PetDrawStatusEnum.values()) {
	            if (status.getCode() == code) {
	                return status;
	            }
	        }
	        throw new IllegalArgumentException("未知狀態: " + code);
	    }
	}
