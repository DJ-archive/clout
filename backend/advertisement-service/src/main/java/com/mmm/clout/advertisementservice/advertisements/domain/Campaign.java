package com.mmm.clout.advertisementservice.advertisements.domain;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import org.hibernate.annotations.DynamicInsert;
import org.springframework.transaction.annotation.Transactional;

@Getter
@ToString
@AllArgsConstructor
@DynamicInsert
@Table(name = "campaign")
@Entity
@DiscriminatorValue("CP")
public class Campaign extends Advertisement {

    private String title;

    @Enumerated(EnumType.STRING)
    private AdCategory adCategory; // 광고 카테고리

    private Boolean isPriceChangeable; // 광고비 협의 여부

    private Boolean isDeliveryRequired; // 배송 여부

    private Integer numberOfRecruiter; // 모집인원

    private Integer numberOfApplicants; // 신청인원

    private String offeringDetails; // 제공 내역 설명

    private String sellingLink;

    private LocalDate applyStartDate; // 모집 시작 날짜

    private LocalDate applyEndDate; // 모집 종료 날짜

    private Integer minClouterAge;

    private Integer maxClouterAge;

    private Integer minFollower;

    @ElementCollection(targetClass = Region.class, fetch = FetchType.LAZY)
    @CollectionTable(name = "advertisement_region", joinColumns = @JoinColumn(name = "advertisement_id"))
    @Enumerated(EnumType.STRING)
    @Column(name = "region")
    private List<Region> regionList = new ArrayList<>();

    protected Campaign() {
        super();
    }

    public Campaign(
        Long registerId,
        List<AdPlatform> adPlatformList,
        Long price,
        String details,
        String title,
        AdCategory category,
        Boolean isPriceChangeable,
        Boolean isDeliveryRequired,
        Integer numberOfRecruiter,
        String offeringDetails,
        String sellingLink,
        Integer minClouterAge,
        Integer maxClouterAge,
        Integer minFollower,
        List<Region> regionList
    ) {
        super(
            registerId,
            adPlatformList,
            price,
            details
        );

        this.title = title;
        this.adCategory = category;
        this.isPriceChangeable = isPriceChangeable;
        this.isDeliveryRequired = isDeliveryRequired;
        this.numberOfRecruiter = numberOfRecruiter;
        this.numberOfApplicants = 0;
        this.offeringDetails = offeringDetails;
        this.sellingLink = sellingLink;
        this.minClouterAge = minClouterAge;
        this.maxClouterAge = maxClouterAge;
        this.minFollower = minFollower;
        this.applyStartDate = LocalDate.now();
        this.applyEndDate = this.applyStartDate.plusMonths(1);
        this.regionList = regionList;
    }

    public static Campaign create(
        Long registerId,
        List<AdPlatform> adPlatformList,
        Long price,
        String details,
        String title,
        AdCategory category,
        Boolean isPriceChangeable,
        Boolean isDeliveryRequired,
        Integer numberOfRecruiter,
        String offeringDetails,
        String sellingLink,
        Integer minClouterAge,
        Integer maxClouterAge,
        Integer minFollower,
        List<Region> regionList
    ) {
        return new Campaign(
            registerId,
            adPlatformList,
            price,
            details,
            title,
            category,
            isPriceChangeable,
            isDeliveryRequired,
            numberOfRecruiter,
            offeringDetails,
            sellingLink,
            minClouterAge,
            maxClouterAge,
            minFollower,
            regionList
        );
    }

    public void update(
        List<AdPlatform> adPlatformList,
        long price,
        String details,
        String title,
        AdCategory adCategory,
        boolean priceChangeable,
        boolean deliveryRequired,
        int numberOfRecruiter,
        String offeringDetails,
        String sellingLink,
        int minClouterAge,
        int maxClouterAge,
        int minFollower,
        List<Region> regionList
    ) {
        super.update(adPlatformList, price, details);
        this.title = title;
        this.adCategory = adCategory;
        this.isPriceChangeable = priceChangeable;
        this.isDeliveryRequired = deliveryRequired;
        this.numberOfRecruiter = numberOfRecruiter;
        this.offeringDetails = offeringDetails;
        this.sellingLink = sellingLink;
        this.minClouterAge = minClouterAge;
        this.maxClouterAge = maxClouterAge;
        this.minFollower = minFollower;
        this.regionList = regionList;
    }
}