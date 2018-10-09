package xyz.sky731.programming.lab8

import java.io.Serializable

/** Contains information about user, such as login and password which is represented as MD5 hash-sum */
@Table("Account")
data class Account(var login: String, var password: String, @Id var id: Int?) : Serializable {
  constructor(name: String, password: String) : this(name, password, null)
}
