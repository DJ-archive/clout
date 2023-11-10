import 'package:clout/hooks/apis/register_api.dart';
import 'package:clout/providers/user_controllers/advertiser_controller.dart';
import 'package:clout/providers/user_controllers/advertiser_info_controller.dart';
import 'package:clout/screens/register_or_modify/advertiser/widgets/advertiser_join_or_modify_1.dart';
import 'package:clout/screens/register_or_modify/advertiser/widgets/advertiser_join_or_modify_2.dart';
import 'package:clout/utilities/bouncing_listview.dart';
import 'package:clout/widgets/buttons/big_button.dart';
import 'package:flutter/material.dart';
import 'package:fluttertoast/fluttertoast.dart';
import 'package:get/get.dart';
import 'package:percent_indicator/linear_percent_indicator.dart';
import '../../../style.dart' as style;

class AdvertiserJoin extends StatefulWidget {
  const AdvertiserJoin({super.key});

  @override
  State<AdvertiserJoin> createState() => _AdvertiserJoinState();
}

class _AdvertiserJoinState extends State<AdvertiserJoin> {
  int pageNum = 1;
  double percent = 1 / 2;

  final advertiserController = Get.put(AdvertiserController());
  final advertiserRegisterController =
      Get.put(AdvertiserInfoController(), tag: 'advertiserRegister');

  showSnackBar() {
    Get.snackbar(
      '',
      '',
      // snackPosition: SnackPosition.BOTTOM,
      duration: Duration(seconds: 4),
      titleText: Text(
        '🥳 회원 가입 완료',
        style: style.textTheme.headlineMedium?.copyWith(
          fontWeight: FontWeight.w700,
          color: Colors.black,
        ),
      ),
      messageText: Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          Text(
            '가입을 진심으로 축하드려요',
            style: style.textTheme.bodyMedium?.copyWith(
              fontWeight: FontWeight.w400,
              color: Colors.black,
            ),
          ),
          Text(
            '성공적인 광고 계약을 기원할게요 🙌',
            style: style.textTheme.bodyMedium?.copyWith(
              fontWeight: FontWeight.w400,
              color: Colors.black,
            ),
          ),
        ],
      ),
      backgroundColor: Colors.white,
      borderWidth: 5,
      borderColor: style.colors['main1'],
      margin: EdgeInsets.only(
        top: 15,
        left: 20,
        right: 20,
      ),
    );
  }

  goNext() {
    if (pageNum == 1) {
      String validationMsg = advertiserRegisterController.canGoSecondPage();
      if (validationMsg == '') {
        setState(() {
          pageNum += 1;
          percent = pageNum / 2;
        });
      } else {
        Fluttertoast.showToast(msg: validationMsg);
      }
    } else {
      String validationMsg = advertiserRegisterController.canRegister();
      if (validationMsg == '') {
        register();
      } else {
        Fluttertoast.showToast(msg: validationMsg);
      }
    }
  }

  register() {
    advertiserRegisterController.setAdvertiser();
    advertiserRegisterController.printAll();
    // 가입 api 호출
    final RegisterApi registerApi = RegisterApi();

    registerApi.postRequest(
        '/member-service/v1/advertisers/signup', advertiserRegisterController.advertiser);
    Get.offNamed('/login');
    showSnackBar();
  }

  @override
  Widget build(BuildContext context) {
    advertiserController.setControllerTag('advertiserRegister');
    advertiserRegisterController.runOtherControllers();
    return GetBuilder<AdvertiserInfoController>(
      tag: 'advertiserRegister',
      builder: (controller) => Scaffold(
        body: LayoutBuilder(
          builder: (context, constraint) {
            return SizedBox(
              width: double.infinity,
              child: BouncingListview(
                child: ConstrainedBox(
                  constraints: BoxConstraints(minHeight: constraint.maxHeight),
                  child: IntrinsicHeight(
                    child: FractionallySizedBox(
                      widthFactor: 0.9,
                      child: Column(
                        children: [
                          Expanded(
                            flex: 0,
                            child: Column(
                              crossAxisAlignment: CrossAxisAlignment.start,
                              children: [
                                SizedBox(height: 80),
                                Text('가입하고',
                                    style: style.textTheme.titleMedium),
                                Row(
                                  children: [
                                    Image.asset(
                                      'assets/images/Clout_Logo.png',
                                      width: 100,
                                    ),
                                    Text(' 와 함께',
                                        style: style.textTheme.titleMedium),
                                  ],
                                ),
                                Text('매칭해요',
                                    style: style.textTheme.titleMedium),
                                SizedBox(height: 10),
                                LinearPercentIndicator(
                                  animation: true,
                                  animationDuration: 1000,
                                  padding: EdgeInsets.zero,
                                  percent: percent,
                                  animateFromLastPercent: true,
                                  lineHeight: 10,
                                  backgroundColor: style.colors['category'],
                                  progressColor: style.colors['main1'],
                                  barRadius: Radius.circular(5),
                                ),
                                SizedBox(height: 50),
                              ],
                            ),
                          ),
                          SizedBox(height: 20),
                          Expanded(
                            flex: 2,
                            child: Column(children: [
                              //페이지별로 보여주기
                              if (pageNum == 1)
                                AdvertiserJoinOrModify1(
                                  modifying: false,
                                  controllerTag: 'advertiserRegister',
                                ),
                              if (pageNum == 2)
                                AdvertiserJoinOrModify2(
                                  modifying: false,
                                  controllerTag: 'advertiserRegister',
                                ),
                            ]),
                          ),
                          SizedBox(height: 20),
                          SizedBox(
                            height: 50,
                            child: FractionallySizedBox(
                              widthFactor: 1,
                              child: BigButton(
                                title: pageNum == 2
                                    ? '완료'
                                    : '다음', // pageNum에 따라 버튼 텍스트 변경
                                function: goNext,
                              ),
                            ),
                          ),
                          SizedBox(height: 20)
                        ],
                      ),
                    ),
                  ),
                ),
              ),
            );
          },
        ),
      ),
    );
  }
}
