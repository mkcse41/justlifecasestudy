package com.justlife.casestudy.dto.response;

import lombok.Getter;
import lombok.Setter;

/**
 * 
 * @author Mukesh.K
 *
 */
@Setter
@Getter
public class APIResponseDTO {

	private String errMsg;
	private Object data;
	
	public static APIResponseDTO success(Object data) {
        APIResponseDTO dto = new APIResponseDTO();
        dto.setData(data);
        return dto;
    }

    public static APIResponseDTO error(String msg) {
        APIResponseDTO dto = new APIResponseDTO();
        dto.setErrMsg(msg);
        return dto;
    }
}
