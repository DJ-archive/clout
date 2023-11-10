package com.mmm.clout.pointservice.point.presentation.response;

import com.mmm.clout.pointservice.point.domain.Point;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ChargePointResponse {

    @Schema(description = "포인트 고유 식별자(id)")
    private Long pointId;

    @Schema(description = "멤버 고유 식별자(id)")
    private Long memberId;

    @Schema(description = "충전한 포인트")
    private Long chargingPoint;

    @Schema(description = "현재 멤버 총 포인트")
    private Long totalPoint;


    public static ChargePointResponse from(Point point, Long chargingPoint) {
        return new ChargePointResponse(
            point.getId(),
            point.getMemberId(),
            chargingPoint,
            point.getTotalPoint()
        );
    }
}
