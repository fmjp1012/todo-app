package model

case class ViewValueCommon(
    title:  String = "default",
    cssSrc: Seq[String] = Seq.empty,
    jsSrc:  Seq[String] = Seq.empty
)
