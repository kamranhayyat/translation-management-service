package com.dev.kamran.dto;

import com.dev.kamran.entity.TranslationExportTask;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ExportTaskReq {
    private Long id;
    private String userId;
    private String status;
    private String resultUrl;

    public static ExportTaskReq fromEntity(TranslationExportTask t) {
        ExportTaskReq d = new ExportTaskReq();
        d.setId(t.getId());
        d.setUserId(t.getUserId());
        d.setStatus(t.getStatus());
        d.setResultUrl(t.getResultUrl());
        return d;
    }
}
