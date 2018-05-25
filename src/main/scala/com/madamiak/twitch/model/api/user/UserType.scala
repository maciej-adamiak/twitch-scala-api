package com.madamiak.twitch.model.api.user

object UserType extends Enumeration {

  type UserType = UserType.Value

  val Staff: UserType     = Value("staff")
  val Admin: UserType     = Value("admin")
  val GlobalMod: UserType = Value("global_mod")
  val Undefined: UserType = Value("")

}
