import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:get/get.dart';
import 'package:clout/style.dart' as style;

class Input extends StatelessWidget {
  Input(
      {super.key,
      this.placeholder,
      this.setText,
      this.suffixIcon,
      this.obscure,
      this.setObscured});

  final placeholder;
  final setText;
  final suffixIcon;
  final obscure;
  final setObscured;

  @override
  Widget build(BuildContext context) {
    return Padding(
      padding: const EdgeInsets.fromLTRB(0, 15, 0, 0),
      child: TextFormField(
          onChanged: (value) => setText(value),
          obscureText: obscure != null ? obscure : false,
          decoration: InputDecoration(
              border:
                  OutlineInputBorder(borderRadius: BorderRadius.circular(10)),
              labelText: placeholder,
              suffixIcon: suffixIcon != null && setObscured != null
                  ? IconButton(
                      onPressed: () {
                        setObscured();
                      },
                      icon: suffixIcon)
                  : null)),
    );
  }
}
