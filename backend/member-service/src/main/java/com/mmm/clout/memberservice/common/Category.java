package com.mmm.clout.memberservice.common;

public enum Category {

    FASHION_BEAUTY("패션/뷰티"),
    HEALTH_LIFESTYLE("건강/생활"),
    TRAVEL_LEISURE("여행/레저"),
    PARENTING("육아"),
    ELECTRONICS("전자제품"),
    FOOD("음식"),
    VISIT_EXPERIENCE("방문/체험"),
    PETS("반려동물"),
    GAMES("게임"),
    ECONOMY_BUSINESS("경제/비즈니스"),
    OTHERS("기타");

    private final String details;

    Category(String details) {
        this.details = details;
    }

}
