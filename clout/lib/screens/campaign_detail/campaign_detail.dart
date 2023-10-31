import 'package:clout/screens/campaign_detail/widgets/campaign_detail_content.dart';
import 'package:clout/screens/campaign_detail/widgets/campaign_detail_delivery_info.dart';
import 'package:clout/screens/campaign_detail/widgets/campaign_detail_info_box.dart';
import 'package:clout/utilities/bouncing_listview.dart';
import 'package:clout/widgets/header/header.dart';
import 'package:flutter/material.dart';
import 'package:get/get.dart';
import 'package:clout/style.dart' as style;

class Campaign {
  int campaignId = 1;
  String category = '카테고리';
  String productName = '못골정미소 백미 5kg';
  String campaginSubject = '못골영농조합법인';
  String preferredFlatform = '인스타램/유튜브/틱톡';
  String endDate = '2023.10.29(일)';
  int applicantCount = 2;
  int recruitCount = 5;
  int pay = 1000;
  List<String> selectedRegions = [];
  String offeringItems = '클라우터에게 배송';
  String itemDetail = '1. 못골 정미소 백미 5kg \n2. 광고비 1000포인트';
  String requirements = '잘 부탁드립니다~~';
}

String caution = '법적 고지, 책임은 계약 당사자 간 있다, 등등...';

class CampaignDetail extends StatelessWidget {
  CampaignDetail({super.key});

  // campaignId GetX에서 argument로 얻어서 api 통신으로 어떤 캠페인 정보 보여줄지 표시해야함
  var campaignId = Get.arguments;

  Campaign campaign = Campaign();

  // GetX Controller에 클라우터인지 광고주인지 저장해놓고(리코일처럼)
  // 버튼 다르게 보이게 해야함

  @override
  Widget build(BuildContext context) {
    return Scaffold(
        appBar: PreferredSize(
          preferredSize: Size.fromHeight(70),
          child: Header(
            header: 3,
            headerTitle: campaign.productName,
          ),
        ),
        body: Container(
          color: Colors.white,
          width: double.infinity,
          child: BouncingListview(
              child: FractionallySizedBox(
            widthFactor: 0.9,
            child: Column(
              crossAxisAlignment: CrossAxisAlignment.start,
              children: [
                // 사진 캐러셀 컴포넌트는 일단 생략
                // 캠페인 정보 상자
                CampaignDetailInfoBox(campaign: campaign),
                SizedBox(height: 20),
                // 배송 여부 상자
                CampaignDetailDeliveryInfo(),
                CampaignDetailContent(
                    title: '협찬 제공 방법',
                    content: Text(
                      campaign.offeringItems,
                      style: style.textTheme.bodyLarge,
                    )),
                CampaignDetailContent(
                    title: '제공 내역',
                    content: Text(
                      campaign.itemDetail,
                      style: style.textTheme.bodyLarge,
                    )),
                CampaignDetailContent(
                    title: '요구사항',
                    content: Text(
                      campaign.requirements,
                      style: style.textTheme.bodyLarge,
                    )),
                CampaignDetailContent(
                    title: '주의사항',
                    content: Text(
                      caution,
                      style: style.textTheme.bodyLarge,
                    ))
              ],
            ),
          )),
        ));
  }
}