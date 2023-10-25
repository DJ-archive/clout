import 'package:clout/widgets/input/input_elements/utilities/numeric_range_formatter.dart';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';

class TextInput extends StatelessWidget {
  const TextInput(
      {super.key,
      this.setData,
      this.placeholder,
      this.keyboardType,
      this.maxLength,
      this.maxValue});

  final setData;
  final placeholder;
  final keyboardType;
  final maxLength;
  final maxValue;

  @override
  Widget build(BuildContext context) {
    return TextFormField(
      inputFormatters:
          maxValue != -1 ? [NumericRangeFormatter(min: 0, max: maxValue)] : [],
      onChanged: (value) => setData(value),
      keyboardType: keyboardType,
      minLines: 5,
      maxLines: 5,
      maxLength: maxLength,
      decoration: InputDecoration(
        contentPadding: EdgeInsets.only(top: 30, left: 15, right: 15),
        border: OutlineInputBorder(borderRadius: BorderRadius.circular(5)),
        hintText: placeholder,
      ),
    );
  }
}