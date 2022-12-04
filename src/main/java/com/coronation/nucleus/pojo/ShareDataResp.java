package com.coronation.nucleus.pojo;

import lombok.Data;

import java.util.List;

/**
 * @author toyewole
 */
@Data
public class ShareDataResp {
    List<ShareDTO> shareDTOList;
    Long count;
}
