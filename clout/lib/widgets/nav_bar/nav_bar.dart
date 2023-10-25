import 'package:flutter/material.dart';
import 'package:get/get.dart';
import '../../style.dart' as style;
import 'package:get/get.dart';
import '../../providers/nav_bar_controller.dart';
import '../../screens/campaign_list/campaign_list.dart';

class NavBar extends StatelessWidget {
  NavBar({super.key, this.tab, this.setTab});

  final tab;

  final setTab;

  @override
  Widget build(BuildContext context) {
    Get.put(NavBarController());
    return Container(
        height: 70,
        decoration: BoxDecoration(
          boxShadow: <BoxShadow>[
            // 그림자효과
            BoxShadow(
              color: Colors.grey,
              blurRadius: 2,
            ),
          ],
        ),
        child: GetBuilder<NavBarController>(builder: (controller) {
          return BottomNavigationBar(
            showSelectedLabels: false,
            showUnselectedLabels: false,
            selectedIconTheme:
                IconThemeData(color: style.colors['main1'], size: 30),
            unselectedIconTheme:
                IconThemeData(color: style.colors['gray'], size: 27), //
            type: BottomNavigationBarType.fixed,
            items: [
              BottomNavigationBarItem(
                  icon: Icon(Icons.home_outlined), label: '홈'),
              BottomNavigationBarItem(
                  icon: Icon(Icons.content_paste_search_outlined), label: '목록'),
              BottomNavigationBarItem(
                  icon: Icon(Icons.chat_bubble_outline), label: '찜'),
              BottomNavigationBarItem(
                  icon: Icon(Icons.person_2_outlined), label: '마이페이지'),
            ],
            onTap: (value) => controller.setTab(value),
            currentIndex: controller.tab,
          );
        }));
  }
}