package xyz.sky731.programming.lab8

import java.io.Serializable

/** Contains information about user, such as login and password which is represented as MD5 hash-sum */
@Table("Account")
data class Account(var login: String, var password: String, @Id var id: Int?) : Serializable, Comparable<Account> {
  constructor(login: String, password: String) : this(login, password, null)

  override fun compareTo(other: Account) = login.compareTo(other.login)
}
