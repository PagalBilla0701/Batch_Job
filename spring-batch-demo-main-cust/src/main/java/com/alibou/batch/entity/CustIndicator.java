package com.alibou.batch.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Data
@Entity
@Table(name = "COPS_CUST_INDICATORS")
public class CustIndicator {

    @Id
    @Column(name = "REL_ID", length = 25)
    private String relId;

    @Column(name = "F_KYC_STATUS", length = 1)
    private String fKycStatus;

    @Column(name = "F_TRANSFER_EXCLUSION", length = 1)
    private String fTransferExclusion;

    @Column(name = "F_SENSITIVE_CUST", length = 1)
    private String fSensitiveCust;

    @Column(name = "RBS_CUST", length = 1)
    private String rbsCust;

    @Column(name = "N_KYC_STATUS_FILE_ID")
    private Integer nKycStatusFileId;

    @Column(name = "N_TRANSFER_EXCLUSION_FILE_ID")
    private Integer nTransferExclusionFileId;

    @Column(name = "N_SENSITIVE_CUST_FILE_ID")
    private Integer nSensitiveCustFileId;

    @Column(name = "N_RBS_CUST_FILE_ID")
    private Integer nRbsCustFileId;

    @Column(name = "D_CREAT")
    @Temporal(TemporalType.DATE)
    private Date dCreat;

    @Column(name = "D_UPD")
    @Temporal(TemporalType.DATE)
    private Date dUpd;

    @Column(name = "X_CREAT", length = 25)
    private String xCreat;

    @Column(name = "X_UPD", length = 25)
    private String xUpd;

}
