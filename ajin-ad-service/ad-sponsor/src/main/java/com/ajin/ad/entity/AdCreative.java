package com.ajin.ad.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

/**
 * 广告的创意,给用户看的具体的广告数据，可以是图片，可以是视频
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "ad_creative")
public class AdCreative {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Basic
    @Column(name = "name", nullable = false)
    private String name;
    /**
     * 创意的类型，如文本，图片，视频等
     */
    @Basic
    @Column(name = "type", nullable = false)
    private Integer type;

    /**
     * 物料的类型，比如图片可以是bmp，或者jpg等等
     */
    @Basic
    @Column(name = "material_type", nullable = false)
    private Integer materialType;

    /**
     * 物料的高度
     */
    @Basic
    @Column(name = "height", nullable = false)
    private Integer height;


    @Basic
    @Column(name = "width", nullable = false)
    private Integer width;

    /**
     * 物料的大小
     */
    @Basic
    @Column(name = "size", nullable = false)
    private Long size;

    /**
     * 物料是视频时表示：视频的时长，非视频物料，该属性为0
     */
    @Basic
    @Column(name = "duration", nullable = false)
    private Integer duration;

    /**
     * 审核状态
     */
    @Basic
    @Column(name = "audit_status", nullable = false)
    private Integer auditStatus;

    @Basic
    @Column(name = "user_id", nullable = false)
    private Long userId;

    /**
     * 物料的url
     */
    @Basic
    @Column(name = "url", nullable = false)
    private String url;

    @Basic
    @Column(name = "create_time", nullable = false)
    private Date createTime;

    @Basic
    @Column(name = "update_time", nullable = false)
    private Date updateTime;

}
